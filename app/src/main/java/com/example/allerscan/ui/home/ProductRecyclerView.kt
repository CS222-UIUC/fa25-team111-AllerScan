package com.example.allerscan.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allerscan.R
import com.example.allerscan.database.Product

//Used as Reference for RecyclerView Implementation: https://www.geeksforgeeks.org/kotlin/how-to-add-dividers-in-android-recyclerview/

class ProductRecyclerView : RecyclerView.Adapter<ProductRecyclerView.ProductDataHolder>() {

    private var products = emptyList<Product>()

    class ProductDataHolder(productView: View) : RecyclerView.ViewHolder(productView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDataHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.history_list_product_view, parent, false)
        return ProductDataHolder(view)
    }

    override fun onBindViewHolder(holder: ProductDataHolder, position: Int) {
        val currentProduct: Product = products[position]
        val product_name_text: TextView = holder.itemView.findViewById(R.id.product_name_text)
        product_name_text.text = currentProduct.name
        val barcode_text: TextView = holder.itemView.findViewById(R.id.barcode_text)
        barcode_text.text = currentProduct.barcode
        val safety_text: TextView = holder.itemView.findViewById(R.id.safety_text)
        safety_text.text = currentProduct.safety
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onViewRecycled(holder: ProductDataHolder) {
        super.onViewRecycled(holder)
    }

    fun submitList(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}