package com.a406.horsebit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.a406.horsebit.databinding.ActivityLoginMainBinding

class LoginRegisterActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)
        setContentView(binding.root)
    }
}