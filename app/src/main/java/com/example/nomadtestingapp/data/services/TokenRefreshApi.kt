package com.example.nomadtestingapp.data.services

import com.example.nomadtestingapp.data.models.SignInSignUpResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenRefreshApi {

    @POST("/auth/refresh")
    fun refreshAccessToken(@Body refreshToken: String): SignInSignUpResponse
}