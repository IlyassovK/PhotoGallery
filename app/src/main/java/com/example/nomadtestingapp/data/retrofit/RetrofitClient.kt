package com.example.nomadtestingapp.data.retrofit

import android.content.Context
import com.example.nomadtestingapp.BuildConfig
import com.example.nomadtestingapp.data.services.RetrofitService
import com.example.nomadtestingapp.data.services.TokenRefreshApi
import com.example.nomadtestingapp.other.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    companion object {

        private fun getRetrofitClient(authenticator: Authenticator? = null): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor { chain ->
                    chain.proceed(chain.request().newBuilder().also {
                        it.addHeader("Accept", "application/json")
                    }.build())
                }.also { client ->
                    authenticator?.let { client.authenticator(it) }
                    if (BuildConfig.DEBUG) {
                        val logging = HttpLoggingInterceptor()
                        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                        client.addInterceptor(logging)
                    }
                }.build()
        }

        var retrofit: Retrofit? = null

        fun getRetrofitInstance(context: Context): Retrofit? {
            if (retrofit == null) {

                val authenticator = TokenAuthenticator(context)

                retrofit = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(getRetrofitClient(authenticator))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }

         fun buildTokenApi(): TokenRefreshApi {
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(getRetrofitClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TokenRefreshApi::class.java)
        }

    }


}
