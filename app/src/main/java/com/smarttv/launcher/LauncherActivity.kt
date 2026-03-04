package com.smarttv.launcher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smarttv.launcher.adapters.AppAdapter
import com.smarttv.launcher.adapters.FeaturedAdapter
import com.smarttv.launcher.databinding.ActivityLauncherBinding
import com.smarttv.launcher.models.AppItem
import com.smarttv.launcher.utils.AppUtils
import com.smarttv.launcher.utils.TimeUtils
import java.util.*

class LauncherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLauncherBinding
    private lateinit var appAdapter: AppAdapter
    private lateinit var featuredAdapter: FeaturedAdapter

    private val timeHandler = Handler(Looper.getMainLooper())
    private val timeRunnable = object : Runnable {
        override fun run() {
            updateDateTime()
            timeHandler.postDelayed(this, 1000)
        }
    }

    private val packageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            loadApps()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI()
        setupUI()
        setupRecyclerViews()
        loadApps()
        startClock()
        registerPackageReceiver()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        )
    }

    private fun setupUI() {
        // Settings button
        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Search button focus behavior
        binding.btnSearch.setOnClickListener {
            // Launch search activity or show search overlay
            showSearchOverlay()
        }

        // Weather widget click
        binding.weatherWidget.setOnClickListener {
            // Could open weather app
        }
    }

    private fun setupRecyclerViews() {
        // Featured apps row (horizontal)
        featuredAdapter = FeaturedAdapter { app ->
            launchApp(app)
        }
        binding.rvFeatured.apply {
            layoutManager = LinearLayoutManager(
                this@LauncherActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = featuredAdapter
        }

        // All apps grid
        appAdapter = AppAdapter { app ->
            launchApp(app)
        }
        binding.rvApps.apply {
            layoutManager = GridLayoutManager(this@LauncherActivity, 6)
            adapter = appAdapter
        }
    }

    private fun loadApps() {
        val allApps = AppUtils.getInstalledApps(this)
        val featured = allApps.take(5)
        val rest = allApps

        featuredAdapter.submitList(featured)
        appAdapter.submitList(rest)

        // Update category tabs
        updateCategoryTabs(allApps)
    }

    private fun updateCategoryTabs(apps: List<AppItem>) {
        binding.tabAll.isSelected = true
    }

    private fun launchApp(app: AppItem) {
        try {
            val intent = packageManager.getLaunchIntentForPackage(app.packageName)
            intent?.let {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showSearchOverlay() {
        // Show a search dialog/overlay
        val searchIntent = Intent(Intent.ACTION_SEARCH_LONG_PRESS)
        searchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(searchIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startClock() {
        timeHandler.post(timeRunnable)
    }

    private fun updateDateTime() {
        val calendar = Calendar.getInstance()
        binding.tvTime.text = TimeUtils.formatTime(calendar)
        binding.tvDate.text = TimeUtils.formatDate(calendar)
    }

    private fun registerPackageReceiver() {
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_REPLACED)
            addDataScheme("package")
        }
        registerReceiver(packageReceiver, filter)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_BACK,
            KeyEvent.KEYCODE_HOME -> true // Prevent leaving launcher
            else -> super.onKeyDown(keyCode, event)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    override fun onResume() {
        super.onResume()
        loadApps()
        startClock()
    }

    override fun onPause() {
        super.onPause()
        timeHandler.removeCallbacks(timeRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        timeHandler.removeCallbacks(timeRunnable)
        try {
            unregisterReceiver(packageReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
