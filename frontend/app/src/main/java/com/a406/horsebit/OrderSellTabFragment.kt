package com.a406.horsebit

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a406.horsebit.databinding.FragmentOrderSellTabBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class OrderSellTabFragment : Fragment() {

    private lateinit var binding : FragmentOrderSellTabBinding
    val api = APIS.create()
    var tokenNo: Long = 0
    var code: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_order_sell_tab, container, false)

        binding = FragmentOrderSellTabBinding.bind(view)

        tokenNo = arguments?.getLong("tokenNo") ?: 0
        code = arguments?.getString("code") ?: ""

        Log.d("afsdsafdfdas", code)
        binding.tvOrderCanSellPrice.text = "0 ${code}"
        binding.tvOrderSellNumType.text = code

        binding.etOrderSellNum.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                calculateTotalPrice()
            }
        })

        binding.etOrderSellPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                calculateTotalPrice()
            }
        })

        binding.tvOrderSellCommit.setOnClickListener {

            val requestData = SellRequestRequestBodyModel(
                tokenNo,
                binding.etOrderSellNum.text.toString().toDouble(),
                binding.etOrderSellPrice.text.toString().toLong()
            )

            api.sellRequest(authorization = "Bearer ${1}" , requestData).enqueue(object: Callback<SellRequestResponseBodyModel> {
                override fun onResponse(call: Call<SellRequestResponseBodyModel>, response: Response<SellRequestResponseBodyModel>) {
                    if(response.code() == 200) {    // 200 Success
                        Log.d("로그", "매도 주문 요청: 200 Success")

                        val responseBody = response.body()

                    }
                    else if(response.code() == 201) {   // 201 Created
                        Log.d("로그", "매도 주문 요청: 201 Created")
                    }
                    else if(response.code() == 202) {   // 202 Accepted - 요청은 정상이나 아직 처리 중
                        Log.d("로그", "매도 주문 요청: 202 Accepted")
                    }
                    else if(response.code() == 400) {   // 400 Bad Request - Message에 누락 필드명 기입
                        Log.d("로그", "매도 주문 요청: 400 Bad Request")
                    }
                    else if(response.code() == 401) {   // 401 Unauthorized - 인증 토큰값 무효
                        Log.d("로그", "매도 주문 요청: 401 Unauthorized")
                    }
                    else if(response.code() == 403) {   // 403 Forbidden - 권한 없음 (둘러보기 회원)
                        Log.d("로그", "매도 주문 요청: 400 Bad Request")
                    }
                    else if(response.code() == 404) {   // 404 Not Found
                        Log.d("로그", "매도 주문 요청: 404 Not Found")
                    }
                }
                override fun onFailure(call: Call<SellRequestResponseBodyModel>, t: Throwable) {
                    Log.d("로그", "매도 주문 요청: onFailure")
                }
            })
        }

        return view
    }

    private fun calculateTotalPrice() {
        val orderNumText = binding.etOrderSellNum.text.toString()
        val orderPriceText = binding.etOrderSellPrice.text.toString()

        val orderNum = orderNumText.toDoubleOrNull() ?: 0.0
        val orderPrice = orderPriceText.toDoubleOrNull() ?: 0.0

        val totalPrice = orderNum * orderPrice

        val formattedTotalPrice = String.format(Locale.US, "%.0f", totalPrice)

        binding.tvOrderSellTotalPrice.text = formattedTotalPrice
    }
}