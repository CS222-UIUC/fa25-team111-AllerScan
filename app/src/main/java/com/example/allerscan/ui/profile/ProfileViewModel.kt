package com.example.allerscan.ui.profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val PREFS_NAME = "user_profile_prefs"
    private val KEY_FULL_NAME = "USER_FULL_NAME"

    private val sharedPreferences = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _fullName = MutableLiveData<String?>()
    val fullName: LiveData<String?> = _fullName

    private val _isEditing = MutableLiveData<Boolean>()
    val isEditing: LiveData<Boolean> = _isEditing


    init {

        val savedName = sharedPreferences.getString(KEY_FULL_NAME, null)
        _fullName.value = savedName

        _isEditing.value = savedName.isNullOrEmpty()
    }


    fun saveName(firstName: String, lastName: String) {
        if (firstName.isBlank() || lastName.isBlank()) {
            return // Don't save if fields are empty
        }

        val nameToSave = "$firstName $lastName"
        _fullName.value = nameToSave
        _isEditing.value = false // Switch to display mode

        sharedPreferences.edit().putString(KEY_FULL_NAME, nameToSave).apply()
    }


    fun editName() {
        _isEditing.value = true
    }

    fun deleteData() {
        sharedPreferences.edit().clear().apply()

        editName()
    }

}
