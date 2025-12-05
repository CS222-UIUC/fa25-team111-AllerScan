package com.example.allerscan.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.allerscan.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var allergenViewMap: Map<CheckBox, String>
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        allergenViewMap = mapOf(
            binding.allergenBox1 to "milk",
            binding.allergenBox2 to "egg",
            binding.allergenBox3 to "wheat",
            binding.allergenBox4 to "soy",
            binding.allergenBox5 to "shellfish",
            binding.allergenBox6 to "fish",
            binding.allergenBox7 to "peanut",
            binding.allergenBox8 to "almond",
            binding.allergenBox9 to "walnut",
            binding.allergenBox10 to "pecan",
            binding.allergenBox11 to "pistachio",
            binding.allergenBox12 to "hazelnut",
            binding.allergenBox15 to "sesame"
        )

        setupClickListeners()
        setupObservers()

        return root
    }

    private fun setupClickListeners() {
        binding.buttonSaveProfile.setOnClickListener {
            val firstName = binding.inputFirstName.text.toString().trim()
            val lastName = binding.inputLastName.text.toString().trim()

            if (firstName.isNotBlank() && lastName.isNotBlank()) {
                profileViewModel.saveName(firstName, lastName)

                val allergenStates = getChecklistAllergenStates()
                profileViewModel.updateAllergensFromUI(allergenStates)
                Toast.makeText(requireContext(), "Profile Saved!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.buttonEditProfile.setOnClickListener {
            profileViewModel.editName()
        }

        binding.buttonDeleteProfile.setOnClickListener {
            profileViewModel.deleteData()
            uncheckAllAllergenBoxes()
            Toast.makeText(requireContext(), "Profile data has been deleted", Toast.LENGTH_SHORT)
                .show()
        }
        binding.buttonAddCustomAllergen.setOnClickListener {
            val firstName = binding.inputFirstName.text.toString().trim()
            val lastName = binding.inputLastName.text.toString().trim()
            val allergenStates = getChecklistAllergenStates()

            if (firstName.isNotBlank() && lastName.isNotBlank()) {
                profileViewModel.saveProgress(firstName, lastName)
                profileViewModel.updateAllergensFromUI(allergenStates)
            }

            findNavController().navigate(com.example.allerscan.R.id.action_profile_to_custom_allergen)
        }
    }

    private fun getChecklistAllergenStates(): Map<String, Boolean> {
        val states = mutableMapOf<String, Boolean>()
        allergenViewMap.forEach { (checkBox, allergenName) ->
            states[allergenName] = checkBox.isChecked
        }
        return states
    }

    private fun updateCheckboxes(activeAllergens: List<String>) {
        val activeSet = activeAllergens.toSet()
        allergenViewMap.forEach { (checkBox, allergenName) ->
            checkBox.isChecked = activeSet.contains(allergenName)
        }
    }

    private fun uncheckAllAllergenBoxes() {
        allergenViewMap.keys.forEach { checkBox ->
            checkBox.isChecked = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupObservers() {
        profileViewModel.fullName.observe(viewLifecycleOwner) { fullName ->
            if (!fullName.isNullOrEmpty()) {
                binding.textWelcomeMessage.text = "Welcome, $fullName"

                val names = fullName.split(" ", limit = 2)
                val firstName = names.getOrNull(0) ?: ""
                val lastName = names.getOrNull(1) ?: ""

                binding.inputFirstName.setText(firstName)
                binding.inputLastName.setText(lastName)
            } else {
                binding.textWelcomeMessage.text = "Welcome"
                binding.inputFirstName.setText("")
                binding.inputLastName.setText("")
            }
        }

        profileViewModel.isEditing.observe(viewLifecycleOwner) { isEditing ->
            binding.groupEditMode.isVisible = isEditing
            binding.groupDisplayMode.isVisible = !isEditing
        }

        profileViewModel.activeAllergens.observe(viewLifecycleOwner) { activeAllergens ->
            updateCheckboxes(activeAllergens)

            if (activeAllergens.isNullOrEmpty()) {
                binding.textAllergenList.text = "None selected."
            } else {
                val formattedList = activeAllergens.joinToString(separator = "\n") { allergen ->
                    "• ${allergen.replaceFirstChar { it.uppercase() }}"
                }
                binding.textAllergenList.text = formattedList
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
