package com.example.nomadtestingapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movielist.other.Resource
import com.example.nomadtestingapp.data.models.User
import com.example.nomadtestingapp.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginRegisterViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    val currentUser: LiveData<Resource<User>> = repository.currentUser

    fun login(email: String, password: String){
        repository.login(email, password)
    }

    fun register(email: String, password: String){
        repository.register(email, password)
    }


}