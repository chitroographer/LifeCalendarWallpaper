package com.example.lifecalendarwallpaper

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import java.time.LocalDate
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val today = LocalDate.now()
        val totalDays = if (today.isLeapYear) 366 else 365
        val dayOfYear = today.dayOfYear
        val daysLeft = totalDays - dayOfYear
        val percentage = (dayOfYear.toFloat() / totalDays * 100).toInt()

        findViewById<TextView>(R.id.tvBadge).text = "$percentage% OF ${today.year} COMPLETE"
        findViewById<TextView>(R.id.tvDaysElapsed).text = dayOfYear.toString()
        findViewById<TextView>(R.id.tvPercentage).text = "$percentage%"
        findViewById<TextView>(R.id.tvDaysLeft).text = daysLeft.toString()
        findViewById<ProgressBar>(R.id.progressBar).progress = percentage

        // Year calendar preview
        val yearBitmap = WallpaperGenerator.generate(this)
        findViewById<ImageView>(R.id.ivYearPreview).setImageBitmap(yearBitmap)

        // Life calendar preview if DOB saved
        val prefs = getSharedPreferences("life_cal_prefs", MODE_PRIVATE)
        val birthEpoch = prefs.getLong("birth_epoch", -1L)
        if (birthEpoch != -1L) {
            val lifeBitmap = LifeCalendarGenerator.generate(this)
            val ivLife = findViewById<ImageView>(R.id.ivLifePreview)
            ivLife.setImageBitmap(lifeBitmap)
            ivLife.visibility = View.VISIBLE
        }

        // Card clicks
        findViewById<LinearLayout>(R.id.cardYear).setOnClickListener {
            startActivity(Intent(this, YearCalendarActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.cardLife).setOnClickListener {
            startActivity(Intent(this, LifeCalendarActivity::class.java))
        }

        scheduleMidnightRefresh()
    }

    private fun scheduleMidnightRefresh() {
        val now = Calendar.getInstance()
        val midnight = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (!after(now)) add(Calendar.DAY_OF_MONTH, 1)
        }
        val delay = midnight.timeInMillis - now.timeInMillis
        val request = PeriodicWorkRequestBuilder<WallpaperWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "wallpaper_refresh",
            ExistingPeriodicWorkPolicy.REPLACE,
            request
        )
    }
}