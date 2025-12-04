package com.example.allerscan.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allerscan.R

class CustomAllergenAdapter(
    private val onDeleteClick: (String) -> Unit
) : RecyclerView.Adapter<CustomAllergenAdapter.AllergenViewHolder>() {

    private var allergens: List<String> = emptyList()

    class AllergenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val allergenNameText: TextView = itemView.findViewById(R.id.text_custom_allergen_name)
        val deleteButton: ImageButton = itemView.findViewById(R.id.button_delete_custom_allergen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllergenViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_custom_allergen, parent, false)
        return AllergenViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllergenViewHolder, position: Int) {
        val allergen = allergens[position]
        holder.allergenNameText.text = allergen
        holder.deleteButton.setOnClickListener {
            onDeleteClick(allergen)
        }
    }

    override fun getItemCount(): Int {
        return allergens.size
    }

    fun submitList(newAllergens: List<String>) {
        allergens = newAllergens
        notifyDataSetChanged()
    }
}
