package com.a406.horsebit

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.a406.horsebit.databinding.FragmentOrderTransactionHistoryTabBinding
import java.util.Date

class OrderTransactionHistoryTabFragment : Fragment() {

    private lateinit var binding : FragmentOrderTransactionHistoryTabBinding
    val api = APIS.create();

    val transactionItemList = arrayListOf(
        TransactionShow(false,'B', Date(), "TTT", 111, 111.3, 123.2),
        TransactionShow(false,'B', Date(), "TTT", 111, 111.3, 123.2),
        TransactionShow(false,'S', Date(), "TTT", 111, 111.3, 123.2),
        TransactionShow(false,'S', Date(), "TTT", 111, 111.3, 123.2),
    )

    val transactionItemList1 = arrayListOf(
        TransactionShow(true,'S', Date(), "AAA", 111, 111.3, 123.2),
        TransactionShow(true,'B', Date(), "BBB", 111, 111.3, 123.2),
        TransactionShow(true,'B', Date(), "CCC", 111, 111.3, 123.2),
        TransactionShow(true,'B', Date(), "DDD", 111, 111.3, 123.2),
        TransactionShow(true,'S', Date(), "EEEE", 111, 111.3, 123.2),
        TransactionShow(true,'S', Date(), "FFFF", 111, 111.3, 123.2),
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_order_transaction_history_tab, container, false)

        binding = FragmentOrderTransactionHistoryTabBinding.bind(view)

        changeColor(0)

        binding.tvNotConclusion.setOnClickListener {
            changeColor(0)

            

            /*
            api.acceptFollowRequest(followerId = followerId, followRequestId = followRequestId, Authorization = "Bearer ${serverAccessToken}", params = data).enqueue(object: Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if(response.code() == 201) {    // 성공
                            Log.d("로그", "팔로우 수락 201 Created")
                        }
                        else if(response.code() == 401) {   // AccessToken이 유효하지 않은 경우
                            Log.d("로그", "팔로우 수락 401 Unauthorized: AccessToken이 유효하지 않은 경우")
                        }
                        else if(response.code() == 400) {   // 해당하는 member나 followRequest가 존재하지 않는 경우
                            Log.d("로그", "팔로우 수락 400  Bad Request: 해당하는 member나 followRequest가 존재하지 않는 경우")
                        }
                    }
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d("로그", "팔로우 수락 onFailure")
                    }
                })


            */


            binding.rvTransactionTable.adapter = TransactionItemAdapter(transactionItemList)
        }

        binding.tvConclusion.setOnClickListener {
            changeColor(1)
            binding.rvTransactionTable.adapter = TransactionItemAdapter(transactionItemList1)
        }

        binding.rvTransactionTable.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)   // VERTICAL은 세로로
        binding.rvTransactionTable.setHasFixedSize(true) // 성능 개선

        binding.rvTransactionTable.adapter = TransactionItemAdapter(transactionItemList)


        return view
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