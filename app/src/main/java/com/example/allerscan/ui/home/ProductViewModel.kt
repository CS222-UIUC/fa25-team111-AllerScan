package com.example.allerscan.ui.home

import android.app.Application
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
}