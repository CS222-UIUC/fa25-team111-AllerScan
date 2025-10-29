package com.example.allerscan.ui.qrscan

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.allerscan.databinding.FragmentQrScanBinding
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.example.allerscan.BarcodeIngredientLookup
import com.example.allerscan.database.Product
import java.util.concurrent.Executors
import com.example.allerscan.AllergenChecker

class QrScanFragment : Fragment() {
    private var _binding: FragmentQrScanBinding? = null
    private val binding get() = _binding!!
    private var cameraProvider: ProcessCameraProvider? = null
    private var camera: Camera? = null
    private var isScanning = true
    private var lastScannedCode: String? = null
    private var lastScanTime = 0L
    private val vm by viewModels<QrScanViewModel>()
    private lateinit var barcodeScanner: BarcodeScanner
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    companion object {
        private const val CAMERA_PERMISSION_CODE = 101
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQrScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.ensureDefaults()
        setupBarcodeScanner()
        setupUI()

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        } else {
            startCamera()
        }
    }

    private fun setupBarcodeScanner() {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_EAN_8,
                Barcode.FORMAT_UPC_A,
                Barcode.FORMAT_UPC_E,
                Barcode.FORMAT_CODE_128,
                Barcode.FORMAT_CODE_39
            )
            .build()
        barcodeScanner = BarcodeScanning.getClient(options)
    }

    private fun setupUI() {
        binding.btnToggleScan.setOnClickListener {
            isScanning = !isScanning
            if (isScanning) {
                binding.btnToggleScan.text = "Pause Scan"
                binding.tvResult.text = "Point camera at barcode"
                startCamera()
            } else {
                binding.btnToggleScan.text = "Resume Scan"
                binding.tvResult.text = "Scanning paused"
                cameraProvider?.unbindAll()
            }
        }

        binding.switchFlash.setOnCheckedChangeListener { _, isChecked ->
            camera?.cameraControl?.enableTorch(isChecked)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(context, "Camera permission is required for scanning.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            // Preview use case
            val preview = Preview.Builder()
                .setTargetRotation(binding.previewView.display.rotation)
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            // Image analysis use case
            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetRotation(binding.previewView.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        processImage(imageProxy)
                    }
                }

            // Camera selector
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            try {
                cameraProvider?.unbindAll()
                camera = cameraProvider?.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )

                // Set initial torch state
                camera?.cameraControl?.enableTorch(binding.switchFlash.isChecked)

            } catch (e: Exception) {
                android.util.Log.e("QrScanFragment", "Camera binding failed", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun processImage(imageProxy: ImageProxy) {
        if (!isScanning) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            barcodeScanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        barcode.rawValue?.let { code ->
                            // Debounce: prevent duplicate scans within 3 seconds
                            val now = System.currentTimeMillis()
                            if (code != lastScannedCode || (now - lastScanTime) > 3000) {
                                lastScannedCode = code
                                lastScanTime = now
                                handleBarcodeScan(code)
                            }
                        }
                    }
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    private fun handleBarcodeScan(barcode: String) {
        activity?.runOnUiThread {
            binding.tvResult.text = "Scanned: $barcode\nFetching ingredients..."

            BarcodeIngredientLookup().lookupOpenFoodFacts(barcode) { ingredients ->
                activity?.runOnUiThread {
                    if (_binding == null) return@runOnUiThread

                    val activeAllergens = vm.getActiveAllergens().toSet()
                    val checker = AllergenChecker()
                    activeAllergens.forEach { checker.addAllergen(it) }

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        _binding = null
    }
}
