package com.a406.horsebit

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_order_transaction_history_tab, container, false)

        binding = FragmentOrderTransactionHistoryTabBinding.bind(view)

        binding.rvTransactionTable.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)   // VERTICAL은 세로로
        binding.rvTransactionTable.setHasFixedSize(true) // 성능 개선

        changeColor(0)
        makeData(0)
        binding.rvTransactionTable.adapter = TransactionItemAdapter(transactionItemList)

        binding.tvNotConclusion.setOnClickListener {
            changeColor(0)
            makeData(0)
        }

        binding.tvConclusion.setOnClickListener {
            changeColor(1)
            makeData(1)
        }

        binding.rvTransactionTable.adapter = TransactionItemAdapter(transactionItemList)


        return view
    }

    private fun makeData(type: Int) {
        transactionItemList.clear()

        when(type){
            0 -> {
                val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())  // import androidx.preference.PreferenceManager 인지 확인
                val token: String = pref.getString("SERVER_ACCESS_TOKEN", "1") ?: "1"

                api.notConcluded(tokenNo = 1, authorization = "Bearer ${token}").enqueue(object: Callback<ArrayList<NotConcludedResponseBodyOrderModel>> {
                    override fun onResponse(call: Call<ArrayList<NotConcludedResponseBodyOrderModel>>, response: Response<ArrayList<NotConcludedResponseBodyOrderModel>>) {
                        if(response.code() == 200) {    // 200 Success
                            Log.d("로그", "미체결 내역 조회: 200 Success")

                            val responseBody = response.body()

                            if(responseBody != null) {
                                for(order in responseBody) {
                                    val transaction = TransactionShow(true, order.sellOrBuy, order.orderTime, order.tokenCode, order.price, order.quantity, order.remainQuantity)
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
                    }
                })
            }
            1 -> {
                val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())  // import androidx.preference.PreferenceManager 인지 확인
                val token: String = pref.getString("SERVER_ACCESS_TOKEN", "1") ?: "1"

                api.concluded(tokenNo = 1, authorization = "Bearer ${token}").enqueue(object: Callback<ArrayList<ConcludedResponseBodyOrderModel>> {
                    override fun onResponse(call: Call<ArrayList<ConcludedResponseBodyOrderModel>>, response: Response<ArrayList<ConcludedResponseBodyOrderModel>>) {
                        if(response.code() == 200) {    // 200 Success
                            Log.d("로그", "체결 내역 조회: 200 Success")

                            val responseBody = response.body()

                            if(responseBody != null) {
                                for(execution in responseBody) {
                                    val transaction = TransactionShow(false, execution.sellOrBuy, execution.timestamp, execution.tokenCode, execution.price, execution.quantity, execution.quantity)
                                    transactionItemList.add(transaction)
                                }
                            }
                            binding.rvTransactionTable.adapter = TransactionItemAdapter(transactionItemList)
                        }
                        else if(response.code() == 400) {   // 400 Bad Request - Message에 누락 필드명 기입
                            Log.d("로그", "체결 내역 조회: 400 Bad Request")
                        }
                        else if(response.code() == 401) {   // 401 Unauthorized - 인증 토큰값 무효
                            Log.d("로그", "체결 내역 조회: 401 Unauthorized")
                        }
                        else if(response.code() == 403) {   // 403 Forbidden - 권한 없음 (둘러보기 회원)
                            Log.d("로그", "체결 내역 조회: 403 Forbidden")
                        }
                        else if(response.code() == 404) {   // 404 Not Found
                            Log.d("로그", "체결 내역 조회: 404 Not Found")
                        }
                    }
                    override fun onFailure(call: Call<ArrayList<ConcludedResponseBodyOrderModel>>, t: Throwable) {
                        Log.d("로그", "체결 내역 조회: onFailure")
                    }
                })
            }
        }

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