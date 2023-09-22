package com.a406.horsebit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.a406.horsebit.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale.filter

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    val api = APIS.create();

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
        api.tokenList(authorization = "Bearer ${1}").enqueue(object: Callback<ArrayList<Token>> {
            override fun onResponse(call: Call<ArrayList<Token>>, response: Response<ArrayList<Token>>) {
                if(response.code() == 200) {    // 200 Success
                    Log.d("로그", "코인 목록 조회 (SSE): 200 Success")

                    val responseBody = response.body()

                    Log.d("dddd", responseBody.toString())

                    if(responseBody != null) {
                        for(token in responseBody) {
                            val tokenShow = TokenShow(1, token.name, token.code, token.currentPrice, token.priceTrend, token.volume, token.newFlag)
                            tokenShowList.add(tokenShow)
                        }
                    }
                    // binding.rvAssetTable.adapter = AssetTableItemAdapter(tokenShowList)
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

        binding.ivAssetNameUp.setOnClickListener {  // 자산명 내림차순 정렬
            assetTableItemAdapter.sortByAssetNameDescending()
        }

        binding.ivAssetNameDown.setOnClickListener {    // 자산명 오름차순 정렬
            assetTableItemAdapter.sortByAssetNameAscending()
        }

        binding.ivCurrentPriceUp.setOnClickListener { // 현재가 오름차순 정렬
            tokenShowList.sortBy { it.currentPrice }
            binding.rvAssetTable.adapter = AssetTableItemAdapter(tokenShowList)
        }

        binding.ivCurrentPriceDown.setOnClickListener { // 현재가 내림차순 정렬
            tokenShowList.sortByDescending { it.currentPrice }
            binding.rvAssetTable.adapter = AssetTableItemAdapter(tokenShowList)
        }

        binding.ivYesterdayPriceUp.setOnClickListener {
            tokenShowList.sortBy { it.priceTrend }
            binding.rvAssetTable.adapter = AssetTableItemAdapter(tokenShowList)
        }

        binding.ivYesterdayPriceDown.setOnClickListener {
            tokenShowList.sortByDescending { it.priceTrend }
            binding.rvAssetTable.adapter = AssetTableItemAdapter(tokenShowList)
        }

        binding.ivTransactionPriceUp.setOnClickListener {
            tokenShowList.sortBy { it.volume }
            binding.rvAssetTable.adapter = AssetTableItemAdapter(tokenShowList)
        }

        binding.ivTransactionPriceDown.setOnClickListener {
            tokenShowList.sortByDescending { it.volume }
            binding.rvAssetTable.adapter = AssetTableItemAdapter(tokenShowList)
        }

        binding.btnTmp.setOnClickListener {
            val intent = Intent(binding.root.context, LoginMainActivity::class.java)
            binding.root.context.startActivity(intent)
        }

        return view
    }
}