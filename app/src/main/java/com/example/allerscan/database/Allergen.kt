package com.example.allerscan.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "allergens")
data class Allergen(
    @PrimaryKey val name: String,
    val active: Boolean = false
)