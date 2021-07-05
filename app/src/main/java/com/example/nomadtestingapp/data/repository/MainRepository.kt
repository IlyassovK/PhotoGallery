package com.example.nomadtestingapp.data.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movielist.other.Resource
import com.example.nomadtestingapp.data.models.*
import com.example.nomadtestingapp.data.retrofit.RetrofitClient
import com.example.nomadtestingapp.data.retrofit.SessionManager
import com.example.nomadtestingapp.data.services.RetrofitService
import com.example.nomadtestingapp.database.GalleryDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import javax.inject.Inject

class MainRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sessionManager = SessionManager(context)

    private val _photoList = MutableLiveData<Resource<List<File>>>()
    val photoList: LiveData<Resource<List<File>>> = _photoList

    private val _currentUser = MutableLiveData<Resource<User>>()
    val currentUser: LiveData<Resource<User>> = _currentUser

    init {
        _photoList.postValue(Resource.loading(null))
        _currentUser.postValue(Resource.loading(null))
    }

    fun login(email: String, password: String){
        val service = RetrofitClient.getRetrofitInstance(context)!!.create(RetrofitService::class.java)
        val signInInfo = SignInBody(email, password)
        val call = service.login(signInInfo)

        call.enqueue(object : Callback<SignInSignUpResponse>{
            override fun onResponse(call: Call<SignInSignUpResponse>, response: retrofit2.Response<SignInSignUpResponse>) {
                val loginResponse = response.body()
                if(loginResponse?.success == true){
                    sessionManager.saveAccessToken(loginResponse.accessToken)
                    sessionManager.saveRefreshToken(loginResponse.refreshToken)
                    createCurrentUser(email, password)
                }
            }

            override fun onFailure(call: Call<SignInSignUpResponse>, t: Throwable) {
                Toast.makeText(context, "Login error!", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun createCurrentUser(email: String, password: String){
        val user = User(email, password)
        sessionManager.safeCurrentUser(email)
        _currentUser.postValue(Resource.success(user))
    }

    fun register(email: String, password: String){
        val service = RetrofitClient.getRetrofitInstance(context)!!.create(RetrofitService::class.java)
        val registerInfo = SignInBody(email, password)
        val call = service.register(registerInfo)

        call.enqueue(object : Callback<SignInSignUpResponse>{
            override fun onResponse(call: Call<SignInSignUpResponse>, response: retrofit2.Response<SignInSignUpResponse>) {
                val registerResponse = response.body()
                if(registerResponse?.success == true){
                    sessionManager.saveAccessToken(registerResponse.accessToken)
                    sessionManager.saveRefreshToken(registerResponse.refreshToken)
                    createCurrentUser(email, password)
                }
            }

            override fun onFailure(call: Call<SignInSignUpResponse>, t: Throwable) {
                Toast.makeText(context, "Registration error!", Toast.LENGTH_SHORT).show()

            }
        })
    }

    fun postPhoto(photo: Photo){
        val service = RetrofitClient.getRetrofitInstance(context)!!.create(RetrofitService::class.java)
        val call = service.postFile(token = "Bearer ${sessionManager.fetchAccessToken()}", file = photo)

        call.enqueue(object : Callback<FileResponse>{
            override fun onResponse(call: Call<FileResponse>, response: Response<FileResponse>) {
                Toast.makeText(context, "Posted", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<FileResponse>, t: Throwable) {
                Toast.makeText(context, "Error. Can't post photo!", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun getAllPhotos(){
        CoroutineScope(Dispatchers.IO).launch {
            GalleryDatabase.getDatabase(context).galleryDao().getAllPhotos(sessionManager.fetchCurrentUser()).collect { data ->
                try {

                    var list = data.toMutableList()
                    list.add(0, File(path = "", userEmail = ""))

                    _photoList.postValue(Resource.success(list))
                }catch (e: Exception){
                    _photoList.postValue(Resource.error("Error", null))
                }
            }
        }
    }



    fun insertPhotoIntoDb(photo: File){
        CoroutineScope(Dispatchers.IO).launch{
            photo.userEmail = sessionManager.fetchCurrentUser()
            GalleryDatabase.getDatabase(context).galleryDao().insertPhoto(photo)
        }
    }

}