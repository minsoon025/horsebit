package com.a406.horsebit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.a406.horsebit.databinding.FragmentMyPageBinding


class MyPageFragment : Fragment() {

    private lateinit var binding: FragmentMyPageBinding

    val myassetItemList = arrayListOf(
        MyAsset(R.drawable.horsebit_logo,"BTC", "클레멘타인", "100", "30.3"),
        MyAsset(R.drawable.horsebit_logo,"BTC", "클레멘타인", "100", "30.3"),
        MyAsset(R.drawable.horsebit_logo,"BTC", "클레멘타인", "100", "30.3"),
        MyAsset(R.drawable.horsebit_logo,"BTC", "클레멘타인", "100", "30.3"),
        MyAsset(R.drawable.horsebit_logo,"BTC", "클레멘타인", "100", "30.3"),


    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // 데이터 바인딩 초기화
        val view = inflater.inflate(R.layout.fragment_my_page, container, false)


        binding = FragmentMyPageBinding.bind(view)

        binding.rvMyCoinCard.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)   // VERTICAL은 세로로
        binding.rvMyCoinCard.setHasFixedSize(true) // 성능 개선

        binding.rvMyCoinCard.adapter = MyPageCoinItemAdapter(myassetItemList)


        return view
    }
}