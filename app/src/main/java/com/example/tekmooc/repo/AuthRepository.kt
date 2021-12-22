package com.example.tekmooc.repo

import android.app.Application
import android.content.Intent
import android.os.Build
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.MutableLiveData
import com.example.tekmooc.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class AuthRepository(application: Application?) {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var application: Application = application!!
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    private var userLiveData: MutableLiveData<FirebaseUser>? = null
    private var loggedOutLiveData: MutableLiveData<Boolean>? = null
    private val TAG = "AuthRepository"

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("423659255042-bpkfspqm6a7a4pi2419m5b30dhegm7p5.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(application!!.applicationContext, gso)
        userLiveData = MutableLiveData()
        loggedOutLiveData = MutableLiveData()
        if (auth.currentUser != null) {
            userLiveData!!.postValue(auth.currentUser)
            loggedOutLiveData!!.postValue(false)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun register(email: String?, password: String?){
        auth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(application.mainExecutor, OnCompleteListener<AuthResult>() {
                    task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        userLiveData?.postValue(auth.currentUser)
                        val user = auth.currentUser
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            application.applicationContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            })
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun login(email: String?, password: String?) {
        auth.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(application.mainExecutor,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        userLiveData!!.postValue(auth.getCurrentUser())
                    } else {
                        Toast.makeText(
                            application.applicationContext,
                            "Login Failure: " + task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
    }

    fun logOut() {
        auth.signOut()
        loggedOutLiveData!!.postValue(true)
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser>? {
        return userLiveData
    }

    fun getLoggedOutLiveData(): MutableLiveData<Boolean>? {
        return loggedOutLiveData
    }
}