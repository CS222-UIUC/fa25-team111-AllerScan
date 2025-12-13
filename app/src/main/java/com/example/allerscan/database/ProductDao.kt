// File: 'app/src/main/java/com/example/allerscan/database/ProductDao.kt'
package com.example.allerscan.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY dateScanned DESC")
    fun getAllProducts(): LiveData<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: Product)

    @Query("SELECT * FROM products WHERE barcode = :code")
    fun findProduct(code: String): Product?
}