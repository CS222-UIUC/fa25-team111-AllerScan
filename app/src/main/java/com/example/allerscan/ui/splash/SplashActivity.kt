//Activity that prompts the user on the welcome screen
package com.example.allerscan.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.allerscan.MainActivity
import com.example.allerscan.R
import com.example.allerscan.ui.welcome.WelcomeActivity
import com.example.allerscan.utils.PreferencesManager

class SplashActivity : AppCompatActivity() {
    
    private lateinit var preferencesManager: PreferencesManager
    private val SPLASH_DELAY = 1500L // 1.5 seconds
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        preferencesManager = PreferencesManager(this)
        
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, SPLASH_DELAY)
    }
    
    private fun navigateToNextScreen() {
        val nextActivity = if (preferencesManager.isFirstTimeLaunch()) {
            WelcomeActivity::class.java
        } else {
            MainActivity::class.java
        }
        
        val intent = Intent(this, nextActivity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}
