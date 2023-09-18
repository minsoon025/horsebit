package com.a406.horsebit

import ExchangeFragment
import android.graphics.ColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.a406.horsebit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
                ft.addToBackStack(null)
                ft.commit()
            }
            1 -> {
                binding.ivHomeNav.setColorFilter(getColor(R.color.normal_hover))
                binding.ivMyPageNav.setColorFilter(getColor(R.color.white))
                binding.ivExchangeNav.setColorFilter(getColor(R.color.normal_hover))
                binding.ivMoreNav.setColorFilter(getColor(R.color.normal_hover))

                val changeFragment = MyPageFragment()
                ft.replace(R.id.fl_MainFrameLayout, changeFragment)
                ft.addToBackStack(null)
                ft.commit()
            }
            2 -> {
                binding.ivHomeNav.setColorFilter(getColor(R.color.normal_hover))
                binding.ivMyPageNav.setColorFilter(getColor(R.color.normal_hover))
                binding.ivExchangeNav.setColorFilter(getColor(R.color.white))
                binding.ivMoreNav.setColorFilter(getColor(R.color.normal_hover))

                val changeFragment = ExchangeFragment()
                ft.replace(R.id.fl_MainFrameLayout, changeFragment)
                ft.addToBackStack(null)
                ft.commit()
            }
            3 -> {
                binding.ivHomeNav.setColorFilter(getColor(R.color.normal_hover))
                binding.ivMyPageNav.setColorFilter(getColor(R.color.normal_hover))
                binding.ivExchangeNav.setColorFilter(getColor(R.color.normal_hover))
                binding.ivMoreNav.setColorFilter(getColor(R.color.white))

                val changeFragment = MoreFragment()
                ft.replace(R.id.fl_MainFrameLayout, changeFragment)
                ft.addToBackStack(null)
                ft.commit()
            }
        }
    }
}