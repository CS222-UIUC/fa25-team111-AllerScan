package com.example.allerscan.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages app preferences including first-time launch tracking
 * Includes error handling to prevent crashes
 */
class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences = try {
        context.getSharedPreferences("AllerScanPrefs", Context.MODE_PRIVATE)
    } catch (e: Exception) {
        //Fallback to default preferences if there's an issue
        context.getSharedPreferences("AllerScanPrefs_backup", Context.MODE_PRIVATE)
    }

    companion object {
        private const val KEY_FIRST_TIME_LAUNCH = "first_time_launch"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }

    fun isFirstTimeLaunch(): Boolean {
        return try {
            sharedPreferences.getBoolean(KEY_FIRST_TIME_LAUNCH, true)
        } catch (e: Exception) {
            //On any error, assume not first time to avoid crash
            false
        }
    }

    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        try {
            sharedPreferences.edit().putBoolean(KEY_FIRST_TIME_LAUNCH, isFirstTime).apply()
        } catch (e: Exception) {
            //Silently fail rather than crash
            e.printStackTrace()
        }
    }

    fun isOnboardingCompleted(): Boolean {
        return try {
            sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETED, false)
        } catch (e: Exception) {
            //On error, assume completed to avoid crash
            true
        }
    }

    fun setOnboardingCompleted(completed: Boolean) {
        try {
            sharedPreferences.edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
        } catch (e: Exception) {
            //Silently fail rather than crash
            e.printStackTrace()
        }
    }
}
