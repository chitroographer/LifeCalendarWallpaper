package com.example.lifecalendarwallpaper

import android.content.Context
import android.graphics.*
import java.time.LocalDate

object WallpaperGenerator {

    fun generate(context: Context): Bitmap {
        // Your exact screen resolution
        val width = 1220
        val height = 2712
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        canvas.drawColor(Color.BLACK)

        val today = LocalDate.now()
        val totalDays = if (today.isLeapYear) 366 else 365
        val dayOfYear = today.dayOfYear
        val daysLeft = totalDays - dayOfYear
        val percentage = (dayOfYear.toFloat() / totalDays * 100).toInt()

        drawDotGrid(canvas, dayOfYear, totalDays, width, height)
        drawBottomStats(canvas, daysLeft, percentage, width, height)

        return bitmap
    }

    private fun drawDotGrid(
        canvas: Canvas,
        dayOfYear: Int,
        totalDays: Int,
        width: Int,
        height: Int
    ) {
        val paint = Paint().apply { isAntiAlias = true }

        val cols = 15       // 7 dots per row = 1 week per row
        val rows = 24      // 53 rows to cover all possible days

        val dotRadius = 16f
        val dotSpacing = 52f  // adjusted for your screen width with 7 cols

        // Safe zones: top 22% for clock, bottom 18% for fingerprint
        val topSafeZone = height * 0.26f
        val bottomSafeZone = height * 0.82f

        val availableHeight = bottomSafeZone - topSafeZone
        val totalGridWidth = cols * dotSpacing
        val totalGridHeight = rows * dotSpacing

        val startX = (width - totalGridWidth) / 2f + dotSpacing / 2f
        val startY = topSafeZone + (availableHeight - totalGridHeight) / 2f

        for (i in 0 until totalDays) {
            val col = i % cols
            val row = i / cols
            val cx = startX + col * dotSpacing
            val cy = startY + row * dotSpacing

            paint.color = when {
                i + 1 < dayOfYear -> Color.WHITE
                i + 1 == dayOfYear -> Color.parseColor("#FF6B00")
                else -> Color.parseColor("#3A3A3A")
            }

            canvas.drawCircle(cx, cy, dotRadius, paint)
        }
    }

    private fun drawBottomStats(
        canvas: Canvas,
        daysLeft: Int,
        percentage: Int,
        width: Int,
        height: Int
    ) {
        val paint = Paint().apply {
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
            textSize = 38f
            color = Color.parseColor("#FF6B00")
        }

        // Place stats just above the fingerprint safe zone
        canvas.drawText("${daysLeft}d left  ·  ${percentage}%", width / 2f, height * 0.85f, paint)
    }
}