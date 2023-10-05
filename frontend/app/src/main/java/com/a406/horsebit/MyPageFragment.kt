package com.a406.horsebit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.a406.horsebit.databinding.FragmentMyPageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyPageFragment : Fragment() {

    private lateinit var binding: FragmentMyPageBinding
    private lateinit var myPageCoinItemAdapter: MyPageCoinItemAdapter

    private val api = APIS.create();

//    var totalAssetList: MyTotalAssetModel = ArrayList()
    var myassetList: ArrayList<MyAssetResponseBodyModel> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        // 데이터 바인딩 초기화
        val view = inflater.inflate(R.layout.fragment_my_page, container, false)
        binding = FragmentMyPageBinding.bind(view)

        // 리사이클러뷰 초기화
        val recyclerView = binding.rvMyCoinCard
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)


        // 어댑터 설정 (여기서 dataList는 데이터 리스트로 대체해야 합니다)
        val dataList = ArrayList<MyAssetResponseBodyModel>() // 실제 데이터 리스트로 대체
        myPageCoinItemAdapter = MyPageCoinItemAdapter(dataList)
        recyclerView.adapter = myPageCoinItemAdapter

        myPageCoinItemAdapter = MyPageCoinItemAdapter(myassetList)
        binding.rvMyCoinCard.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvMyCoinCard.setHasFixedSize(true)

        myassetList.clear()


        val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())  // import androidx.preference.PreferenceManager 인지 확인
        val token = pref.getString("SERVER_ACCESS_TOKEN", "1")

        Log.d("엑세스토큰입니다", token.toString())
        api.MyCoins(authorization = "Bearer ${token}").enqueue(object: Callback<ArrayList<MyAssetResponseBodyModel>> {
            override fun onResponse(call: Call<ArrayList<MyAssetResponseBodyModel>>, response: Response<ArrayList<MyAssetResponseBodyModel>>) {
                if(response.code() == 200) {    // 200 Success
                    Log.d("로그", "보유 마패 전체 조회: 200 Success")

                    val responseBody = response.body()



                    if (responseBody != null) {
                        for (coin in responseBody) {


                            myassetList.add(coin)

                        }
                        myPageCoinItemAdapter.notifyDataSetChanged()
                    } else {
                        showNoDataMessage()
                    }

                    myPageCoinItemAdapter = MyPageCoinItemAdapter(myassetList)

                    binding.rvMyCoinCard.adapter = myPageCoinItemAdapter



                }
                else if(response.code() == 400) {   // 400 Bad Request - Message에 누락 필드명 기입
                    Log.d("로그", "보유 마패 전체 조회: 400 Bad Request")
                }
                else if(response.code() == 401) {   // 401 Unauthorized - 인증 토큰값 무효
                    Log.d("로그", "보유 마패 전체 조회: 401 Unauthorized")
                }
                else if(response.code() == 403) {
                    Log.d("로그", "보유 마패 전체 조회: 403 Forbidden")
                }
                else if(response.code() == 404) {   // 404 Not Found
                    Log.d("로그", "보유 마패 전체 조회: 404 Not Found")
                }
            }
            override fun onFailure(call: Call<ArrayList<MyAssetResponseBodyModel>>, t: Throwable) {
                Log.d("로그", "보유 마패 전체 조회: onFailure")
            }
        })

        api.MyTotalAsset(authorization = "Bearer ${token}").enqueue(object : Callback<MyTotalAssetResponseBodyModel> {
            override fun onResponse(call: Call<MyTotalAssetResponseBodyModel>, response: Response<MyTotalAssetResponseBodyModel>) {
                if (response.code() == 200) {    // 200 Success
                    Log.d("로그", "내 자산 전체 조회: 200 Success")

                    val responseBody = response.body()



                    if (responseBody != null) {
                        // totalAssetList에서 총 자산 값 가져오기
                        val totalAssetValue = responseBody.totalAsset
                        val cashBalanceValue = responseBody.cashBalance
                        val totalPurchaseValue = responseBody.totalPurchase
                        val totalEvaluationValue = responseBody.totalEvaluation
                        val profitOrLossValue = responseBody.profitOrLoss
                        val returnRateValue = responseBody.returnRate

                        // TextView에 총 자산 값 설정
                        binding.tvMyTotalCoin.text = totalAssetValue.toString()
                        binding.tvMyOwnKRW.text = cashBalanceValue.toString()
                        binding.tvMyCoinTotalBuy.text = totalPurchaseValue.toString()
                        binding.tvMyCoinTotalVal.text = totalEvaluationValue.toString()
                        binding.tvMyPlusMinus.text = profitOrLossValue.toString()
                        binding.tvMyCoinRate.text = returnRateValue.toString()
                    }
                }
                else if(response.code() == 400) {   // 400 Bad Request - Message에 누락 필드명 기입
                    Log.d("로그", "내 자산 전체 조회: 400 Bad Request")
                }
                else if(response.code() == 401) {   // 401 Unauthorized - 인증 토큰값 무효
                    Log.d("로그", "내 자산 전체 조회: 401 Unauthorized")
                }
                else if(response.code() == 403) {
                    Log.d("로그", "내 자산 전체 조회: 403 Forbidden")
                }
                else if(response.code() == 404) {   // 404 Not Found
                    Log.d("로그", "내 자산 전체 조회: 404 Not Found")
                }
            }
            override fun onFailure(call: Call<MyTotalAssetResponseBodyModel>, t: Throwable) {
                Log.d("로그", "내 자산 전체 조회: onFailure")

            }
        })


        return view


    }


    private fun showNoDataMessage() {
        // 데이터가 없을 때 특정 메시지를 표시하는 로직을 여기에 구현합니다.
        // 예를 들어, TextView에 메시지를 설정하거나 다이얼로그를 표시할 수 있습니다.
        // 예제로 TextView에 메시지 설정하는 방법을 보여드리겠습니다.

        val noDataMessage = "보유한 마패가 없습니다." // 표시할 메시지를 지정
//        binding.tvViewNoData.text = noDataMessage
//        binding.tvViewNoData.visibility = View.VISIBLE // TextView를 화면에 표시
    }


    private fun saveData() {
        val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val edit = pref.edit()

        // 토큰 정보를 저장
        val token = "your_token_here" // 실제 토큰 정보로 대체
        edit.putString("[키]", "[데이터]")


        // 닉네임 정보도 동일하게 저장할 수 있습니다.
        val nickname = "your_nickname_here" // 실제 닉네임 정보로 대체
        edit.putString("[키]", "[데이터]")


        edit.apply()    // 적용

    }
}