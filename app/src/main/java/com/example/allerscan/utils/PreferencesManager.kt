//Handles onboarding and first-time launch

package com.example.allerscan.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences("AllerScanPrefs", Context.MODE_PRIVATE)
    
    companion object {
        private const val KEY_FIRST_TIME_LAUNCH = "first_time_launch"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }
    
    fun isFirstTimeLaunch(): Boolean {
        return sharedPreferences.getBoolean(KEY_FIRST_TIME_LAUNCH, true)
    }
    
    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_FIRST_TIME_LAUNCH, isFirstTime).apply()
    }
    
    fun isOnboardingCompleted(): Boolean {
        return sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }
    
    fun setOnboardingCompleted(completed: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
    }
}
