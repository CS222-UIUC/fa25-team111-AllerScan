package com.example.allerscan

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.allerscan.databinding.ActivityMainBinding
import com.example.allerscan.R
import android.util.Log
import com.example.allerscan.BarcodeIngredientLookup
import com.example.allerscan.AllergenChecker

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "STARTED!!!")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ingredient fetch test with oreo barcode "044000047009"
        val test = BarcodeIngredientLookup()
        test.lookupOpenFoodFacts("044000047009") { ingredients ->
            if (ingredients.isNotEmpty()) {
                Log.d("MainActivity", "Test Oreo Ingredients: $ingredients")
            } else {
                Log.e("MainActivity", "Test Oreo Ingredients not found/request failed")
            }
        }
        //ingredient fetch test with peanut butter barcode "051500241776"
        test.lookupOpenFoodFacts("051500241776") { ingredients ->
            if (ingredients.isNotEmpty()) {
                Log.d("MainActivity", "Test Peanut Butter Ingredients: $ingredients")
            } else {
                Log.e("MainActivity", "Test Peanut Butter Ingredients found/request failed")
            }
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_qr, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home)
                    true
                }
                R.id.navigation_qr -> {
                    navController.navigate(R.id.navigation_qr)
                    true
                }
                R.id.navigation_profile -> {
                    navController.navigate(R.id.navigation_profile)
                    true
                }
                else -> false
            }
        }
    }
}
