package com.example.nomadtestingapp.data.models

data class FileResponse(
    val `data`: Data,
    val success: Boolean
)

data class Data(
    val fileDate: String,
    val fileName: String,
    val id: Int,
    val size: Int,
    val type: String
)