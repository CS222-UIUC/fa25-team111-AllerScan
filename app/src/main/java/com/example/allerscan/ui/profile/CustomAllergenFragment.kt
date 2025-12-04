package com.example.allerscan.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allerscan.databinding.FragmentCustomAllergenBinding

class CustomAllergenFragment : Fragment() {

    private var _binding: FragmentCustomAllergenBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by activityViewModels()

    private lateinit var customAllergenAdapter: CustomAllergenAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomAllergenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()

        profileViewModel.activeAllergens.observe(viewLifecycleOwner) { allAllergens ->
            val predefinedAllergens = setOf(
                "milk", "egg", "fish", "peanut", "wheat", "soy", "shellfish",
                "almond", "walnut", "pecan", "pistachio", "hazelnut", "sesame"
            )
            val customList = allAllergens.filter { !predefinedAllergens.contains(it) }.sorted()
            customAllergenAdapter.submitList(customList)
        }
    }

    private fun setupRecyclerView() {
        customAllergenAdapter = CustomAllergenAdapter { allergenToRemove ->
            profileViewModel.removeAllergen(allergenToRemove)
        }

        binding.recyclerViewCustomAllergens.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = customAllergenAdapter
        }
    }

    private fun setupClickListeners() {
        binding.buttonAdd.setOnClickListener {
            val newAllergen = binding.inputAddAllergen.text.toString().trim().lowercase()

            if (newAllergen.isNotBlank()) {
                profileViewModel.addAllergen(newAllergen)
                binding.inputAddAllergen.text.clear()
            } else {
                Toast.makeText(requireContext(), "Allergen name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
