package com.example.allerscan.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.allerscan.databinding.FragmentHomeBinding
import com.example.allerscan.R

class HomeFragment : Fragment() {

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

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    //use recycler view with list adapter to scroll through barcode history
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //reference: https://www.geeksforgeeks.org/kotlin/searchview-in-android-with-kotlin/
        var searchBarcodeHistory = view.findViewById<SearchView>(R.id.searchBarcodeHistory)
        searchBarcodeHistory.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val barcodeList = mutableListOf<String>()
                if (barcodeList.contains(query)) {
                    //if barcode exists then show it
                } else {
                    //if barcode does not exist, print text does not exist/redirect to camera
                }
                return false;
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false;
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}