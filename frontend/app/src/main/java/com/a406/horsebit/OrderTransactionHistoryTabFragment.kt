package com.a406.horsebit

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.a406.horsebit.databinding.FragmentOrderTransactionHistoryTabBinding
import java.util.Date
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderTransactionHistoryTabFragment : Fragment() {

    private lateinit var binding : FragmentOrderTransactionHistoryTabBinding
    val api = APIS.create();

    var transactionItemList : ArrayList<TransactionShow> = ArrayList()

    val transactionItemList1 = arrayListOf(
        TransactionShow(true,"S", Date(), "AAA", 111, 111.3, 123.2),
        TransactionShow(true,"S", Date(), "BBB", 111, 111.3, 123.2),
        TransactionShow(true,"S", Date(), "CCC", 111, 111.3, 123.2),
        TransactionShow(true,"S", Date(), "DDD", 111, 111.3, 123.2),
        TransactionShow(true,"S", Date(), "EEEE", 111, 111.3, 123.2),
        TransactionShow(true,"S", Date(), "FFFF", 111, 111.3, 123.2),
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_order_transaction_history_tab, container, false)

        binding = FragmentOrderTransactionHistoryTabBinding.bind(view)

        binding.rvTransactionTable.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)   // VERTICAL은 세로로
        binding.rvTransactionTable.setHasFixedSize(true) // 성능 개선

        changeColor(0)
        makeData()
        binding.rvTransactionTable.adapter = TransactionItemAdapter(transactionItemList)

        binding.tvNotConclusion.setOnClickListener {
            changeColor(0)
            makeData()
        }

        binding.tvConclusion.setOnClickListener {
            changeColor(1)
            makeData()
            binding.rvTransactionTable.adapter = TransactionItemAdapter(transactionItemList1)
        }

        binding.rvTransactionTable.adapter = TransactionItemAdapter(transactionItemList)


        return view
    }

    private fun makeData() {
        transactionItemList.clear()

        val requestBodyData = NotConcludedRequestBodyModel(
            userNo = 1, //유저번호
            tokenNo = 1, //토큰번호
            startDate = Date(), //시작일자
            endDate =  Date(), //종료일자
        )
//requestBody = requestBodyData
        api.notConcluded(tokenNo = 1, accessToken = "1").enqueue(object: Callback<ArrayList<NotConcludedResponseBodyOrderModel>> {
            override fun onResponse(call: Call<ArrayList<NotConcludedResponseBodyOrderModel>>, response: Response<ArrayList<NotConcludedResponseBodyOrderModel>>) {
                if(response.code() == 200) {    // 200 Success
                    Log.d("로그", "미체결 내역 조회: 200 Success")

                    val responseBody = response.body()
                    Log.d("로그", responseBody.toString())

                    if(responseBody != null) {
                        for(order in responseBody) {
                            val transaction = TransactionShow(false, order.sellOrBuy, order.orderTime, order.tokenCode, order.price, order.quantity, order.remain_quantity)
                            transactionItemList.add(transaction)
                        }
                    }
                    binding.rvTransactionTable.adapter = TransactionItemAdapter(transactionItemList)

                }
                else if(response.code() == 400) {   // 400 Bad Request - Message에 누락 필드명 기입
                    Log.d("로그", "미체결 내역 조회: 400 Bad Request")
                }
                else if(response.code() == 401) {   // 401 Unauthorized - 인증 토큰값 무효
                    Log.d("로그", "미체결 내역 조회: 401 Unauthorized")
                }
                else if(response.code() == 403) {   // 403 Forbidden - 권한 없음 (둘러보기 회원)
                    Log.d("로그", "미체결 내역 조회: 403 Forbidden")
                }
                else if(response.code() == 404) {   // 404 Not Found
                    Log.d("로그", "미체결 내역 조회: 404 Not Found")
                }
            }
            override fun onFailure(call: Call<ArrayList<NotConcludedResponseBodyOrderModel>>, t: Throwable) {
                Log.d("로그", "미체결 내역 조회: onFailure")
                Log.d("asdfadsf", t.toString())
            }
        })
    }

    private fun changeColor(i: Int) {

        when(i) {
            0 -> {
                binding.tvNotConclusion.setTextColor(ContextCompat.getColor(binding.root.context, R.color.main_color))
                binding.tvNotConclusion.setBackgroundResource(R.drawable.edge_main_color)
                binding.tvConclusion.setTextColor(ContextCompat.getColor(binding.root.context, R.color.font_gray))
                binding.tvConclusion.setBackgroundResource(R.drawable.edge)
            }
            1 -> {
                binding.tvNotConclusion.setTextColor(ContextCompat.getColor(binding.root.context, R.color.font_gray))
                binding.tvNotConclusion.setBackgroundResource(R.drawable.edge)
                binding.tvConclusion.setTextColor(ContextCompat.getColor(binding.root.context, R.color.main_color))
                binding.tvConclusion.setBackgroundResource(R.drawable.edge_main_color)
            }
        }
    }
}