package com.example.lifecalendarwallpaper

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class WallpaperWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        val bitmap = WallpaperGenerator.generate(applicationContext)
        WallpaperSetter.saveAndOpen(applicationContext, bitmap)
        return Result.success()
    }
}