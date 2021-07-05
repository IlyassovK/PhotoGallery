package com.example.nomadtestingapp.data.models.converters

import androidx.room.TypeConverter
import com.example.nomadtestingapp.data.models.File
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FileConverter {

    @TypeConverter
    fun fromFile(file: File): String?{
        if(file == null){
            return null
        }else{
            val gson = Gson()
            val type = object : TypeToken<File>(){

            }.type
            return gson.toJson(file, type)
        }
    }

    @TypeConverter
    fun toFile(fileString: String): File?{
        if(fileString == null){
            return null
        }else{
            val gson = Gson()
            val type = object : TypeToken<File>(){

            }.type
            return gson.fromJson(fileString, type)
        }
    }
}