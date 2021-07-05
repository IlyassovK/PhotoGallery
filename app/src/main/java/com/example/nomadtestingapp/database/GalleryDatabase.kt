package com.example.nomadtestingapp.database

import android.content.Context
import androidx.room.*
import com.example.nomadtestingapp.data.models.File
import com.example.nomadtestingapp.data.models.Photo
import com.example.nomadtestingapp.data.models.converters.FileConverter

@Database(entities = [File::class], version = 1, exportSchema = false)
@TypeConverters(FileConverter::class)
abstract class GalleryDatabase : RoomDatabase(){

    companion object{
        var galleryDatabase: GalleryDatabase ?= null

        @Synchronized
        fun getDatabase(context: Context): GalleryDatabase{
            if(galleryDatabase == null){
                galleryDatabase = Room.databaseBuilder(
                    context,
                    GalleryDatabase::class.java,
                    "gallery.db"
                ).build()
            }
            return galleryDatabase!!
        }
    }

    abstract fun galleryDao(): GalleryDao

}