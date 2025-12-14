package com.example.allerscan.ui.home

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.allerscan.database.Product
import com.example.allerscan.database.ProductDatabase
import com.example.allerscan.data.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = ProductDatabase.getDatabase(application).productDao()
    val allProducts: LiveData<List<Product>> = dao.getAllProducts()
    fun search(barcode: String): Boolean {
        //If product searched and found, updates the timestamp so the product appears on top of the list
        if (barcode.isEmpty()) return false
        if (barcode.isBlank()) return false
        if (barcode.length != 12) return false
        var updated = false
        viewModelScope.launch {
            val found = dao.findProduct(barcode)
            if (found != null) {
                found.dateScanned = System.currentTimeMillis().toString()
                dao.updateProduct(found)
                updated = true
            }
        }
        return updated
    }
}