package com.example.allerscan.ui.qrscan
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.view.PreviewView
import androidx.fragment.app.Fragment
import com.example.allerscan.databinding.FragmentBarcodeSearchBinding
import com.example.allerscan.databinding.FragmentQrScanBinding

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
        binding.buttonSaveProfile.setOnClickListener {
            val barcode = binding.inputBarcode.text.toString()
//            if (barcode.isNotBlank()) {
//                //try to make ingredient call
//            } else {
//
//            }
        }
    }

    //    val searchView = findViewById(R.id.barcodeSearch)
//    searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
//        fun onQueryTextSubmit(query: String): String {
//
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}