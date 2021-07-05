package com.example.nomadtestingapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import dagger.multibindings.IntoMap

data class Photo(
    val fileDate: String,
    val fileName: String,
    val size: Long,
    val type: String = "photo"

) {
}