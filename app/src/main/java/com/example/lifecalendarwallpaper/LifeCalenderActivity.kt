package com.example.lifecalendarwallpaper

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate

class LifeCalendarActivity : AppCompatActivity() {

    private var generatedBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_calendar)

        findViewById<TextView>(R.id.btnBack).setOnClickListener { finish() }

        // Load saved values if they exist
        val prefs = getSharedPreferences("life_cal_prefs", MODE_PRIVATE)
        val birthEpoch = prefs.getLong("birth_epoch", -1L)
        val savedLifespan = prefs.getInt("lifespan", 72)
        if (birthEpoch != -1L) {
            val birthDate = LocalDate.ofEpochDay(birthEpoch)
            findViewById<EditText>(R.id.etDay).setText(birthDate.dayOfMonth.toString())
            findViewById<EditText>(R.id.etMonth).setText(birthDate.monthValue.toString())
            findViewById<EditText>(R.id.etYear).setText(birthDate.year.toString())
            findViewById<EditText>(R.id.etLifespan).setText(savedLifespan.toString())
        }

        findViewById<Button>(R.id.btnGenerate).setOnClickListener {
            generateCalendar()
        }

        findViewById<Button>(R.id.btnSetWallpaper).setOnClickListener {
            generatedBitmap?.let {
                WallpaperSetter.saveAndOpen(this, it)
            } ?: Toast.makeText(this, "Please generate first!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateCalendar() {
        val dayStr = findViewById<EditText>(R.id.etDay).text.toString().trim()
        val monthStr = findViewById<EditText>(R.id.etMonth).text.toString().trim()
        val yearStr = findViewById<EditText>(R.id.etYear).text.toString().trim()
        val lifespanStr = findViewById<EditText>(R.id.etLifespan).text.toString().trim()

        if (dayStr.isEmpty() || monthStr.isEmpty() || yearStr.isEmpty()) {
            Toast.makeText(this, "Please enter your date of birth", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val day = dayStr.toInt()
            val month = monthStr.toInt()
            val year = yearStr.toInt()
            val lifespan = if (lifespanStr.isEmpty()) 72 else lifespanStr.toInt()

            val birthDate = LocalDate.of(year, month, day)
            val today = LocalDate.now()

            if (birthDate.isAfter(today)) {
                Toast.makeText(this, "Birth date cannot be in the future!", Toast.LENGTH_SHORT).show()
                return
            }

            val weeksLived = ((today.toEpochDay() - birthDate.toEpochDay()) / 7).toInt()
            val totalWeeks = lifespan * 52
            val weeksLeft = totalWeeks - weeksLived
            val lifePercent = (weeksLived.toFloat() / totalWeeks * 100).toInt()

            // Save to prefs
            val prefs = getSharedPreferences("life_cal_prefs", MODE_PRIVATE)
            prefs.edit()
                .putLong("birth_epoch", birthDate.toEpochDay())
                .putInt("lifespan", lifespan)
                .apply()

            // Update stats
            findViewById<TextView>(R.id.tvWeeksLived).text = weeksLived.toString()
            findViewById<TextView>(R.id.tvWeeksLeft).text = weeksLeft.toString()
            findViewById<TextView>(R.id.tvLifePercent).text = lifePercent.toString()
            findViewById<LinearLayout>(R.id.statsCard).visibility = View.VISIBLE

            // Generate bitmap
            val bitmap = LifeCalendarGenerator.generateWithParams(this, birthDate, lifespan)
            generatedBitmap = bitmap

            // Show preview
            findViewById<ImageView>(R.id.ivPreview).setImageBitmap(bitmap)
            findViewById<LinearLayout>(R.id.previewCard).visibility = View.VISIBLE
            findViewById<Button>(R.id.btnSetWallpaper).visibility = View.VISIBLE

            Toast.makeText(this, "Calendar generated!", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Toast.makeText(this, "Invalid date! Please check your input.", Toast.LENGTH_SHORT).show()
        }
    }
}