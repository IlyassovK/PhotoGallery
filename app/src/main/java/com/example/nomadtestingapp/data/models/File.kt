package com.example.nomadtestingapp.data.models

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.nomadtestingapp.data.models.converters.FileConverter
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Photo")
data class File(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "path")
    @Expose
    @SerializedName("path")
    @TypeConverters(FileConverter::class)
    val path: String,

    @ColumnInfo(name = "userEmail")
    @Expose
    @SerializedName("useremail")
    @TypeConverters(FileConverter::class)
    var userEmail: String? = ""
) {
}