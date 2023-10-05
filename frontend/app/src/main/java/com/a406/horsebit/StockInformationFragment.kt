package com.a406.horsebit

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a406.horsebit.databinding.FragmentStockInformationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Date



class StockInformationFragment : Fragment() {

    private lateinit var binding: FragmentStockInformationBinding
    val api = APIS.create()

    var tokenNo: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stock_information, container, false)

        binding = FragmentStockInformationBinding.bind(view)

        tokenNo = arguments?.getLong("tokenNo") ?: 0

        api.tokenInfo(tokenNo = tokenNo).enqueue(object: Callback<TokenInfoResponseBodyModel> {
            override fun onResponse(call: Call<TokenInfoResponseBodyModel>, response: Response<TokenInfoResponseBodyModel>) {
                if(response.code() == 200) {    // 200 Success
                    Log.d("로그", "코인 경주마 정보 조회: 200 Success")

                    val responseBody = response.body()

                    if(responseBody != null) {
                        val resourceName = "pic_$tokenNo"
                        val resourceId = requireContext().resources.getIdentifier(resourceName, "drawable", requireContext().packageName)

                        if (resourceId != 0) {
                            binding.ivMoreProfile.setImageResource(resourceId)
                        }
                        binding.tvStockInformationHorseName.text = "${responseBody.hrName}(${responseBody.code})"
                        binding.tvStockInformationContent.text = "${responseBody.content}"
                        binding.tvStockInformationOwNameRight.text = "${responseBody.owName}"
                        binding.tvStockInformationBirthPlaceRight.text = "${responseBody.birthPlace}"
                        binding.tvStockInformationFatherHrNameRight.text = "${responseBody.fatherHrName}"
                        binding.tvStockInformationMotherHrNameRight.text = "${responseBody.motherHrName}"
                        binding.tvStockInformationRaceRankRight.text = "${responseBody.raceRank}"

                        val publishDate: Date = responseBody.publishDate
                        val calendar = Calendar.getInstance()
                        calendar.time = publishDate
                        val year = calendar.get(Calendar.YEAR)
                        val month = calendar.get(Calendar.MONTH) + 1
                        val date = calendar.get(Calendar.DATE)

                        binding.tvStockInformationPublishDateRight.text = "${year}.${month}.${date}"
                        binding.tvStockInformationSupplyRight.text = "${responseBody.supply} ${responseBody.code}"
                        binding.tvStockInformationMarketCapRight.text = "${responseBody.marketCap} KRW"
                    }

                }
                else if(response.code() == 400) {   // 400 Bad Request - Message에 누락 필드명 기입
                    Log.d("로그", "코인 경주마 정보 조회: 400 Bad Request")
                }
                else if(response.code() == 401) {   // 401 Unauthorized - 인증 토큰값 무효
                    Log.d("로그", "코인 경주마 정보 조회: 401 Unauthorized")
                }
                else if(response.code() == 404) {   // 404 Not Found
                    Log.d("로그", "코인 경주마 정보 조회: 404 Not Found")
                }
            }
            override fun onFailure(call: Call<TokenInfoResponseBodyModel>, t: Throwable) {
                Log.d("로그", "코인 경주마 정보 조회: onFailure")
            }
        })
        return view
    }
}