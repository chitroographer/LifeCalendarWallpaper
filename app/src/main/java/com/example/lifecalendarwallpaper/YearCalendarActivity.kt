package com.example.lifecalendarwallpaper

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate

class YearCalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_year_calendar)

        // Back button
        findViewById<TextView>(R.id.btnBack).setOnClickListener { finish() }

        // Stats
        val today = LocalDate.now()
        val totalDays = if (today.isLeapYear) 366 else 365
        val dayOfYear = today.dayOfYear
        val daysLeft = totalDays - dayOfYear
        val percentage = (dayOfYear.toFloat() / totalDays * 100).toInt()

        findViewById<TextView>(R.id.tvPreviewStats).text =
            "${daysLeft}d left  ·  ${percentage}%"

        // Generate preview
        val bitmap = WallpaperGenerator.generate(this)
        findViewById<ImageView>(R.id.ivPreview).setImageBitmap(bitmap)

        // Set wallpaper button
        findViewById<Button>(R.id.btnSetWallpaper).setOnClickListener {
            try {
                WallpaperSetter.saveAndOpen(this, bitmap)
            } catch (e: Exception) {
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}