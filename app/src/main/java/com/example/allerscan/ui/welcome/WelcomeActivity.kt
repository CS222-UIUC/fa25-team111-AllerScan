package com.example.allerscan.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.allerscan.MainActivity
import com.example.allerscan.R
import com.example.allerscan.utils.PreferencesManager

/**
 * Welcome activity with ViewPager2 slides
 */
class WelcomeActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var dotsLayout: LinearLayout
    private lateinit var skipButton: TextView
    private lateinit var nextButton: Button
    private lateinit var startButton: Button

    private lateinit var adapter: WelcomeAdapter
    private val slides = listOf(
        WelcomeSlide(
            R.drawable.ic_welcome_scan,
            "Scan Products",
            "Quickly scan barcodes to check for allergens in food products"
        ),
        WelcomeSlide(
            R.drawable.ic_welcome_barcode,
            "Instant Results",
            "Get immediate allergen information from our comprehensive database"
        ),
        WelcomeSlide(
            R.drawable.ic_welcome_profile,
            "Manage Your Profile",
            "Set up your allergen profile to get personalized warnings"
        ),
        WelcomeSlide(
            R.drawable.ic_welcome_safe,
            "Stay Safe",
            "Keep track of safe and unsafe products for your dietary needs"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            setContentView(R.layout.activity_welcome)
            initializeViews()
            setupViewPager()
            setupDots()
        } catch (e: Exception) {
            Log.e("WelcomeActivity", "Failed to initialize, going to main", e)
            navigateToMain()
        }
    }

    private fun initializeViews() {
        try {
            viewPager = findViewById(R.id.viewPager)
            dotsLayout = findViewById(R.id.dotsLayout)
            skipButton = findViewById(R.id.skipButton)
            nextButton = findViewById(R.id.nextButton)
            startButton = findViewById(R.id.startButton)

            skipButton.setOnClickListener {
                completeOnboarding()
            }

            nextButton.setOnClickListener {
                if (viewPager.currentItem < slides.size - 1) {
                    viewPager.currentItem += 1
                }
            }

            startButton.setOnClickListener {
                completeOnboarding()
            }

        } catch (e: Exception) {
            Log.e("WelcomeActivity", "Error setting up views", e)
            throw e
        }
    }

    private fun setupViewPager() {
        adapter = WelcomeAdapter(slides)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDots(position)
                updateButtons(position)
            }
        })
    }

    private fun setupDots() {
        val dots = Array(slides.size) { ImageView(this) }

        dotsLayout.removeAllViews()

        for (i in dots.indices) {
            dots[i] = ImageView(this)
            dots[i].setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground)
            )

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)

            dotsLayout.addView(dots[i], params)
        }

        updateDots(0)
    }

    private fun updateDots(position: Int) {
        for (i in 0 until dotsLayout.childCount) {
            val dot = dotsLayout.getChildAt(i) as ImageView
            if (i == position) {
                dot.setColorFilter(ContextCompat.getColor(this, R.color.dot_active))
                dot.alpha = 1.0f
            } else {
                dot.setColorFilter(ContextCompat.getColor(this, R.color.dot_inactive))
                dot.alpha = 0.5f
            }
        }
    }

    private fun updateButtons(position: Int) {
        if (position == slides.size - 1) {
            // Last slide
            nextButton.visibility = View.GONE
            skipButton.visibility = View.GONE
            startButton.visibility = View.VISIBLE
        } else {
            // Not last slide
            nextButton.visibility = View.VISIBLE
            skipButton.visibility = View.VISIBLE
            startButton.visibility = View.GONE
        }
    }

    private fun completeOnboarding() {
        try {
            val preferencesManager = PreferencesManager(this)
            preferencesManager.setFirstTimeLaunch(false)
            preferencesManager.setOnboardingCompleted(true)

            Log.d("WelcomeActivity", "Onboarding completed, navigating to main")
        } catch (e: Exception) {
            Log.e("WelcomeActivity", "Error saving preferences", e)
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
            finish()
        }
    }
}
