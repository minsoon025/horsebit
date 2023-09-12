package com.a406.horsebit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.a406.horsebit.databinding.ActivityOrderBinding

class OrderActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityOrderBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }
}