// File: 'app/src/main/java/com/example/allerscan/ui/qrscan/QrScanFragment.kt'
package com.example.allerscan.ui.qrscan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.allerscan.databinding.FragmentQrScanBinding
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.camera.CameraSettings
import com.google.zxing.BarcodeFormat
import com.example.allerscan.BarcodeIngredientLookup
import com.example.allerscan.AllergenChecker
import com.example.allerscan.database.Product

class QrScanFragment : Fragment() {
    private var _binding: FragmentQrScanBinding? = null
    private val binding get() = _binding!!
    private val vm by viewModels<QrScanViewModel>()
    private var isScanning = true
    private var lastScannedCode: String? = null
    private var lastScanTime = 0L

    private val barcodeCallback = BarcodeCallback { result ->
        if (result == null || !isScanning) return@BarcodeCallback

        // Debounce: prevent duplicate scans within 3 seconds
        val now = System.currentTimeMillis()
        if (result.text == lastScannedCode && (now - lastScanTime) < 3000) {
            return@BarcodeCallback
        }

        lastScannedCode = result.text
        lastScanTime = now
        handleBarcodeScan(result.text)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentQrScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.ensureDefaults()

        // Configure camera settings to fix mirroring
        val cameraSettings = CameraSettings()
        cameraSettings.requestedCameraId = -1 // Use back camera
        cameraSettings.isAutoFocusEnabled = true
        cameraSettings.isContinuousFocusEnabled = true
        binding.barcodeScanner.barcodeView.cameraSettings = cameraSettings

        // Remove status text and hide laser line
        binding.barcodeScanner.setStatusText("")

        // Try to disable laser if the method exists
        try {
            binding.barcodeScanner.viewFinder.setLaserVisibility(false)
        } catch (e: Exception) {
            // Method might not exist in this version
        }

        // Set supported barcode formats
        val formats = listOf(
            BarcodeFormat.QR_CODE,
            BarcodeFormat.CODE_128,
            BarcodeFormat.CODE_39,
            BarcodeFormat.EAN_13,
            BarcodeFormat.EAN_8,
            BarcodeFormat.UPC_A,
            BarcodeFormat.UPC_E
        )
        binding.barcodeScanner.barcodeView.decoderFactory =
            com.journeyapps.barcodescanner.DefaultDecoderFactory(formats)

        // Start continuous decoding
        binding.barcodeScanner.decodeContinuous(barcodeCallback)

        binding.cbFlash.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.barcodeScanner.setTorchOn()
            } else {
                binding.barcodeScanner.setTorchOff()
            }
        }

        binding.btnToggleScan.setOnClickListener {
            isScanning = !isScanning
            if (isScanning) {
                binding.btnToggleScan.text = "Pause Scan"
                binding.barcodeScanner.resume()
                binding.tvResult.text = "Point camera at barcode"
            } else {
                binding.btnToggleScan.text = "Resume Scan"
                binding.barcodeScanner.pause()
                binding.tvResult.text = "Scanning paused"
            }
        }
    }

    private fun handleBarcodeScan(barcode: String) {
        binding.tvResult.text = "Scanned: $barcode\nFetching ingredients..."

        // Fetch ingredients async, then evaluate and save
        BarcodeIngredientLookup().lookupOpenFoodFacts(barcode) { ingredients ->
            activity?.runOnUiThread {
                if (_binding == null) return@runOnUiThread // Fragment destroyed

                val activeAllergens = vm.getActiveAllergens().toSet()
                val checker = AllergenChecker().apply {
                    activeAllergens.forEach { addAllergen(it) }
                }

                val parsed = BarcodeIngredientLookup().parseIngredients(ingredients)
                val verdict = when (checker.foodSafe(parsed)) {
                    2 -> "⚠️ Contains your allergens!"
                    1 -> "⚠️ Unknown, proceed with caution"
                    else -> "✓ No allergens found"
                }

                // Save the scan
                vm.saveScan(
                    Product(
                        barcode = barcode,
                        name = "Unknown",
                        ingredients = if (ingredients.isBlank()) null else ingredients
                    )
                )

                binding.tvResult.text = "Scanned: $barcode\n$verdict"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.barcodeScanner.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.barcodeScanner.pause()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.barcodeScanner.pause()
        _binding = null
    }
}