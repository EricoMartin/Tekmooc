package com.example.tekmooc.ui.auth.viewmodel

import androidx.lifecycle.MutableLiveData

import com.google.firebase.auth.FirebaseUser

import android.app.Application

import androidx.annotation.NonNull

import androidx.lifecycle.AndroidViewModel
import com.example.tekmooc.repo.AuthRepository


class SignedInUserViewModel (application: Application) : AndroidViewModel(application) {

        private val authRepository: AuthRepository
        val userLiveData: MutableLiveData<FirebaseUser>
        val loggedOutLiveData: MutableLiveData<Boolean>

        fun logOut() {
            authRepository.logOut()
        }

        init {
            authRepository = AuthRepository(application)
            userLiveData = authRepository.getUserLiveData()!!
            loggedOutLiveData = authRepository.getLoggedOutLiveData()!!
        }
    
}