package com.example.allerscan.ui.qrscan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.view.PreviewView
import androidx.fragment.app.Fragment
import com.example.allerscan.databinding.FragmentBarcodeSearchBinding
import com.example.allerscan.databinding.FragmentQrScanBinding
import android.widget.Toast
import com.example.allerscan.AllergenChecker
import com.example.allerscan.BarcodeIngredientLookup
import android.util.Log


class barcodeSearchFragment : Fragment() {
    private var _binding: FragmentBarcodeSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBarcodeSearchBinding.inflate(inflater, container, false)

        setupClickListeners()

        return binding.root
    }

    private fun setupClickListeners() {
        binding.buttonBarcode.setOnClickListener {
            val barcode = binding.inputBarcode.text.toString()
            if (barcode.isNotBlank() && barcode.length == 12) {
                //try to make ingredient call
                val productIngredients = BarcodeIngredientLookup()
                val allergenCheck = AllergenChecker()
                productIngredients.lookupOpenFoodFacts(barcode) { ingredients ->
                    if (ingredients.isEmpty()) {
                        Log.e("barcodeSearchFragment", "Ingredients Not found")
                    } else {
                        Log.d("barcodeSearchFragment", "Ingredients Found: $ingredients")
                        val ingredientList = productIngredients.parseIngredients(ingredients)
                        val safe = allergenCheck.foodSafe(ingredientList)
                        Toast.makeText(requireContext(), "Barcode Found", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please Enter a Valid Barcode (Must be 12 digits)", Toast.LENGTH_SHORT).show()
            }
            binding.inputBarcode.getText().clear()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}