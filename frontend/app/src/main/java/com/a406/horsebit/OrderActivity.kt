package com.a406.horsebit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.a406.horsebit.databinding.ActivityOrderBinding

class OrderActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityOrderBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.tvTitle.text = "${intent.getStringExtra("assetName")}(${intent.getStringExtra("assetTicker")})"
        binding.tvCurrPrice.text = intent.getStringExtra("currentPrice")
        binding.tvPercntageYesterday.text = "${intent.getStringExtra("yesterdayPrice")}%"

        Log.d("로그", intent.getStringExtra("currentPrice").toString())

        changeFrag(0)

        binding.flStockOrder.setOnClickListener {
            changeFrag(0)
        }

        binding.flStockChart.setOnClickListener {
            changeFrag(1)
        }

        binding.flStockInformation.setOnClickListener {
            changeFrag(2)
        }
    }

    private fun changeFrag(fragNum: Int) {
        val ft = supportFragmentManager.beginTransaction()

        when(fragNum) {
            0 -> {
                val changeFragment = StockOrderFragment()
                ft.replace(R.id.fl_Order, changeFragment)
                ft.addToBackStack(null)
                ft.commit()
            }

            1 -> {
                val changeFragment = StockChartFragment()
                ft.replace(R.id.fl_Order, changeFragment)
                ft.addToBackStack(null)
                ft.commit()
            }

            2 -> {
                val changeFragment = StockInformationFragment()
                ft.replace(R.id.fl_Order, changeFragment)
                ft.addToBackStack(null)
                ft.commit()
            }
        }

    }
}