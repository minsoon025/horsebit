package com.a406.horsebit

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.a406.horsebit.databinding.FragmentStockOrderBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StockOrderFragment : Fragment() {

    private lateinit var binding: FragmentStockOrderBinding
    val api = APIS.create()

    val orderList :ArrayList<Order> = ArrayList()

    var tokenNo: Long = 0
    var code: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stock_order, container, false)

        binding = FragmentStockOrderBinding.bind(view)

        binding.rvOrder.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvOrder.setHasFixedSize(true)

        tokenNo = arguments?.getLong("tokenNo") ?: 0
        code = arguments?.getString("code") ?: ""

        api.coinOrderSituation(tokenNo = tokenNo).enqueue(object: Callback<ArrayList<Order>> {
            override fun onResponse(call: Call<ArrayList<Order>>, response: Response<ArrayList<Order>>) {
                if(response.code() == 200) {    // 200 Success
                    Log.d("로그", "코인 주문 현황 상세 조회 (SSE): 200 Success")

                    val responseBody = response.body()

                    var maxVolume: Float = 0f
                    if(responseBody != null) {

                        for(order in responseBody) {
                            orderList.add(order)
                            if(maxVolume < order.volume.toFloat()) maxVolume = order.volume.toFloat()
                        }
                    }
                    binding.rvOrder.adapter = OrderItemAdapter(orderList, maxVolume)
                }
                else if(response.code() == 400) {   // 400 Bad Request - Message에 누락 필드명 기입
                    Log.d("로그", "코인 주문 현황 상세 조회 (SSE): 400 Bad Request")
                }
                else if(response.code() == 401) {   // 401 Unauthorized - 인증 토큰값 무효
                    Log.d("로그", "코인 주문 현황 상세 조회 (SSE): 401 Unauthorized")
                }
                else if(response.code() == 404) {   // 404 Not Found
                    Log.d("로그", "코인 주문 현황 상세 조회 (SSE): 404 Not Found")
                }
            }
            override fun onFailure(call: Call<ArrayList<Order>>, t: Throwable) {
                Log.d("로그", "코인 주문 현황 상세 조회 (SSE): onFailure")
            }
        })

        // 탭 헤더 색상 변경 시작
        binding.tvOrderBuyTabHeader.setTextColor(ContextCompat.getColor(binding.root.context, R.color.red))
        binding.tvOrderBuyTabHeader.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.white))
        binding.tvOrderSellTabHeader.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
        binding.tvOrderSellTabHeader.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.gray))
        binding.tvOrderTransactionHistoryTabHeader.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
        binding.tvOrderTransactionHistoryTabHeader.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.gray))
        // 텝 헤더 색상 변경 끝

        changFrag(0)

        binding.tvOrderBuyTabHeader.setOnClickListener {
            binding.tvOrderBuyTabHeader.setTextColor(ContextCompat.getColor(binding.root.context, R.color.red))
            binding.tvOrderBuyTabHeader.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.white))
            binding.tvOrderSellTabHeader.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
            binding.tvOrderSellTabHeader.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.gray))
            binding.tvOrderTransactionHistoryTabHeader.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
            binding.tvOrderTransactionHistoryTabHeader.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.gray))
            changFrag(0)
        }

        binding.tvOrderSellTabHeader.setOnClickListener {
            binding.tvOrderBuyTabHeader.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
            binding.tvOrderBuyTabHeader.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.gray))
            binding.tvOrderSellTabHeader.setTextColor(ContextCompat.getColor(binding.root.context, R.color.blue))
            binding.tvOrderSellTabHeader.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.white))
            binding.tvOrderTransactionHistoryTabHeader.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
            binding.tvOrderTransactionHistoryTabHeader.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.gray))
            changFrag(1)
        }

        binding.tvOrderTransactionHistoryTabHeader.setOnClickListener {
            binding.tvOrderBuyTabHeader.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
            binding.tvOrderBuyTabHeader.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.gray))
            binding.tvOrderSellTabHeader.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
            binding.tvOrderSellTabHeader.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.gray))
            binding.tvOrderTransactionHistoryTabHeader.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
            binding.tvOrderTransactionHistoryTabHeader.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.white))
            changFrag(2)
        }

        return view
    }

    private fun changFrag(fragNum: Int) {
        val ft = requireActivity().supportFragmentManager.beginTransaction()

        when(fragNum){
            0->{
                val bundle = Bundle()
                bundle.putLong("tokenNo", tokenNo)
                bundle.putString("code",code)

                val changeFragment = OrderBuyTabFragment()
                changeFragment.arguments = bundle
                ft.replace(R.id.fl_OrderTab, changeFragment)
                ft.commit()
            }
            1->{
                val bundle = Bundle()
                bundle.putLong("tokenNo", tokenNo)
                bundle.putString("code",code)

                val changeFragment = OrderSellTabFragment()
                changeFragment.arguments = bundle
                ft.replace(R.id.fl_OrderTab, changeFragment)
                ft.commit()
            }
            2->{
                val changeFragment = OrderTransactionHistoryTabFragment()

                ft.replace(R.id.fl_OrderTab, changeFragment)
                ft.commit()
            }
        }
    }
}