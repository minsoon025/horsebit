package com.a406.horsebit

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

        binding.tvLoginPass.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
    }
}