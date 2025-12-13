package com.example.allerscan.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.allerscan.MainActivity
import com.example.allerscan.R
import com.example.allerscan.utils.PreferencesManager

/**
 * Simple welcome activity without ViewPager2
 * Minimal dependencies to avoid crashes
 */
class WelcomeActivity : AppCompatActivity() {
    
    private lateinit var getStartedButton: Button
    private lateinit var skipTextView: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            setContentView(R.layout.activity_welcome)
            initializeViews()
        } catch (e: Exception) {
            Log.e("WelcomeActivity", "Failed to initialize, going to main", e)
            // If anything fails, go straight to main
            navigateToMain()
        }
    }
    
    private fun initializeViews() {
        try {
            // Find the button - use correct ID from layout
            getStartedButton = findViewById(R.id.btnGetStarted)
            
            // Set click listener
            getStartedButton.setOnClickListener {
                completeOnboarding()
            }
            
            // Find skip text - use correct ID from layout
            skipTextView = findViewById(R.id.tvSkip)
            skipTextView.setOnClickListener {
                completeOnboarding()
            }
            
        } catch (e: Exception) {
            Log.e("WelcomeActivity", "Error setting up views", e)
            // Add a delay then go to main anyway
            window.decorView.postDelayed({
                navigateToMain()
            }, 2000)
        }
    }
    
    private fun completeOnboarding() {
        try {
            // Mark onboarding as complete
            val preferencesManager = PreferencesManager(this)
            preferencesManager.setFirstTimeLaunch(false)
            preferencesManager.setOnboardingCompleted(true)
            
            Log.d("WelcomeActivity", "Onboarding completed, navigating to main")
        } catch (e: Exception) {
            Log.e("WelcomeActivity", "Error saving preferences", e)
            // Continue anyway
        }
        
        navigateToMain()
    }
    
    private fun navigateToMain() {
        try {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Log.e("WelcomeActivity", "Critical error - could not start MainActivity", e)
            // Last resort - just finish this activity
            finish()
        }
    }
    
    override fun onBackPressed() {
        // Don't allow back press on welcome screen
        // User must complete or skip
        // This prevents users from getting stuck
        super.onBackPressed()
        completeOnboarding()
    }
}
