//Actual Welcome activity implementation

package com.example.allerscan.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.allerscan.MainActivity
import com.example.allerscan.R
import com.example.allerscan.utils.PreferencesManager

class WelcomeActivity : AppCompatActivity() {
    
    private lateinit var viewPager: ViewPager2
    private lateinit var dotsLayout: LinearLayout
    private lateinit var skipButton: TextView
    private lateinit var nextButton: Button
    private lateinit var startButton: Button
    private lateinit var preferencesManager: PreferencesManager
    
    private val welcomeSlides = listOf(
        WelcomeSlide(
            R.drawable.ic_welcome_scan,
            "Welcome to AllerScan",
            "Your personal allergen detection assistant"
        ),
        WelcomeSlide(
            R.drawable.ic_welcome_profile,
            "Set Your Allergen Profile",
            "Tell us about your allergies and dietary restrictions for personalized alerts"
        ),
        WelcomeSlide(
            R.drawable.ic_welcome_barcode,
            "Scan Product Barcodes",
            "Simply scan any product barcode to instantly check for allergens"
        ),
        WelcomeSlide(
            R.drawable.ic_welcome_safe,
            "Stay Safe & Informed",
            "Get real-time alerts about ingredients that may affect you"
        )
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        
        preferencesManager = PreferencesManager(this)
        
        // Check if onboarding is already completed
        if (preferencesManager.isOnboardingCompleted()) {
            navigateToMain()
            return
        }
        
        initViews()
        setupViewPager()
        setupClickListeners()
    }
    
    private fun initViews() {
        viewPager = findViewById(R.id.viewPager)
        dotsLayout = findViewById(R.id.dotsLayout)
        skipButton = findViewById(R.id.skipButton)
        nextButton = findViewById(R.id.nextButton)
        startButton = findViewById(R.id.startButton)
    }
    
    private fun setupViewPager() {
        val adapter = WelcomeAdapter(welcomeSlides)
        viewPager.adapter = adapter
        
        setupDots(0)
        
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setupDots(position)
                updateButtons(position)
            }
        })
    }
    
    private fun setupDots(position: Int) {
        dotsLayout.removeAllViews()
        val dots = Array(welcomeSlides.size) { TextView(this) }
        
        dots.forEach { dot ->
            dot.text = "•"
            dot.textSize = 35f
            dot.setTextColor(ContextCompat.getColor(this, R.color.dot_inactive))
            dotsLayout.addView(dot)
        }
        
        if (dots.isNotEmpty()) {
            dots[position].setTextColor(ContextCompat.getColor(this, R.color.dot_active))
        }
    }
    
    private fun updateButtons(position: Int) {
        when (position) {
            welcomeSlides.size - 1 -> {
                skipButton.visibility = View.GONE
                nextButton.visibility = View.GONE
                startButton.visibility = View.VISIBLE
            }
            else -> {
                skipButton.visibility = View.VISIBLE
                nextButton.visibility = View.VISIBLE
                startButton.visibility = View.GONE
            }
        }
    }
    
    private fun setupClickListeners() {
        skipButton.setOnClickListener {
            completeOnboarding()
        }
        
        nextButton.setOnClickListener {
            val current = viewPager.currentItem
            if (current < welcomeSlides.size - 1) {
                viewPager.currentItem = current + 1
            }
        }
        
        startButton.setOnClickListener {
            completeOnboarding()
        }
    }
    
    private fun completeOnboarding() {
        preferencesManager.setFirstTimeLaunch(false)
        preferencesManager.setOnboardingCompleted(true)
        navigateToMain()
    }
    
    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}

data class WelcomeSlide(
    val imageResource: Int,
    val title: String,
    val description: String
)
