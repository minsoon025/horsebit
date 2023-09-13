package com.a406.horsebit

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.a406.horsebit.databinding.ActivityLoginMainBinding

class LoginMainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.ivGoogleLogin.setOnClickListener {
            val intent = Intent(binding.root.context, LoginRegisterActivity::class.java)
            binding.root.context.startActivity(intent)
        }

        binding.tvLoginPass.setOnClickListener {
            val intent = Intent(binding.root.context, MainActivity::class.java)
            binding.root.context.startActivity(intent)
        }
    }

}