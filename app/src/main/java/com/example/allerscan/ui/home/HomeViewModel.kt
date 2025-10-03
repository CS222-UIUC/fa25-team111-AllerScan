package com.example.allerscan.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "You haven't scanned any items yet! \n Scan an item to get started."
    }
    val text: LiveData<String> = _text
}