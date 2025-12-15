package com.example.allerscan.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.allerscan.databinding.FragmentHomeBinding
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.fragment.app.activityViewModels

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val productViewModel: ProductViewModel by activityViewModels()
    private lateinit var customProductAdapter: ProductRecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    //Product History List Recycler View
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Update Recycler View with real user data
        customProductAdapter = ProductRecyclerView()
        binding.productRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.productRecyclerView.adapter = customProductAdapter
        productSearchListener()
        productViewModel.allProducts.observe(viewLifecycleOwner) { products ->
            customProductAdapter.addProductList(products)
            //If no products scanned, show add product text
            if (products.isNullOrEmpty()) {
                //Show text
                binding.textHome.visibility = View.VISIBLE
                binding.productRecyclerView.visibility = View.GONE
            } else {
                //Show recycler list of products
                binding.textHome.visibility = View.GONE
                binding.productRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    //Product Search Bar
    private fun productSearchListener() {
        binding.searchBarcodeHistory.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val barcode = query.orEmpty().trim()
                if (barcode.length < 10 || barcode.length > 14) {
                    Toast.makeText(requireContext(), "Enter valid barcode (must be 12 digits).", Toast.LENGTH_LONG).show()
                    return false
                }
                val found = productViewModel.search(barcode)
                if (!found) {
                    Toast.makeText(requireContext(), "Barcode was not previously scanned. Scan barcode to view product.", Toast.LENGTH_LONG).show()
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}