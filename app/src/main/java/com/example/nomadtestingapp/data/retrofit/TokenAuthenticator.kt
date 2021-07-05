package com.example.nomadtestingapp.data.retrofit

import android.content.Context
import com.example.movielist.other.Resource
import com.example.movielist.other.Status
import com.example.nomadtestingapp.data.models.SignInSignUpResponse
import com.example.nomadtestingapp.data.services.RetrofitService
import com.example.nomadtestingapp.data.services.TokenRefreshApi
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    context: Context
): Authenticator{

    private val sessionManager = SessionManager(context)

    val service: TokenRefreshApi = RetrofitClient.buildTokenApi()


    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            val tokenResponse = getUpdatedToken()
            when(tokenResponse.status){
                Status.SUCCESS -> {
                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${sessionManager.fetchAccessToken()}")
                        .build()
                }
                else -> null
            }
        }
    }

    suspend fun getUpdatedToken(): Resource<SignInSignUpResponse>{

        val refreshToken = sessionManager.fetchRefreshToken()?.first()

        val response = service.refreshAccessToken(refreshToken = refreshToken.toString())

        if(response.success){
            sessionManager.saveAccessToken(response.accessToken)
            sessionManager.saveRefreshToken(response.refreshToken)
            return Resource.success(response)
        }

        return Resource.error("error", null)
    }


}