package com.a406.horsebit

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.a406.horsebit.databinding.FragmentStockChartBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StockChartFragment : Fragment() {

    private lateinit var binding: FragmentStockChartBinding
    val api = APIS.create()

    var tokenNo: Long = 0

    var candleChartData = arrayListOf<CandleShow>()
    val barChartData = arrayListOf<BarShow>()

    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 1000L // 1초마다 업데이트

    private val apiRunnable = object : Runnable {
        override fun run() {
            updateChartData() // 캔들 차트 데이터 업데이트
            handler.postDelayed(this, updateInterval)
        }
    }

    private fun updateChartData() {
        val customDateTime = LocalDateTime.now()

        tokenNo = arguments?.getLong("tokenNo") ?: 0

        api.candleChartData(tokenNo = tokenNo, quantity = 100L, endTime = customDateTime, candleTypeIndex = 0, margin = 0L).enqueue(object: Callback<ArrayList<CandleChartDataResponseBodyBodyModel>> {
            override fun onResponse(call: Call<ArrayList<CandleChartDataResponseBodyBodyModel>>, response: Response<ArrayList<CandleChartDataResponseBodyBodyModel>>) {
                if(response.code() == 200) {    // 200 Success
                    Log.d("로그", "차트 캔들 조회: 200 Success")

                    val responseBody = response.body()

                    if(responseBody != null) {
                        var idx : Float = 0F
                        candleChartData = arrayListOf()
                        for(candleChart in responseBody) {
                            val candleShow = CandleShow(
                                idx,
                                candleChart.high.toFloat(),
                                candleChart.low.toFloat(),
                                candleChart.open.toFloat(),
                                candleChart.close.toFloat()
                            )
                            val barShow = BarShow(idx, candleChart.volume.toFloat(), candleChart.low < candleChart.high)

                            candleChartData.add(candleShow)
                            barChartData.add(barShow)
                            ++idx
                        }
                    }
                    setChartData(candleChartData, barChartData)
                }
                else if(response.code() == 400) {   // 400 Bad Request - Message에 누락 필드명 기입
                    Log.d("로그", "차트 캔들 조회: 400 Bad Request")
                }
                else if(response.code() == 401) {   // 401 Unauthorized - 인증 토큰값 무효
                    Log.d("로그", "차트 캔들 조회: 401 Unauthorized")
                }
                else if(response.code() == 403) {   // 403 Forbidden - 권한 없음 (둘러보기 회원)
                    Log.d("로그", "차트 캔들 조회: 404 Not Found")
                }
                else if(response.code() == 404) {   // 404 Not Found
                    Log.d("로그", "차트 캔들 조회: 404 Not Found")
                }
            }
            override fun onFailure(call: Call<ArrayList<CandleChartDataResponseBodyBodyModel>>, t: Throwable) {
                Log.d("로그", "차트 캔들 조회: onFailure")
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stock_chart, container, false)

        binding = FragmentStockChartBinding.bind(view)

        initChart()

        // 초기화 후 처음 데이터 로드
        updateChartData()

        // 핸들러를 사용하여 주기적으로 데이터 업데이트
        handler.postDelayed(apiRunnable, updateInterval)

        return view
    }

    // https://velog.io/@kimjihun1001/Android-MPAndroidChart-%EB%9D%BC%EC%9D%B4%EB%B8%8C%EB%9F%AC%EB%A6%AC-%EC%82%AC%EC%9A%A9-%ED%9B%84%EA%B8%B0-%EB%B0%A9%EB%B2%95
    private fun initChart() {
        // 캔들 차트
        binding.apply {
            ccCandleChart.description.isEnabled = false // description 표시하지 않기

            ccCandleChart.setTouchEnabled(true) // 그래프 터치 가능
            ccCandleChart.isDragXEnabled = true // x 축 드래그 활성화
            ccCandleChart.isDragYEnabled = true // y 축 드래그 비활성화

            ccCandleChart.setScaleEnabled(true) // 확대 가능
            ccCandleChart.setPinchZoom(true)    // 축소가능

            // ccCandleChart.setVisibleXRange(5f, 5f)

            // x축 설정
            ccCandleChart.xAxis.apply {
                this.position = XAxis.XAxisPosition.BOTTOM   // 레이블 위치 아래

                // x축 선 생성
                this.setDrawAxisLine(true)
                this.axisLineColor = ContextCompat.getColor(binding.root.context, R.color.black)
            }

            // 왼쪽 y축 설정
            ccCandleChart.axisLeft.apply {
                this.setDrawLabels(false)
            }
            // 오른쪽 y축 설정
            ccCandleChart.axisRight.apply {
                this.textColor = ContextCompat.getColor(binding.root.context, R.color.black)

                // 오른쪽 y축 선 생성
                this.setDrawAxisLine(true)
                this.axisLineColor = ContextCompat.getColor(binding.root.context, R.color.black)
            }
            ccCandleChart.legend.isEnabled = false
        }

        // 바 차트
        binding.apply{
            ccBarChart.description.isEnabled = false    // description 표시하지 않기

            ccBarChart.setTouchEnabled(true) // 그래프 터치 가능
            ccBarChart.isDragXEnabled = true // x 축 드래그 활성화
            ccBarChart.isDragYEnabled = true // y 축 드래그 비활성화

            ccBarChart.setScaleEnabled(true) // 확대 가능
            ccBarChart.setPinchZoom(true)    // 축소가능


            ccBarChart.animateY(1000)
            ccBarChart.animateX(100)

            // x 축 설정
            ccBarChart.xAxis.apply {
                this.position = XAxis.XAxisPosition.BOTTOM  // 레이블 위치 아래

                // x 축 선 생성
                this.setDrawAxisLine(true)
                this.textColor = ContextCompat.getColor(binding.root.context, R.color.black)
            }

            // 오른쪽 y 축 설정
            ccBarChart.axisRight.apply {
                this.setDrawAxisLine(false)
                this.setDrawLabels(true)
                this.textColor = ContextCompat.getColor(binding.root.context, R.color.black)
            }

            ccBarChart.axisLeft.apply {
                this.setDrawLabels(false)
            }

        }
    }

    private fun setChartData(candles: ArrayList<CandleShow>, bars: ArrayList<BarShow>) {

        // 캔들차트
        val priceEntries = ArrayList<CandleEntry>()
        for (candle in candles) {
            // 캔들 차트 entry 생성
            priceEntries.add(
                CandleEntry(
                    candle.x,
                    candle.shadowH,
                    candle.shadowL,
                    candle.open,
                    candle.close
                )
            )
        }

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

        binding.ccCandleChart.apply {
            this.data = CandleData(priceDataSet)
            invalidate()
        }

        // 바 차트
        val volumeEntries = ArrayList<BarEntry>()
        for (bar in bars) {
            // 캔들 차트 entry 생성
            volumeEntries.add(
                BarEntry(
                    bar.x,
                    bar.value
                )
            )
        }

        val colorFlag = true // 또는 어떤 조건에 따라서 true 또는 false로 설정

// colorFlag에 따른 다른 색상 정의
        val increasingColor = if (colorFlag) {
            ContextCompat.getColor(binding.root.context, R.color.red) // colorFlag가 true일 때의 색상
        } else {
            ContextCompat.getColor(binding.root.context, R.color.blue) // colorFlag가 false일 때의 색상
        }

        val barDataSet = BarDataSet(volumeEntries, "").apply {
            this.valueTextColor = Color.TRANSPARENT

            this.color = increasingColor
        }

        binding.ccBarChart.apply {
            this.data = BarData(barDataSet)
            invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 페이지를 벗어날 때 핸들러 작업을 삭제하여 반복을 종료합니다.
        handler.removeCallbacksAndMessages(null)
    }
}