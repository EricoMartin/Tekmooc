package com.example.tekmooc.ui.auth

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.tekmooc.R
import com.example.tekmooc.databinding.ActivitySignInBinding
import com.example.tekmooc.databinding.ActivitySignUpBinding
import com.example.tekmooc.ui.MainActivity
import com.example.tekmooc.ui.auth.viewmodel.SignUpViewModel
import com.google.firebase.auth.FirebaseUser
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.security.AccessController.getContext


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private var signUpViewModel: SignUpViewModel? = null

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        signUpViewModel!!.getUserLiveData()?.observe(this, object : Observer<FirebaseUser?> {
            override fun onChanged(firebaseUser: FirebaseUser?) {
                if (firebaseUser != null) {

                    startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                    //                    Navigation.findNavController(getView())
                    //                        .navigate(R.id.action_loginRegisterFragment_to_loggedInFragment)
                }
            }
        })
        handleClick(binding)
    }
    @RequiresApi(Build.VERSION_CODES.P)
    private fun handleClick(binding: ActivitySignUpBinding) {
        val nameTxt = binding.editTextTextPersonName
        val emailTxt = binding.editTextTextEmail
        val passwordTxt = binding.passwordText
        val btnSignUp = binding.button

        btnSignUp.setOnClickListener {
            val email: String = emailTxt.text.toString()
            val password: String = passwordTxt.text.toString()
            if (email.length > 12 && password.length > 8) {
                SignUpViewModel(application).register(email, password)
            } else {
                Toast.makeText(
                    baseContext,
                    "Email Address and Password Must Be Entered",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}