package com.example.nomadtestingapp.ui.viewmodels

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movielist.other.Resource
import com.example.nomadtestingapp.data.models.File
import com.example.nomadtestingapp.data.models.Photo
import com.example.nomadtestingapp.data.models.User
import com.example.nomadtestingapp.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.URI

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
@HiltViewModel
class PhotoGalleryViewModel @Inject constructor(
    private val repository: MainRepository,
    @ApplicationContext val context: Context
): ViewModel() {


    val photoList: LiveData<Resource<List<File>>> = repository.photoList

    val currentUser: LiveData<Resource<User>> = repository.currentUser

    fun getAllPhotos() {
        repository.getAllPhotos()
    }

    private fun postPhoto(photo: Photo) {
        repository.postPhoto(photo)
    }

    fun addPhotoToDataBase(imageUri: Uri) {

        repository.insertPhotoIntoDb(File(path = imageUri.toString()))

        val mime = MimeTypeMap.getSingleton()
        val type: String? = mime.getExtensionFromMimeType(context.contentResolver.getType(imageUri))

        val file: java.io.File = java.io.File(imageUri.path)
        val date = file.lastModified()

        context.contentResolver.query(imageUri, null, null, null, null).use {
            it?.let { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

                cursor.moveToFirst()

                val name = cursor.getString(nameIndex)
                val size = cursor.getLong(sizeIndex)

                //todo send to server
                name
                val a = convertLongToTime(date)
                size

                postPhoto(Photo(fileName = name, fileDate = convertLongToTime(date), size = size, type = type!!))
            }

        }
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return format.format(date)
    }
}
