package com.example.allerscan.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.allerscan.MainActivity
import com.example.allerscan.R
import com.example.allerscan.ui.welcome.WelcomeActivity
import com.example.allerscan.utils.PreferencesManager

/**
 * Splash screen with robust error handling
 * Falls back to MainActivity if anything goes wrong
 */
class SplashActivity : AppCompatActivity() {

    private val SPLASH_DELAY = 1500L // 1.5 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            // Try to set the layout
            setContentView(R.layout.activity_splash)
        } catch (e: Exception) {
            Log.e("SplashActivity", "Failed to set content view, going to main", e)
            // If layout fails, go straight to main
            goToMainActivity()
            return
        }

        // Delay and then navigate
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, SPLASH_DELAY)
    }

    private fun navigateToNextScreen() {
        try {
            val preferencesManager = PreferencesManager(this)

            // Check if this is first time launch
            if (preferencesManager.isFirstTimeLaunch()) {
                Log.d("SplashActivity", "First time launch - going to welcome")
                goToWelcomeActivity()
            } else {
                Log.d("SplashActivity", "Not first time - going to main")
                goToMainActivity()
            }
        } catch (e: Exception) {
            Log.e("SplashActivity", "Error checking preferences, going to main", e)
            // On any error, just go to main activity
            goToMainActivity()
        }
    }

    private fun goToWelcomeActivity() {
        try {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Log.e("SplashActivity", "Failed to start WelcomeActivity, going to main", e)
            goToMainActivity()
        }
    }

    private fun goToMainActivity() {
        try {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("SplashActivity", "Critical error - could not start MainActivity", e)
        } finally {
            finish()
        }
    }
}
