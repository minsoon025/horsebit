package com.a406.horsebit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.a406.horsebit.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    val assetItemList = arrayListOf(
        AssetItem(R.drawable.baseline_dashboard_customize_24, "보겸코인", "YBC", false, 22.0, -0.45, 44.3),
        AssetItem(R.drawable.baseline_dashboard_customize_24, "임서희코인", "BTC", true, 2.4, 12.4, 44.33),
        AssetItem(R.drawable.baseline_dashboard_customize_24, "팀장님은발표", "BBB", true, 2222.4, -2.2, 4.3),
        AssetItem(R.drawable.baseline_dashboard_customize_24, "군것질화영코인", "AAA", false, 4422.4, 33.2, 434.3),
        AssetItem(R.drawable.baseline_dashboard_customize_24, "할어버지코인", "TTT", true, 5555.4, -22.3, 444.3),
        AssetItem(R.drawable.baseline_dashboard_customize_24, "버섯코인", "DDD", false, 2312.4, 22.2, 4.3),
        AssetItem(R.drawable.baseline_dashboard_customize_24, "이마에점", "AAA", true, 22233.4, 43.2, 43.3),
        AssetItem(R.drawable.baseline_dashboard_customize_24, "어제는2개", "EEE", true, 22444.4, 1.1, 1.3),
        AssetItem(R.drawable.baseline_dashboard_customize_24, "오늘은3개", "RRRR", false, 5555.4, -0.2, 14.3),
        AssetItem(R.drawable.baseline_dashboard_customize_24, "그럼내일은?", "YONG", true, 2266.4, -0.22, 4444.3),
        AssetItem(R.drawable.baseline_dashboard_customize_24, "당연히5개", "YONTTG", false, 7777.4, -0.23, 4444.3),
        AssetItem(R.drawable.baseline_dashboard_customize_24, "놀랬지?", "YY", false, 8888.4, 0.01, 4444.3),
        AssetItem(R.drawable.baseline_dashboard_customize_24, "구라얌", "HHH", false, 99999.4, 0.00, 4444.3),
        AssetItem(R.drawable.baseline_dashboard_customize_24, "짱구는", "JJJ", false, 34343.4, 0.22, 4444.3),
        AssetItem(R.drawable.baseline_dashboard_customize_24, "엉덩이로", "JJJJ", false, 367643.4, 3.2, 4444.3),
        AssetItem(R.drawable.baseline_dashboard_customize_24, "춤을춰", "EEE", false, 34543.4, 1.2, 4444.3),
        AssetItem(R.drawable.baseline_dashboard_customize_24, "어허잇", "PPP", false, 34534.4, -1.2, 4444.3),
        AssetItem(R.drawable.baseline_dashboard_customize_24, "헤헤", "LLL", false, 32432.4, -2.2, 4444.3),
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        binding = FragmentHomeBinding.bind(view)

        binding.rvAssetTable.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)   // VERTICAL은 세로로
        binding.rvAssetTable.setHasFixedSize(true) // 성능 개선

        binding.rvAssetTable.adapter = AssetTableItemAdapter(assetItemList)

        binding.ivAssetNameUp.setOnClickListener {  // 자산명 내림차순 정렬
            assetItemList.sortByDescending { it.assetName }
            binding.rvAssetTable.adapter = AssetTableItemAdapter(assetItemList)
        }

        binding.ivAssetNameDown.setOnClickListener {    // 자산명 오름차순 정렬
            assetItemList.sortBy { it.assetName }
            binding.rvAssetTable.adapter = AssetTableItemAdapter(assetItemList)
        }

        binding.ivCurrentPriceUp.setOnClickListener { // 현재가 오름차순 정렬
            assetItemList.sortBy { it.currentPrice }
            binding.rvAssetTable.adapter = AssetTableItemAdapter(assetItemList)
        }

        binding.ivCurrentPriceDown.setOnClickListener { // 현재가 내림차순 정렬
            assetItemList.sortByDescending { it.currentPrice }
            binding.rvAssetTable.adapter = AssetTableItemAdapter(assetItemList)
        }

        binding.ivYesterdayPriceUp.setOnClickListener {
            assetItemList.sortBy { it.yesterdayPrice }
            binding.rvAssetTable.adapter = AssetTableItemAdapter(assetItemList)
        }

        binding.ivYesterdayPriceDown.setOnClickListener {
            assetItemList.sortByDescending { it.yesterdayPrice }
            binding.rvAssetTable.adapter = AssetTableItemAdapter(assetItemList)
        }

        binding.ivTransactionPriceUp.setOnClickListener {
            assetItemList.sortBy { it.transactionPrice }
            binding.rvAssetTable.adapter = AssetTableItemAdapter(assetItemList)
        }

        binding.ivTransactionPriceDown.setOnClickListener {
            assetItemList.sortByDescending { it.transactionPrice }
            binding.rvAssetTable.adapter = AssetTableItemAdapter(assetItemList)
        }

        binding.btnTmp.setOnClickListener {
            val intent = Intent(binding.root.context, LoginMainActivity::class.java)
            binding.root.context.startActivity(intent)
        }

        return view
    }
}