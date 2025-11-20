//Manages the display for the welcome screen
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
    
    inner class WelcomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.slideImage)
        private val titleTextView: TextView = itemView.findViewById(R.id.slideTitle)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.slideDescription)
        
        fun bind(slide: WelcomeSlide) {
            imageView.setImageResource(slide.imageResource)
            titleTextView.text = slide.title
            descriptionTextView.text = slide.description
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WelcomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_welcome_slide, parent, false)
        return WelcomeViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: WelcomeViewHolder, position: Int) {
        holder.bind(slides[position])
    }
    
    override fun getItemCount(): Int = slides.size
}
