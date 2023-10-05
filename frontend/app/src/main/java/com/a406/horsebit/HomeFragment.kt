package com.a406.horsebit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.a406.horsebit.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    val api = APIS.create()

    var tokenShowList: ArrayList<TokenShow> = ArrayList()

    lateinit var assetTableItemAdapter : AssetTableItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        binding = FragmentHomeBinding.bind(view)

        assetTableItemAdapter = AssetTableItemAdapter(tokenShowList)

        var searchViewTextListener: SearchView.OnQueryTextListener = object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                assetTableItemAdapter.filter.filter(s)
                return true
            }
        }

        binding.svAssert.setOnQueryTextListener(searchViewTextListener)

        binding.rvAssetTable.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)   // VERTICAL은 세로로
        binding.rvAssetTable.setHasFixedSize(true) // 성능 개선
        tokenShowList.clear()

        binding.ivWhole.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.black))
        changeShowType(0)

        binding.llvWhole.setOnClickListener {   // 전체
            binding.ivWhole.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.black))
            binding.ivInterest.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.font_gray))
            binding.ivHold.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.font_gray))
            tokenShowList.clear()
            changeShowType(0)
        }

        binding.llvInterest.setOnClickListener {    // 즐겨찾기
            binding.ivWhole.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.font_gray))
            binding.ivInterest.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.black))
            binding.ivHold.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.font_gray))
            tokenShowList.clear()
            changeShowType(1)
        }

        binding.llvHold.setOnClickListener {    // 보유
            binding.ivWhole.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.font_gray))
            binding.ivInterest.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.font_gray))
            binding.ivHold.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.black))
            tokenShowList.clear()
            changeShowType(2)
        }

        val sortingFlag: ArrayList<Int> = arrayListOf(0, 0, 0, 0)

        binding.llhAssetName.setOnClickListener {   // 자산명
            sortingFlag[0] = (sortingFlag[0] + 1) % 3
            when(sortingFlag[0]){
                0 -> {
                    binding.ivAssetNameSort.setImageResource(R.drawable.sort_not)
                }
                1->{
                    binding.ivAssetNameSort.setImageResource(R.drawable.sort_ascending)
                    assetTableItemAdapter.sortByAssetNameAscending()    // 자산명 내림차순 정렬
                }
                2->{
                    binding.ivAssetNameSort.setImageResource(R.drawable.sort_descending)
                    assetTableItemAdapter.sortByAssetNameDescending()    // 자산명 오름차순정렬

                }
            }
        }

        binding.llhCurrentPrice.setOnClickListener {    // 토큰 현재가
            sortingFlag[1] = (sortingFlag[1] + 1) % 3
            when(sortingFlag[1]) {
                0 -> {
                    binding.ivCurrentPriceSort.setImageResource(R.drawable.sort_not)
                }
                1 -> {
                    binding.ivCurrentPriceSort.setImageResource(R.drawable.sort_ascending)
                    assetTableItemAdapter.sortByAssetCurrentPriceAscending()    // 토큰 현재가 내림차순 정렬

                }
                2 -> {
                    binding.ivCurrentPriceSort.setImageResource(R.drawable.sort_descending)
                    assetTableItemAdapter.sortByAssetCurrentPriceDescending()   // 토큰 현재가 오름차순 정렬
                }
            }
        }

        binding.llhYesterdayPrice.setOnClickListener {  // 변동 추이
            sortingFlag[2] = (sortingFlag[2] + 1) % 3
            when(sortingFlag[2]) {
                0 -> {
                    binding.ivYesterdayPriceSort.setImageResource(R.drawable.sort_not)
                }
                1 -> {
                    binding.ivYesterdayPriceSort.setImageResource(R.drawable.sort_ascending)
                    assetTableItemAdapter.sortByPriceTrendAscending()   // 변동 추이 내림차순 정렬
                }
                2 -> {
                    binding.ivYesterdayPriceSort.setImageResource(R.drawable.sort_descending)
                    assetTableItemAdapter.sortByPriceTrendDescending()   // 변동 추이 오름차순 정렬
                }
            }
        }

        binding.llhTransactionPrice.setOnClickListener {    // 거래 금액
            sortingFlag[3] = (sortingFlag[3] + 1) % 3
            when(sortingFlag[3]) {
                0 -> {
                    binding.ivTransactionPriceSort.setImageResource(R.drawable.sort_not)
                }
                1 -> {
                    binding.ivTransactionPriceSort.setImageResource(R.drawable.sort_ascending)
                    assetTableItemAdapter.sortByVolumeAscending()   // 거래금액 내림차순 정렬
                }
                2 -> {
                    binding.ivTransactionPriceSort.setImageResource(R.drawable.sort_descending)
                    assetTableItemAdapter.sortByVolumeDescending()  // 거래금액 오름차순 정렬
                }
            }
        }

        val swipeHelperCallback = SwipeHelperCallback(assetTableItemAdapter).apply {
            // 스와이프한 뒤 고정시킬 위치 지정
            setClamp(resources.displayMetrics.widthPixels.toFloat() / 4)    // 1080 / 4 = 270
        }
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(binding.rvAssetTable)

        binding.rvAssetTable.setOnTouchListener { _, _ ->
            swipeHelperCallback.removePreviousClamp(binding.rvAssetTable)
            false
        }

        return view
    }

    private fun changeShowType(showType: Int) {
        when(showType) {
            0 -> {
                val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())  // import androidx.preference.PreferenceManager 인지 확인
                val token = pref.getString("token", "1")

                api.tokenList(authorization = "Bearer ${token}").enqueue(object: Callback<ArrayList<Token>> {
                    override fun onResponse(call: Call<ArrayList<Token>>, response: Response<ArrayList<Token>>) {
                        if(response.code() == 200) {    // 200 Success
                            Log.d("로그", "코인 목록 조회 (SSE): 200 Success")

                            val responseBody = response.body()

                            if(responseBody != null) {
                                for(token in responseBody) {
                                    val tokenShow = TokenShow(token.tokenNo, token.name, token.code, token.currentPrice, token.priceRateOfChange, token.volume, token.newFlag, false)
                                    tokenShowList.add(tokenShow)
                                }
                            }
                            assetTableItemAdapter = AssetTableItemAdapter(tokenShowList)
                            binding.rvAssetTable.adapter = assetTableItemAdapter
                        }
                        else if(response.code() == 400) {   // 400 Bad Request - Message에 누락 필드명 기입
                            Log.d("로그", "코인 목록 조회 (SSE): 400 Bad Request")
                        }
                        else if(response.code() == 401) {   // 401 Unauthorized - 인증 토큰값 무효
                            Log.d("로그", "코인 목록 조회 (SSE): 401 Unauthorized")
                        }
                        else if(response.code() == 404) {   // 404 Not Found
                            Log.d("로그", "코인 목록 조회 (SSE): 404 Not Found")
                        }
                    }
                    override fun onFailure(call: Call<ArrayList<Token>>, t: Throwable) {
                        Log.d("로그", "코인 목록 조회 (SSE): onFailure")
                    }
                })
            }
            1 -> {
                val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())  // import androidx.preference.PreferenceManager 인지 확인
                val token = pref.getString("SERVER_ACCESS_TOKEN", "1")

                api.favorites(authorization = "Bearer ${token}").enqueue(object: Callback<ArrayList<Token>> {
                    override fun onResponse(call: Call<ArrayList<Token>>, response: Response<ArrayList<Token>>) {
                        if(response.code() == 200) {    // 200 Success
                            Log.d("로그", "즐겨찾기 코인 목록 조회: 200 Success")

                            val responseBody = response.body()

                            if(responseBody != null) {
                                for(token in responseBody) {
                                    val tokenShow = TokenShow(token.tokenNo, token.name, token.code, token.currentPrice, token.priceRateOfChange, token.volume, token.newFlag, true)
                                    tokenShowList.add(tokenShow)
                                }
                            }
                            assetTableItemAdapter = AssetTableItemAdapter(tokenShowList)
                            binding.rvAssetTable.adapter = assetTableItemAdapter
                        }
                        else if(response.code() == 400) {   // 400 Bad Request - Message에 누락 필드명 기입
                            Log.d("로그", "즐겨찾기 코인 목록 조회: 400 Bad Request")
                        }
                        else if(response.code() == 401) {   // 401 Unauthorized - 인증 토큰값 무효
                            Log.d("로그", "즐겨찾기 코인 목록 조회: 401 Unauthorized")

                            Toast.makeText(context, "[관심]은 로그인 후 이용이 가능합니다", Toast.LENGTH_SHORT).show()
                            binding.ivWhole.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.black))
                            binding.ivInterest.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.font_gray))
                            binding.ivHold.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.font_gray))
                            tokenShowList.clear()
                            changeShowType(0)

                        }
                        else if(response.code() == 403) {   // 403 Forbidden - 권한 없음 (둘러보기 회원)
                            Log.d("로그", "즐겨찾기 코인 목록 조회: 403 Forbidden")
                        }
                        else if(response.code() == 404) {   // 404 Not Found
                            Log.d("로그", "즐겨찾기 코인 목록 조회: 404 Not Found")
                        }
                    }
                    override fun onFailure(call: Call<ArrayList<Token>>, t: Throwable) {
                        Log.d("로그", "즐겨찾기 코인 목록 조회: onFailure")
                    }
                })
            }
            2 -> {

                val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())  // import androidx.preference.PreferenceManager 인지 확인
                val token = pref.getString("SERVER_ACCESS_TOKEN", "1")

                api.holding(authorization = "Bearer ${token}").enqueue(object: Callback<ArrayList<Token>> {
                    override fun onResponse(call: Call<ArrayList<Token>>, response: Response<ArrayList<Token>>) {
                        if(response.code() == 200) {    // 200 Success
                            Log.d("로그", "보유 코인 목록 조회: 200 Success")

                            val responseBody = response.body()

                            if(responseBody != null) {
                                for(token in responseBody) {
                                    val tokenShow = TokenShow(token.tokenNo, token.name, token.code, token.currentPrice, token.priceRateOfChange, token.volume, token.newFlag, false)
                                    tokenShowList.add(tokenShow)
                                }
                            }
                            assetTableItemAdapter = AssetTableItemAdapter(tokenShowList)
                            binding.rvAssetTable.adapter = assetTableItemAdapter
                        }
                        else if(response.code() == 400) {   // 400 Bad Request - Message에 누락 필드명 기입
                            Log.d("로그", "보유 코인 목록 조회: 400 Bad Request")
                        }
                        else if(response.code() == 401) {   // 401 Unauthorized - 인증 토큰값 무효
                            Log.d("로그", "보유 코인 목록 조회: 401 Unauthorized")

                            Toast.makeText(context, "[보유]는 로그인 후 이용이 가능합니다", Toast.LENGTH_SHORT).show()
                            binding.ivWhole.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.black))
                            binding.ivInterest.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.font_gray))
                            binding.ivHold.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.font_gray))
                            tokenShowList.clear()
                            changeShowType(0)
                        }
                        else if(response.code() == 403) {   // 403 Forbidden - 권한 없음 (둘러보기 회원)
                            Log.d("로그", "보유 코인 목록 조회: 403 Forbidden")
                        }
                        else if(response.code() == 404) {   // 404 Not Found
                            Log.d("로그", "보유 코인 목록 조회: 404 Not Found")
                        }
                    }
                    override fun onFailure(call: Call<ArrayList<Token>>, t: Throwable) {
                        Log.d("로그", "보유 코인 목록 조회: onFailure")
                    }
                })
            }
        }
    }
}