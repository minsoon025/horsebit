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
//        Order(1343, 1000, 0.03),
//        Order(2723, 291, 0.07),
//        Order(3091, 845, 0.18),
//        Order(4558, 968, 0.27),
//        Order(5300, 833, 0.41),
//        Order(6115, 587, 0.53),
//        Order(6787, 537, 0.62),
//        Order(7539, 313, 0.76),
//        Order(8975, 687, 0.89),
//        Order(9901, 797, 0.97),
//        Order(2092, 633, -0.98),
//        Order(3894, 9, -0.92),
//        Order(4640, 419, -0.81),
//        Order(5774, 679, -0.75),
//        Order(6309, 489, -0.68),
//        Order(7372, 822, -0.65),
//        Order(7915, 734, -0.52),
//        Order(8399, 210, -0.49),
//        Order(9106, 837, -0.47),
//        Order(9997, 286, -0.42)
//    )

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

                    if(responseBody != null) {
                        for(order in responseBody) {
                            orderList.add(order)
                        }
                    }
                    binding.rvOrder.adapter = OrderItemAdapter(orderList)
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
                bundle.putString("ticker", arguments?.getString("ticker").toString())

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