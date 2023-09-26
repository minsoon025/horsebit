package com.a406.horsebit

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.a406.horsebit.databinding.AssetTableItemBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections

class AssetTableItemAdapter(val tokenShowList: ArrayList<TokenShow>) : RecyclerView.Adapter<AssetTableItemAdapter.CustomViewHolder>(), Filterable {


    var filteredTokenShowList = ArrayList<TokenShow>()
    var itemFilter = ItemFilter()

    init {
        filteredTokenShowList.addAll(tokenShowList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetTableItemAdapter.CustomViewHolder {
        val binding = AssetTableItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding).apply {
            itemView.setOnClickListener{
                val curPos: Int = adapterPosition
                val assetItem: TokenShow = filteredTokenShowList.get(curPos)

                val intent = Intent(binding.root.context, OrderActivity::class.java)
                intent.putExtra("assetName", assetItem.name)
                intent.putExtra("assetTicker", assetItem.code)
                intent.putExtra("currentPrice", assetItem.currentPrice.toString())
                intent.putExtra("yesterdayPrice", assetItem.priceRateOfChange.toString())
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredTokenShowList.size
    }

    override fun getFilter(): Filter {
        return itemFilter
    }

    inner class ItemFilter: Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filterString = charSequence.toString()
            val results = FilterResults()

            val filteredList: ArrayList<TokenShow> = ArrayList<TokenShow>()
            if (filterString.trim { it <= ' ' }.isEmpty()) {
                results.values = tokenShowList
                results.count = tokenShowList.size

                return results
            }
            else {
                for(tokenShow in tokenShowList) {
                    if (tokenShow.code.contains(filterString) || tokenShow.name.contains(filterString)) {
                        filteredList.add(tokenShow)
                    }
                }
            }

            results.values = filteredList
            results.count = filteredList.size

            return results
        }
        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
            filteredTokenShowList.clear()
            filteredTokenShowList.addAll(filterResults.values as ArrayList<TokenShow>)
            notifyDataSetChanged() // RecyclerView 어댑터를 업데이트
        }
    }

    override fun onBindViewHolder(holder: AssetTableItemAdapter.CustomViewHolder, position: Int) {
        val assetItem = filteredTokenShowList[position]
        holder.bind(assetItem)
    }

    class CustomViewHolder(private val binding: AssetTableItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tokenShow: TokenShow) {
            initChart()
            setChartData(CandleShow(0, 0f, if(tokenShow.priceRateOfChange.toFloat() > 20f) 20f else if(tokenShow.priceRateOfChange.toFloat() < -20f) -20f else tokenShow.priceRateOfChange.toFloat(), 20f, -20f))

