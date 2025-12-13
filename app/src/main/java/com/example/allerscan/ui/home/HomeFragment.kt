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
import androidx.fragment.app.viewModels
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}