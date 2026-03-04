package com.smarttv.launcher

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.smarttv.launcher.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI()
        setupUI()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
    }

    private fun setupUI() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnNetwork.setOnFocusChangeListener { view, hasFocus ->
            view.isSelected = hasFocus
        }

        binding.btnDisplay.setOnFocusChangeListener { view, hasFocus ->
            view.isSelected = hasFocus
        }

        binding.btnSound.setOnFocusChangeListener { view, hasFocus ->
            view.isSelected = hasFocus
        }

        binding.btnApps.setOnFocusChangeListener { view, hasFocus ->
            view.isSelected = hasFocus
        }

        binding.btnSystem.setOnFocusChangeListener { view, hasFocus ->
            view.isSelected = hasFocus
        }
    }
}
