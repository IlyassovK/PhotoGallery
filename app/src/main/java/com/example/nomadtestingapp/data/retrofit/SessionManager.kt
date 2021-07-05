package com.example.nomadtestingapp.data.retrofit

import android.content.Context
import android.content.SharedPreferences
import com.example.nomadtestingapp.R
import com.example.nomadtestingapp.data.models.User
import com.google.gson.reflect.TypeToken

class SessionManager(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object{
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
        const val CURRENT_USER_EMAIL = "current_user"
    }

    fun safeCurrentUser(email: String){
        val editor = prefs.edit()
        editor.putString(CURRENT_USER_EMAIL, email)
        editor.apply()
    }

    fun fetchCurrentUser(): String?{
        return prefs.getString(CURRENT_USER_EMAIL, null)
    }

    fun saveAccessToken(token: String){
        val editor = prefs.edit()
        editor.putString(ACCESS_TOKEN, token)
        editor.apply()
    }

    fun fetchAccessToken(): String?{
        return prefs.getString(ACCESS_TOKEN, null)
    }

    fun clearAccessToken(){
        val editor = prefs.edit()
        editor.putString(ACCESS_TOKEN, "")
        editor.apply()
    }

    fun saveRefreshToken(token: String){
        val editor = prefs.edit()
        editor.putString(REFRESH_TOKEN, token)
        editor.apply()
    }

    fun fetchRefreshToken(): String?{
        return prefs.getString(REFRESH_TOKEN, null)
    }
}