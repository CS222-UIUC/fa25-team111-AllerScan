package com.example.allerscan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

@Deprecated("Replaced by QrScanFragment - do not use")
class QrScanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()
    }
}