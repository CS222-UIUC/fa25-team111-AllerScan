package com.example.allerscan.ui.qrscan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.allerscan.databinding.FragmentBarcodeSearchBinding
import android.widget.Toast
import com.example.allerscan.AllergenChecker
import com.example.allerscan.BarcodeIngredientLookup
import android.util.Log
import androidx.fragment.app.viewModels
import com.example.allerscan.database.Product
import kotlin.getValue


class barcodeSearchFragment : Fragment() {
    private var _binding: FragmentBarcodeSearchBinding? = null
    private val binding get() = _binding!!

    private val vm by viewModels<QrScanViewModel>()

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
                productIngredients.lookupOpenFoodFacts(barcode) { productName, ingredients ->
                    if (ingredients.isEmpty()) {
                        Log.e("barcodeSearchFragment", "Ingredients Not found")
                    } else {
                        Log.d("barcodeSearchFragment", "Ingredients Found: $ingredients")
                        val ingredientList = productIngredients.parseIngredients(ingredients)
                        val safe = allergenCheck.foodSafe(ingredientList)
                        //Toast.makeText(requireContext(), "Barcode Found", Toast.LENGTH_SHORT).show()

                        val activeAllergens = vm.getActiveAllergens().toSet()
                        val checker = AllergenChecker().apply {
                            activeAllergens.forEach { addAllergen(it) }
                        }

                        val verdict = when (checker.foodSafe(ingredientList)) {
                            2 -> "⚠️ Contains your allergens!"
                            1 -> "⚠️ Unknown, proceed with caution"
                            else -> "✓ No allergens found"
                        }

                        // Save the scan
                        vm.saveScan(
                            Product(
                                barcode = barcode,
                                name = productName,
                                safety = verdict,
                                ingredients = if (ingredients.isBlank()) null else ingredients
                            )
                        )
                        binding.inputBarcode.getText().clear()
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