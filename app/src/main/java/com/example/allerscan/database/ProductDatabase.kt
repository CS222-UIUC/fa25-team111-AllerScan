// Put this at: app/src/main/java/com/example/allerscan/database/ProductDatabase.kt
package com.example.allerscan.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Product::class, Allergen::class], version = 2, exportSchema = false)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun allergenDao(): AllergenDao

    // This is a simple singleton pattern
    companion object {
        @Volatile private var instance: ProductDatabase? = null

        fun getDatabase(context: Context): ProductDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ProductDatabase::class.java,
                    "product_database"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build().also { instance = it }
            }
        }
    }
}