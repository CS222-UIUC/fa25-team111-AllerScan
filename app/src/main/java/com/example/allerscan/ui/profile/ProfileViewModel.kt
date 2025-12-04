package com.example.allerscan.ui.profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import com.example.allerscan.data.AppRepository
import kotlinx.coroutines.launch


class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = AppRepository(application)

    private val PREFS_NAME = "user_profile_prefs"
    private val KEY_FULL_NAME = "USER_FULL_NAME"

    private val sharedPreferences = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _fullName = MutableLiveData<String?>()
    val fullName: LiveData<String?> = _fullName

    private val _isEditing = MutableLiveData<Boolean>()
    val isEditing: LiveData<Boolean> = _isEditing

    private val _activeAllergens = MutableLiveData<List<String>>()
    val activeAllergens: LiveData<List<String>> = _activeAllergens



    init {

        val savedName = sharedPreferences.getString(KEY_FULL_NAME, null)
        _fullName.value = savedName
        _isEditing.value = savedName.isNullOrEmpty()
        loadActiveAllergens()
    }

    fun addAllergen(allergenName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.setAllergenActive(allergenName, true)
            loadActiveAllergens()
        }
    }

    fun removeAllergen(allergenName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.setAllergenActive(allergenName, false)
            loadActiveAllergens()
        }
    }

    fun saveProgress(firstName: String, lastName: String) {
        sharedPreferences.edit().putString("full_name", "$firstName $lastName").apply()
        _fullName.value = "$firstName $lastName"
    }
    fun saveName(firstName: String, lastName: String) {
        if (firstName.isBlank() || lastName.isBlank()) {
            return
        }

        val nameToSave = "$firstName $lastName"
        _fullName.value = nameToSave
        _isEditing.value = false

        sharedPreferences.edit().putString(KEY_FULL_NAME, nameToSave).apply()
    }
    fun updateAllergensFromUI(allergenStates: Map<String, Boolean>){
        viewModelScope.launch(Dispatchers.IO){
            allergenStates.forEach { (allergenName, isActive) ->
                repo.setAllergenActive(allergenName, isActive)
            }
            loadActiveAllergens()
        }

    }

    fun loadActiveAllergens(){
        viewModelScope.launch(Dispatchers.IO){
            val activeAllergensList = repo.getActiveAllergens()
            _activeAllergens.postValue(activeAllergensList)
        }
    }


    fun editName() {
        _isEditing.value = true
    }

    fun deleteData() {
        sharedPreferences.edit().clear().apply()
        _fullName.value = null
        _activeAllergens.value = emptyList()
        editName()

        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAllCustomAllergens()
            val checklistAllergens = listOf(
                "milk", "egg", "wheat", "soy", "shellfish", "fish", "peanut",
                "almond", "walnut", "pecan", "pistachio", "hazelnut", "sesame"
            )
            val inactiveStates = checklistAllergens.associateWith { false }
            updateAllergensFromUI(inactiveStates)
        }
    }
}
