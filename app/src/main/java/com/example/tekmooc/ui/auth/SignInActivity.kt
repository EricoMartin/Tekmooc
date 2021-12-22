package com.example.tekmooc.ui.auth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.tekmooc.R
import com.example.tekmooc.databinding.ActivitySignInBinding
import com.example.tekmooc.ui.MainActivity
import com.example.tekmooc.ui.auth.viewmodel.SignUpViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.security.AccessController.getContext


class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private var signUpViewModel: SignUpViewModel? = null

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("423659255042-bpkfspqm6a7a4pi2419m5b30dhegm7p5.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        //

        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        signUpViewModel!!.getUserLiveData()?.observe(this, object : Observer<FirebaseUser?> {
            override fun onChanged(firebaseUser: FirebaseUser?) {
                if (firebaseUser != null) {

                    startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                    finish()
    //                    Navigation.findNavController(getView())
    //                        .navigate(R.id.action_loginRegisterFragment_to_loggedInFragment)
                }
            }
        })
        handleClick(binding)

        binding.signInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun handleClick(binding: ActivitySignInBinding) {

        val emailText = binding.editTextEmail
        val passwordText = binding.editTextPassword
        val button = binding.button
        button.setOnClickListener {
            val email: String = emailText.text.toString()
            val password: String = passwordText.text.toString()
            if (email.length > 12 && password.length > 8) {
                SignUpViewModel(application).register(email, password)
            } else {
                Toast.makeText(
                    this,
                    "Email Address and Password Must Be Entered",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user == null){
            Log.w(TAG, "User is null. Try again!")
            return
        }else{
            startActivity(Intent(this@SignInActivity, MainActivity::class.java))
            finish()
        }
    }

    companion object{
        private val RC_SIGN_IN = 9001
        private val TAG = "SignInActivity"
    }
}