// File: 'app/src/main/java/com/example/allerscan/data/AppRepository.kt
package com.example.allerscan.data

import android.content.Context
import com.example.allerscan.database.*

class AppRepository(context: Context) {
    private val db = ProductDatabase.getDatabase(context)
    private val productDao = db.productDao()
    private val allergenDao = db.allergenDao()

    fun ensureDefaultAllergens() {
        if (allergenDao.getAll().isEmpty()) {
            listOf(
                "peanut","almond","walnut","pecan","pistachio","hazelnut","cashews", "pine nuts",
                "milk","egg","soy","fish","shellfish","sesame","wheat"
            ).forEach { allergenDao.upsert(Allergen(name = it, active = false)) }
        }
    }

    fun getActiveAllergens(): List<String> = allergenDao.getActive().map { it.name }

    fun setAllergenActive(name: String, active: Boolean) {
        allergenDao.upsert(Allergen(name = name, active = active))
    }

    fun insertProduct(product: Product) = productDao.insertProduct(product)

    fun getAllProducts() = productDao.getAllProducts()
    suspend fun deleteAllCustomAllergens() {
        allergenDao.deleteAllCustomAllergens()
    }

}