            binding.tvItemAssetName.text = tokenShow.name
            binding.hsvAssetTableItemName.isHorizontalScrollBarEnabled = false
            binding.tvItemAssetTicker.text = tokenShow.code
            binding.ivNew.setImageResource(R.drawable.baseline_fiber_new_24)
            binding.ivNew.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.orange_red))
            if(tokenShow.newFlag) {
                binding.ivNew.isVisible = true
                val params = binding.hsvAssetTableItemName.layoutParams as LinearLayout.LayoutParams
                params.weight = 4f
                binding.hsvAssetTableItemName.layoutParams = params
            }
            else {
                binding.ivNew.isInvisible = true
                val params = binding.hsvAssetTableItemName.layoutParams as LinearLayout.LayoutParams
                params.weight = 5f
                binding.hsvAssetTableItemName.layoutParams = params
            }

            var color = ContextCompat.getColor(binding.root.context, R.color.black)

            if(tokenShow.priceRateOfChange > 0) {color = ContextCompat.getColor(binding.root.context, R.color.red)}
            else if(tokenShow.priceRateOfChange < 0) {color = ContextCompat.getColor(binding.root.context, R.color.blue)}

            binding.tvItemCurrentPrice.setTextColor(color)
            binding.tvItemYesterdayPrice.setTextColor(color)
            binding.tvItemCurrentPrice.text = tokenShow.currentPrice.toString()
            binding.tvItemYesterdayPrice.text = "${tokenShow.priceRateOfChange.toString()}%"
            binding.tvItemTransactionPrice.text = "${tokenShow.volume.toString()}만원"

            if(tokenShow.interest) {
                binding.tvRemoveOrFavor.text = "즐겨찾기\n삭제"
                binding.tvRemoveOrFavor.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.red))
            }
            else{
                binding.tvRemoveOrFavor.text = "즐겨찾기\n추가"
                binding.tvRemoveOrFavor.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.yellow))
            }
            val api = APIS.create()
            binding.llhAssetTableItemHide.setOnClickListener {
                if(tokenShow.interest) {
                    // 즐겨찾기 삭제
                    api.deleteFavorite(tokenNo = tokenShow.tokenNo, authorization = "Bearer ${1}").enqueue(object: Callback<FavoriteResponseBodyModel> {
                        override fun onResponse(call: Call<FavoriteResponseBodyModel>, response: Response<FavoriteResponseBodyModel>) {
                            if(response.code() == 200) {    // 200 Success
                                Log.d("로그", "즐겨찾기 삭제: 200 Success")
                                Toast.makeText(binding.root.context,"${tokenShow.name}이 즐겨찾기에서 삭제되었습니다", Toast.LENGTH_SHORT).show()
                            }
                            else if(response.code() == 201) {   // 201 Created
                                Log.d("로그", "즐겨찾기 삭제: 201 Created")
                            }
                            else if(response.code() == 202) {   // 202 Accepted - 요청은 정상이나 아직 처리 중
                                Log.d("로그", "즐겨찾기 삭제: 202 Accepted")
                            }
                            else if(response.code() == 400) {   // 400 Bad Request - Message에 누락 필드명 기입
                                Log.d("로그", "즐겨찾기 삭제: 400 Bad Request")
                            }
                            else if(response.code() == 401) {   // 401 Unauthorized - 인증 토큰값 무효
                                Log.d("로그", "즐겨찾기 삭제: 401 Unauthorized")
                            }
                            else if(response.code() == 403) {   // 403 Forbidden - 권한 없음 (둘러보기 회원)
                                Log.d("로그", "즐겨찾기 삭제: 403 Forbidden")
                            }
                            else if(response.code() == 404) {   // 404 Not Found
                                Log.d("로그", "즐겨찾기 삭제: 404 Not Found")
                            }
                        }
                        override fun onFailure(call: Call<FavoriteResponseBodyModel>, t: Throwable) {
                            Log.d("로그", "즐겨찾기 삭제: onFailure")
                        }
                    })
                }
                else{
                    // 즐겨 찾기 추가
                    api.addFavorite(tokenNo = tokenShow.tokenNo, authorization = "Bearer ${1}").enqueue(object: Callback<FavoriteResponseBodyModel> {
                        override fun onResponse(call: Call<FavoriteResponseBodyModel>, response: Response<FavoriteResponseBodyModel>) {
                            if(response.code() == 200) {    // 200 Success
                                Log.d("로그", "즐겨찾기 추가: 200 Success")
                                Toast.makeText(binding.root.context,"${tokenShow.name}이 즐겨찾기에 추가되었습니다", Toast.LENGTH_SHORT).show()
                            }
                            else if(response.code() == 201) {   // 201 Created
                                Log.d("로그", "즐겨찾기 추가: 201 Created")
                            }
                            else if(response.code() == 202) {   // 202 Accepted - 요청은 정상이나 아직 처리 중
                                Log.d("로그", "즐겨찾기 추가: 202 Accepted")
                            }
                            else if(response.code() == 400) {   // 400 Bad Request - Message에 누락 필드명 기입
                                Log.d("로그", "즐겨찾기 추가: 400 Bad Request")
                            }
                            else if(response.code() == 401) {   // 401 Unauthorized - 인증 토큰값 무효
                                Log.d("로그", "즐겨찾기 추가: 401 Unauthorized")
                            }
                            else if(response.code() == 403) {   // 403 Forbidden - 권한 없음 (둘러보기 회원)
                                Log.d("로그", "즐겨찾기 추가: 403 Forbidden")
                            }
                            else if(response.code() == 404) {   // 404 Not Found
                                Log.d("로그", "즐겨찾기 추가: 404 Not Found")
                            }
                        }
                        override fun onFailure(call: Call<FavoriteResponseBodyModel>, t: Throwable) {
                            Log.d("로그", "즐겨찾기 추가: onFailure")
                        }
                    })
                }



            }
        }

        private fun setChartData(candle: CandleShow) {
            // 캔들차트
            val priceEntries = ArrayList<CandleEntry>()

            priceEntries.add(
                CandleEntry(
                    candle.createdAt.toFloat(),
                    candle.shadowHigh,
                    candle.shadowLow,
                    candle.open,
                    candle.close
                )
            )

            val priceDataSet = CandleDataSet(priceEntries, "").apply {
                this.axisDependency = YAxis.AxisDependency.LEFT
                // 심지 부분 설정
                this.shadowColor = ContextCompat.getColor(binding.root.context, R.color.font_gray)
                this.shadowWidth = 0.7F
                // 음봉 설정
                this.decreasingColor = ContextCompat.getColor(binding.root.context, R.color.blue)
                this.decreasingPaintStyle = Paint.Style.FILL
                // 양봉 설정
                this.increasingColor = ContextCompat.getColor(binding.root.context, R.color.red)
                this.increasingPaintStyle = Paint.Style.FILL

                this.neutralColor = ContextCompat.getColor(binding.root.context, R.color.black)
                this.setDrawValues(false)

                // 터치시 노란 선 제거
                this.highLightColor = Color.TRANSPARENT
            }

            binding.ccGraph.apply {
                this.data = CandleData(priceDataSet)
                invalidate()
            }
        }

        private fun initChart() {
            // 캔들 차트
            binding.apply {
                ccGraph.description.isEnabled = false // description 표시하지 않기

                ccGraph.setTouchEnabled(false) // 그래프 터치 가능
                ccGraph.setDrawGridBackground(false)

                // x축 설정
                ccGraph.xAxis.apply {
                    this.setDrawLabels(false)
                    this.setDrawAxisLine(false)
                    this.setDrawGridLines(false)
                }

                // 왼쪽 y축 설정
                ccGraph.axisLeft.apply {
                    this.setDrawLabels(false)
                    this.setDrawAxisLine(false)
                    this.setDrawGridLines(false)
                }
                // 오른쪽 y축 설정
                ccGraph.axisRight.apply {
                    this.setDrawLabels(false)
                    this.setDrawAxisLine(false)
                    this.setDrawGridLines(false)
                }
                ccGraph.legend.isEnabled = false
            }
        }
    }

    fun sortByAssetNameDescending() {
        filteredTokenShowList.sortByDescending { it.name }
        notifyDataSetChanged()
    }

    fun sortByAssetNameAscending() {
        filteredTokenShowList.sortBy { it.name }
        notifyDataSetChanged()
    }

    fun sortByAssetCurrentPriceDescending() {
        filteredTokenShowList.sortByDescending { it.currentPrice }
        notifyDataSetChanged()
    }

    fun sortByAssetCurrentPriceAscending() {
        filteredTokenShowList.sortBy { it.currentPrice }
        notifyDataSetChanged()
    }

    fun sortByPriceTrendDescending() {
        filteredTokenShowList.sortByDescending { it.priceRateOfChange }
        notifyDataSetChanged()
    }

    fun sortByPriceTrendAscending() {
        filteredTokenShowList.sortBy { it.priceRateOfChange }
        notifyDataSetChanged()
    }

    fun sortByVolumeDescending() {
        filteredTokenShowList.sortByDescending { it.volume }
        notifyDataSetChanged()
    }

    fun sortByVolumeAscending() {
        filteredTokenShowList.sortBy { it.volume }
        notifyDataSetChanged()
    }

    fun removeData(position: Int) {
        tokenShowList.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged() // 데이터 변경 후 새로고침
    }

    fun swapData(fromPos: Int, toPos: Int) {
        Collections.swap(tokenShowList, fromPos, toPos)
        notifyItemMoved(fromPos, toPos)
        notifyDataSetChanged() // 데이터 변경 후 새로고침
    }

}
