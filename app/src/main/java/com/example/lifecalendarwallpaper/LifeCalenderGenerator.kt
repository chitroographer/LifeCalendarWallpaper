package com.example.lifecalendarwallpaper

import android.content.Context
import android.graphics.*
import java.time.LocalDate

object LifeCalendarGenerator {

    fun generateWithParams(context: Context, birthDate: LocalDate, lifespan: Int): Bitmap {
        val today = LocalDate.now()
        val weeksLived = ((today.toEpochDay() - birthDate.toEpochDay()) / 7).toInt()
        val totalWeeks = lifespan * 52
        return drawBitmap(weeksLived, totalWeeks)
    }

    fun generate(context: Context): Bitmap {
        val prefs = context.getSharedPreferences("life_cal_prefs", Context.MODE_PRIVATE)
        val birthEpoch = prefs.getLong("birth_epoch", -1L)
        val lifespan = prefs.getInt("lifespan", 72)
        return if (birthEpoch == -1L) {
            drawBitmap(0, lifespan * 52)
        } else {
            val today = LocalDate.now()
            val birthDate = LocalDate.ofEpochDay(birthEpoch)
            val weeksLived = ((today.toEpochDay() - birthDate.toEpochDay()) / 7).toInt()
            drawBitmap(weeksLived, lifespan * 52)
        }
    }

    private fun drawBitmap(weeksLived: Int, totalWeeks: Int): Bitmap {
        val width = 1220
        val height = 2712
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.BLACK)

        val paint = Paint().apply { isAntiAlias = true }

        val cols = 52
        val dotRadius = 12f
        val dotSpacing = 22f
        val totalGridWidth = cols * dotSpacing
        val startX = (width - totalGridWidth) / 2f + dotSpacing / 2f
        val startY = height * 0.28f

        for (i in 0 until totalWeeks) {
            val col = i % cols
            val row = i / cols
            val cx = startX + col * dotSpacing
            val cy = startY + row * dotSpacing

            paint.color = when {
                i < weeksLived -> Color.WHITE
                i == weeksLived -> Color.parseColor("#FF6B00")
                else -> Color.parseColor("#2A2A2A")
            }

            canvas.drawCircle(cx, cy, dotRadius, paint)
        }

        val textPaint = Paint().apply {
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
            textSize = 38f
            color = Color.parseColor("#FF6B00")
        }

        val weeksLeft = totalWeeks - weeksLived
        canvas.drawText(
            "${weeksLeft} weeks left",
            width / 2f,
            height * 0.88f,
            textPaint
        )

        return bitmap
    }
}