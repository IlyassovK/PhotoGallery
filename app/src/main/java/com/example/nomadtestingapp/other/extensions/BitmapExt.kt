package com.example.nomadtestingapp.other.extensions

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.io.OutputStream

fun Bitmap.saveMediaToStorage(context: Context,bitmap: Bitmap): Uri?{
    val filename = "${System.currentTimeMillis()}.jpg"
    var fos: OutputStream? = null

    var imageUri: Uri? = null

    context.contentResolver?.also { resolver ->
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
        imageUri =
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        fos = imageUri?.let { resolver.openOutputStream(it) }
    }

    fos?.use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        Toast.makeText(context, "Изображение сохранено", Toast.LENGTH_SHORT).show()
    }
    return imageUri
}
