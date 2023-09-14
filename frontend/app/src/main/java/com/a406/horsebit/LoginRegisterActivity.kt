package com.a406.horsebit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.a406.horsebit.databinding.ActivityLoginRegisterBinding

class LoginRegisterActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.tvLookingText1.setOnClickListener {
            // LinearLayout의 visibility를 visible로 변경합니다.

            if (binding.llhRegisterGone1.visibility == View.VISIBLE) {
                binding.llhRegisterGone1.visibility = View.GONE
            }
            else {
                binding.llhRegisterGone1.visibility = View.VISIBLE
            }

        }

        binding.tvUnderAgree1.setOnClickListener {
            // LinearLayout의 visibility를 visible로 변경합니다.

            if (binding.llhRegisterGone1.visibility == View.VISIBLE) {
                binding.llhRegisterGone1.visibility = View.GONE
            }
            else {
                binding.llhRegisterGone1.visibility = View.VISIBLE
            }

        }

        binding.tvLookingText2.setOnClickListener {
            // LinearLayout의 visibility를 visible로 변경합니다.
            if (binding.llhRegisterGone2.visibility == View.VISIBLE) {
                binding.llhRegisterGone2.visibility = View.GONE
            }
            else {
                binding.llhRegisterGone2.visibility = View.VISIBLE
            }

        }

        binding.tvUnderAgree2.setOnClickListener {
            // LinearLayout의 visibility를 visible로 변경합니다.
            if (binding.llhRegisterGone2.visibility == View.VISIBLE) {
                binding.llhRegisterGone2.visibility = View.GONE
            }
            else {
                binding.llhRegisterGone2.visibility = View.VISIBLE
            }

        }

        binding.tvLookingText3.setOnClickListener {
            // LinearLayout의 visibility를 visible로 변경합니다.
            if (binding.llhRegisterGone3.visibility == View.VISIBLE) {
                binding.llhRegisterGone3.visibility = View.GONE
            }
            else {
                binding.llhRegisterGone3.visibility = View.VISIBLE
            }

        }

        binding.tvUnderAgree3.setOnClickListener {
            // LinearLayout의 visibility를 visible로 변경합니다.
            if (binding.llhRegisterGone3.visibility == View.VISIBLE) {
                binding.llhRegisterGone3.visibility = View.GONE
            }
            else {
                binding.llhRegisterGone3.visibility = View.VISIBLE
            }

        }

        binding.tvLookingText4.setOnClickListener {
            if (binding.llhRegisterGone4.visibility == View.VISIBLE) {
                binding.llhRegisterGone4.visibility = View.GONE
            }
            else {
                binding.llhRegisterGone4.visibility = View.VISIBLE
            }

        }

        binding.tvUnderAgree4.setOnClickListener {
            if (binding.llhRegisterGone4.visibility == View.VISIBLE) {
                binding.llhRegisterGone4.visibility = View.GONE
            }
            else {
                binding.llhRegisterGone4.visibility = View.VISIBLE
            }

        }

        binding.flRegisterFinal.setOnClickListener {
            val intent = Intent(binding.root.context, MainActivity::class.java)
            binding.root.context.startActivity(intent)
        }


    }
}