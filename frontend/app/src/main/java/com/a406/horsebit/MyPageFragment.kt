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

    var totalAssetList: ArrayList<MyTotalAssetModel> = ArrayList()
    var myassetList: ArrayList<MyAssetModel> = ArrayList()





    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        // 데이터 바인딩 초기화
        val view = inflater.inflate(R.layout.fragment_my_page, container, false)

        // 리사이클러뷰 초기화
        val recyclerView = binding.rvMyCoinCard
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)

        // 어댑터 설정 (여기서 dataList는 데이터 리스트로 대체해야 합니다)
        val dataList = ArrayList<MyAssetModel>() // 실제 데이터 리스트로 대체
        myPageCoinItemAdapter = MyPageCoinItemAdapter(dataList)
        recyclerView.adapter = myPageCoinItemAdapter

        binding = FragmentMyPageBinding.bind(view)
        myPageCoinItemAdapter = MyPageCoinItemAdapter(myassetList)
        binding.rvMyCoinCard.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvMyCoinCard.setHasFixedSize(true)

        myassetList.clear()
        totalAssetList.clear()

        //val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())  // import androidx.preference.PreferenceManager 인지 확인
        //val token = pref.getString("token", "1")

        api.MyCoins(authorization = "Bearer ${1}").enqueue(object: Callback<ArrayList<MyAssetModel>> {
            override fun onResponse(call: Call<ArrayList<MyAssetModel>>, response: Response<ArrayList<MyAssetModel>>) {
                if(response.code() == 200) {    // 200 Success
                    Log.d("로그", "보유 마패 전체 조회: 200 Success")

                    val responseBody = response.body()

                    Log.d("dddd", responseBody.toString())

                    if (responseBody != null) {
                        for (coin in responseBody) {
                            myassetList.add(coin)
                        }
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
            override fun onFailure(call: Call<ArrayList<MyAssetModel>>, t: Throwable) {
                Log.d("로그", "보유 마패 전체 조회: onFailure")
                Log.d("ddddd", t.toString())
            }
        })

        api.MyTotalAsset(authorization = "Bearer ${1}").enqueue(object: Callback<ArrayList<MyTotalAssetModel>> {
            override fun onResponse(call: Call<ArrayList<MyTotalAssetModel>>, response: Response<ArrayList<MyTotalAssetModel>>) {
                if(response.code() == 200) {    // 200 Success
                    Log.d("로그", "내 자산 전체 조회: 200 Success")

                    val responseBody = response.body()

                    Log.d("dddd", responseBody.toString())

                    if (responseBody != null) {
                        for (total in responseBody) {
                            totalAssetList.add(total)
                        }
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
            override fun onFailure(call: Call<ArrayList<MyTotalAssetModel>>, t: Throwable) {
                Log.d("로그", "내 자산 전체 조회: onFailure")
                Log.d("ddddd", t.toString())
            }
        })


        return view


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