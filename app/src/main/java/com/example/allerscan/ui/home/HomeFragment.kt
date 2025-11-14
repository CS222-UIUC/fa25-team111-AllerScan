package com.example.allerscan.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.allerscan.databinding.FragmentHomeBinding
import com.example.allerscan.R
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager

class HomeFragment : Fragment() {

    //MutableMap for Barcode to ProductData
    var productHistory: MutableMap<Int, ProductData> = mutableMapOf()
    //Update the productHistoryMap
    fun addBarcodeHistory(barcode: Int) {
        productHistory[barcode] = ProductData("Product Name", barcode.toString(), "Safe to Consume!")
    }


    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }


    //Product History List Recycler View
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val productHistoryList = productHistory.values.toList()
        //Temporary List of 30 Products to Test Product Display UI
        var productHistoryList = mutableListOf<ProductData>()
        for (i in 1..30) {
            productHistoryList.add(ProductData("Product Name", "111111111111", "Safe to Consume!"))
        }
        binding.productRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.productRecyclerView.adapter = ProductRecyclerView(productHistoryList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}