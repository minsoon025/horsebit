package com.a406.horsebit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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

        binding.ivStockOrder.visibility = View.VISIBLE
        binding.ivStockChart.visibility = View.INVISIBLE
        binding.ivStockInformation.visibility = View.INVISIBLE
        changeFrag(0)

        binding.flStockOrder.setOnClickListener {
            binding.ivStockOrder.visibility = View.VISIBLE
            binding.ivStockChart.visibility = View.INVISIBLE
            binding.ivStockInformation.visibility = View.INVISIBLE
            changeFrag(0)
        }

        binding.flStockChart.setOnClickListener {
            binding.ivStockOrder.visibility = View.INVISIBLE
            binding.ivStockChart.visibility = View.VISIBLE
            binding.ivStockInformation.visibility = View.INVISIBLE
            changeFrag(1)
        }

        binding.flStockInformation.setOnClickListener {
            binding.ivStockOrder.visibility = View.INVISIBLE
            binding.ivStockChart.visibility = View.INVISIBLE
            binding.ivStockInformation.visibility = View.VISIBLE
            changeFrag(2)
        }
    }

    private fun changeFrag(fragNum: Int) {
        val ft = supportFragmentManager.beginTransaction()

        when(fragNum) {
            0 -> {
                val changeFragment = StockOrderFragment()
                ft.replace(R.id.fl_Order, changeFragment)
                ft.commit()
            }

            1 -> {
                val changeFragment = StockChartFragment()
                ft.replace(R.id.fl_Order, changeFragment)
                ft.commit()
            }

            2 -> {
                val changeFragment = StockInformationFragment()
                ft.replace(R.id.fl_Order, changeFragment)
                ft.commit()
            }
        }

    }
}