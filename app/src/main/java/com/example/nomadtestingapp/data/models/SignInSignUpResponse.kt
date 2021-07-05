package com.example.nomadtestingapp.data.models

data class SignInSignUpResponse(
    var success: Boolean,
    var accessToken: String,
    var refreshToken: String
)