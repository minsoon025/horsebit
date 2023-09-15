package com.a406.horsebit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a406.horsebit.databinding.FragmentStockInformationBinding
import java.util.Date



class StockInformationFragment : Fragment() {

    private lateinit var binding: FragmentStockInformationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stock_information, container, false)

        binding = FragmentStockInformationBinding.bind(view)

        binding.tvStockInformationHorseName.text = "임서희(BABO)"
        binding.tvStockInformationContent.text = "임서히는 1999년애 태어난 99 대장말이며 , 가끔은 시크하다는 얘기를 듣지만 속은 그렇지 않은 말으로 기분이 좋으면 매우 잘 달립니다."
        binding.tvStockInformationOwNameRight.text = "나가모토 사토시"
        binding.tvStockInformationBirthPlaceRight.text = "경기도 원주시"
        binding.tvStockInformationFatherHrNameRight.text = "블루오션"
        binding.tvStockInformationMotherHrNameRight.text = "레드오션"
        binding.tvStockInformationRaceRankRight.text = "국5"
        binding.tvStockInformationPublishDateRight.text = "${Date().year.toString()}.${(Date().month + 1).toString()}"
        binding.tvStockInformationSupplyRight.text = "100"
        binding.tvStockInformationMarketCapRight.text = "22 억 원"
        binding.tvStockInformationYearMaxRight.text = "333,232 원"
        binding.tvStockInformationYearMinRight.text = "332,332 원"

        return view
    }
}