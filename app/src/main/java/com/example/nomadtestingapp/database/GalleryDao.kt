package com.example.nomadtestingapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nomadtestingapp.data.models.File
import com.example.nomadtestingapp.data.models.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface GalleryDao {

    @Query("SELECT * FROM photo WHERE userEmail = :currentUserEmail")
    fun getAllPhotos(currentUserEmail: String?): Flow<List<File>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(photo: File)
}