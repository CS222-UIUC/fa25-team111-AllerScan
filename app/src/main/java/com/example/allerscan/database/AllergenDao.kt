package com.example.allerscan.database

import androidx.room.*

@Dao
interface AllergenDao {
    @Query("SELECT * FROM allergens")
    fun getAll(): List<Allergen>

    @Query("SELECT * FROM allergens WHERE active = 1")
    fun getActive(): List<Allergen>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(allergen: Allergen)

    @Update
    fun update(allergen: Allergen)

    @Query("DELETE FROM allergens")
    fun clear()
}
