package com.example.allerscan.ui.welcome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allerscan.R

class WelcomeAdapter(private val slides: List<WelcomeSlide>) :
    RecyclerView.Adapter<WelcomeAdapter.WelcomeViewHolder>() {

    inner class WelcomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.slideImage)
        val title: TextView = view.findViewById(R.id.slideTitle)
        val description: TextView = view.findViewById(R.id.slideDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WelcomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_welcome_slide, parent, false)
        return WelcomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: WelcomeViewHolder, position: Int) {
        val slide = slides[position]
        holder.image.setImageResource(slide.imageRes)
        holder.title.text = slide.title
        holder.description.text = slide.description
    }

    override fun getItemCount(): Int = slides.size
}
