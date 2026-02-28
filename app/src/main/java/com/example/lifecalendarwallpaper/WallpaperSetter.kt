package com.example.lifecalendarwallpaper

import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore

object WallpaperSetter {

    fun saveAndOpen(context: Context, bitmap: Bitmap) {
        val uri = saveBitmapToGallery(context, bitmap)
        if (uri != null) {
            openImageForWallpaper(context, uri)
        }
    }

    private fun saveBitmapToGallery(context: Context, bitmap: Bitmap): Uri? {
        return try {
            val filename = "life_calendar.png"
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }

            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ) ?: return null

            context.contentResolver.openOutputStream(uri)?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            }

            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            context.contentResolver.update(uri, contentValues, null, null)

            uri
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun openImageForWallpaper(context: Context, uri: Uri) {
        // Open the image with a chooser so user can pick "Set as wallpaper"
        val intent = Intent(Intent.ACTION_ATTACH_DATA).apply {
            setDataAndType(uri, "image/png")
            putExtra("mimeType", "image/png")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(intent, "Set as wallpaper").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }
}