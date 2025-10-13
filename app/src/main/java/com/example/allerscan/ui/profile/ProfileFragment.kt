package com.example.allerscan.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.allerscan.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupClickListeners()
        setupObservers()

        return root
    }


    private fun setupClickListeners() {
        binding.buttonSaveProfile.setOnClickListener {
            val firstName = binding.inputFirstName.text.toString()
            val lastName = binding.inputLastName.text.toString()

            if (firstName.isNotBlank() && lastName.isNotBlank()) {
                profileViewModel.saveName(firstName, lastName)
            } else {
                Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonEditProfile.setOnClickListener {
            profileViewModel.editName()
        }

        binding.buttonDeleteProfile.setOnClickListener {
            profileViewModel.deleteData()
            Toast.makeText(requireContext(), "Profile data has been deleted", Toast.LENGTH_SHORT).show()
        }
    }

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
