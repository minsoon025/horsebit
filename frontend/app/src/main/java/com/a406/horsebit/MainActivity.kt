package com.a406.horsebit

import ExchangeFragment
import MoreFragment
import android.content.Intent
import android.graphics.ColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.a406.horsebit.databinding.ActivityMainBinding
import androidx.preference.PreferenceManager

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 첫 로그인인 경우 or 로그아웃 후 처음 방문한다면 LoginMainActivity로 이동
        val pref = PreferenceManager.getDefaultSharedPreferences(this)  // import androidx.preference.PreferenceManager 인지 확인

        if(pref.getBoolean("firstLoginChk", true)) {
            val intent = Intent(binding.root.context, LoginMainActivity::class.java)
            binding.root.context.startActivity(intent)
        }

        changeFrag(0)

        binding.ivHomeNav.setOnClickListener {
            changeFrag(0)
        }

        binding.ivMyPageNav.setOnClickListener {
            changeFrag(1)
        }

        binding.ivExchangeNav.setOnClickListener {
            changeFrag(2)
        }

        binding.ivMoreNav.setOnClickListener {
            changeFrag(3)
        }

    }

    private fun changeFrag(fragNum: Int) {
        val ft = supportFragmentManager.beginTransaction()

        when(fragNum){
            0 -> {
                binding.ivHomeNav.setColorFilter(getColor(R.color.white))
                binding.ivMyPageNav.setColorFilter(getColor(R.color.normal_hover))
                binding.ivExchangeNav.setColorFilter(getColor(R.color.normal_hover))
                binding.ivMoreNav.setColorFilter(getColor(R.color.normal_hover))

                val changeFragment = HomeFragment()
                ft.replace(R.id.fl_MainFrameLayout, changeFragment)
                ft.commit()
            }
            1 -> {
                binding.ivHomeNav.setColorFilter(getColor(R.color.normal_hover))
                binding.ivMyPageNav.setColorFilter(getColor(R.color.white))
                binding.ivExchangeNav.setColorFilter(getColor(R.color.normal_hover))
                binding.ivMoreNav.setColorFilter(getColor(R.color.normal_hover))

                val changeFragment = MyPageFragment()
                ft.replace(R.id.fl_MainFrameLayout, changeFragment)
                ft.commit()
            }
            2 -> {
                binding.ivHomeNav.setColorFilter(getColor(R.color.normal_hover))
                binding.ivMyPageNav.setColorFilter(getColor(R.color.normal_hover))
                binding.ivExchangeNav.setColorFilter(getColor(R.color.white))
                binding.ivMoreNav.setColorFilter(getColor(R.color.normal_hover))

                val changeFragment = ExchangeFragment()
                ft.replace(R.id.fl_MainFrameLayout, changeFragment)
                ft.commit()
            }
            3 -> {
                binding.ivHomeNav.setColorFilter(getColor(R.color.normal_hover))
                binding.ivMyPageNav.setColorFilter(getColor(R.color.normal_hover))
                binding.ivExchangeNav.setColorFilter(getColor(R.color.normal_hover))
                binding.ivMoreNav.setColorFilter(getColor(R.color.white))

                val changeFragment = MoreFragment()
                ft.replace(R.id.fl_MainFrameLayout, changeFragment)
                ft.commit()
            }
        }
    }
}