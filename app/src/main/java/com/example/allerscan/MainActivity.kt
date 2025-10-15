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
import com.example.allerscan.QrScanActivity
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ingredient fetch test with oreo barcode
        val lookupTest = BarcodeIngredientLookup()
        val barcode = "044000047009"
        Thread {
            val ingredients = lookupTest.lookupOpenFoodFacts(barcode)
            Log.d("MainActivity", "Test for BarcodeIngredientLookup: $ingredients")
        }.start()
        //ingredient fetch test with peanut barcode
//        val lookupTest2 = BarcodeIngredientLookup()
//        val barcode2 = "051500241776"
//        Thread {
//            val ingredients2 = lookupTest.lookupOpenFoodFacts(barcode)
//            Log.d("MainActivity", "Test for BarcodeIngredientLookup2: $ingredients2")
//        }.start()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
