package com.example.nomadtestingapp.data.services

import com.example.nomadtestingapp.data.models.FileResponse
import com.example.nomadtestingapp.data.models.Photo
import com.example.nomadtestingapp.data.models.SignInSignUpResponse
import com.example.nomadtestingapp.data.models.SignInBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface RetrofitService {

    @POST("/auth/signIn")
    fun login(@Body info: SignInBody): Call<SignInSignUpResponse>

    @POST("/auth/signUp")
    fun register(@Body info: SignInBody): Call<SignInSignUpResponse>

    @POST("/meta/add")
    fun postFile(@Header("Authorization") token: String, @Body file: Photo): Call<FileResponse>

}