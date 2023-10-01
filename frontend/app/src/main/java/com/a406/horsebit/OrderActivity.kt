package com.a406.horsebit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.a406.horsebit.databinding.ActivityOrderBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityOrderBinding.inflate(layoutInflater)
    }

    val api = APIS.create()
    var tokenNo: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.hsvTitle.isHorizontalScrollBarEnabled = false

        tokenNo = intent.getLongExtra("tokenNo", 0)
        Log.d("!!!!!!!!!", tokenNo.toString())

        api.tokenListDetail(tokenNo = tokenNo, authorization = "Bearer ${1}").enqueue(object: Callback<TokenListDetailResponseBodyModel> {
            override fun onResponse(call: Call<TokenListDetailResponseBodyModel>, response: Response<TokenListDetailResponseBodyModel>) {
                if(response.code() == 200) {    // 200 Success
                    Log.d("로그", "코인 상세 조회 (SSE): 200 Success")

                    val responseBody = response.body()

                    if(responseBody != null) {
                        binding.tvTitle.text = "${responseBody.name}(${responseBody.code})"
                        binding.tvCurrPrice.text = responseBody.currentPrice
                        binding.tvPercntageYesterday.text = "${responseBody.priceRateOfChange}%"
                        binding.tvRisePrice.text = responseBody.priceOfChange.toString()

                        binding.tvCurrPrice.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                        binding.tvPercntageYesterday.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                        binding.ivUpOrDown.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.black))
                        binding.tvRisePrice.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))

                        if(responseBody.priceRateOfChange < 0) {
                            binding.tvCurrPrice.setTextColor(ContextCompat.getColor(binding.root.context, R.color.blue))
                            binding.tvPercntageYesterday.setTextColor(ContextCompat.getColor(binding.root.context, R.color.blue))
                            binding.ivUpOrDown.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.blue))
                            binding.tvRisePrice.setTextColor(ContextCompat.getColor(binding.root.context, R.color.blue))
                            binding.ivUpOrDown.rotation = 180f

                        }
                        else if(responseBody.priceRateOfChange > 0) {
                            binding.tvCurrPrice.setTextColor(ContextCompat.getColor(binding.root.context, R.color.red))
                            binding.tvPercntageYesterday.setTextColor(ContextCompat.getColor(binding.root.context, R.color.red))
                            binding.ivUpOrDown.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.red))
                            binding.tvRisePrice.setTextColor(ContextCompat.getColor(binding.root.context, R.color.red))
                        }
                    }

                }
                else if(response.code() == 400) {   // 400 Bad Request - Message에 누락 필드명 기입
                    Log.d("로그", "코인 상세 조회 (SSE): 400 Bad Request")
                }
                else if(response.code() == 401) {   // 401 Unauthorized - 인증 토큰값 무효
                    Log.d("로그", "코인 상세 조회 (SSE): 401 Unauthorized")
                }
                else if(response.code() == 403) {   // 403 Forbidden - 권한 없음 (둘러보기 회원)
                    Log.d("로그", "코인 상세 조회 (SSE): 403 Forbidden")
                }
                else if(response.code() == 404) {   // 404 Not Found
                    Log.d("로그", "코인 상세 조회 (SSE): 404 Not Found")
                }
            }
            override fun onFailure(call: Call<TokenListDetailResponseBodyModel>, t: Throwable) {
                Log.d("로그", "코인 상세 조회 (SSE): onFailure")
            }
        })

        // 상단 탭 컨트롤
        binding.ivStockOrder.visibility = View.VISIBLE
        binding.ivStockChart.visibility = View.INVISIBLE
        binding.ivStockInformation.visibility = View.INVISIBLE
        binding.tvStockOrder.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
        binding.tvStockChart.setTextColor(ContextCompat.getColor(binding.root.context, R.color.font_gray))
        binding.tvStockInformation.setTextColor(ContextCompat.getColor(binding.root.context, R.color.font_gray))

        changeFrag(0)

        binding.flStockOrder.setOnClickListener {
            binding.ivStockOrder.visibility = View.VISIBLE
            binding.ivStockChart.visibility = View.INVISIBLE
            binding.ivStockInformation.visibility = View.INVISIBLE
            binding.tvStockOrder.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
            binding.tvStockChart.setTextColor(ContextCompat.getColor(binding.root.context, R.color.font_gray))
            binding.tvStockInformation.setTextColor(ContextCompat.getColor(binding.root.context, R.color.font_gray))
            changeFrag(0)
        }

        binding.flStockChart.setOnClickListener {
            binding.ivStockOrder.visibility = View.INVISIBLE
            binding.ivStockChart.visibility = View.VISIBLE
            binding.ivStockInformation.visibility = View.INVISIBLE
            binding.tvStockOrder.setTextColor(ContextCompat.getColor(binding.root.context, R.color.font_gray))
            binding.tvStockChart.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
            binding.tvStockInformation.setTextColor(ContextCompat.getColor(binding.root.context, R.color.font_gray))
            changeFrag(1)
        }

        binding.flStockInformation.setOnClickListener {
            binding.ivStockOrder.visibility = View.INVISIBLE
            binding.ivStockChart.visibility = View.INVISIBLE
            binding.ivStockInformation.visibility = View.VISIBLE
            binding.tvStockOrder.setTextColor(ContextCompat.getColor(binding.root.context, R.color.font_gray))
            binding.tvStockChart.setTextColor(ContextCompat.getColor(binding.root.context, R.color.font_gray))
            binding.tvStockInformation.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
            changeFrag(2)
        }
    }

    private fun changeFrag(fragNum: Int) {
        val ft = supportFragmentManager.beginTransaction()

        when(fragNum) {
            0 -> {
                //val bundle = Bundle()
                //bundle.putString("ticker", intent.getStringExtra("assetTicker").toString())

                val changeFragment = StockOrderFragment()
                //changeFragment.arguments = bundle
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