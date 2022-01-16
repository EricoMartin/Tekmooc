package com.example.tekmooc.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tekmooc.R
import com.example.tekmooc.databinding.ActivityHomeBinding
import com.example.tekmooc.ui.auth.SignInActivity
import com.example.tekmooc.ui.auth.SignUpActivity
import com.google.android.material.button.MaterialButton

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val materialButton1 = findViewById<MaterialButton>(R.id.sign_in_zc_btn)
        val materialButton2 = findViewById<MaterialButton>(R.id.register_zc_btn)

        materialButton1.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
        materialButton2.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}