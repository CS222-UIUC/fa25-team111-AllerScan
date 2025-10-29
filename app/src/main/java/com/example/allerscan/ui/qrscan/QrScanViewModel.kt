// File: 'app/src/main/java/com/example/allerscan/ui/qrscan/QrScanViewModel.kt'
package com.example.allerscan.ui.qrscan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.allerscan.data.AppRepository
import com.example.allerscan.database.Product

class QrScanViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = AppRepository(app)

    fun ensureDefaults() = repo.ensureDefaultAllergens()
    fun getActiveAllergens(): List<String> = repo.getActiveAllergens()
    fun saveScan(product: Product) = repo.insertProduct(product)
}