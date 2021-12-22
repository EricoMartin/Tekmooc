package com.example.tekmooc.ui.auth.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseUser

import androidx.lifecycle.MutableLiveData
import com.example.tekmooc.repo.AuthRepository

class SignUpViewModel(application: Application) : AndroidViewModel(application) {
    private var authRepository: AuthRepository? = null
    private var userLiveData: MutableLiveData<FirebaseUser>? = null

    init {
        authRepository = AuthRepository(application)
        userLiveData = authRepository!!.getUserLiveData()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun login(email: String?, password: String?) {
        authRepository?.login(email, password)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun register(email: String?, password: String?) {
        authRepository?.register(email, password)
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser>? {
        return userLiveData
    }
}