package com.a406.horsebit

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Half.toFloat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.a406.horsebit.databinding.FragmentStockChartBinding
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry

class StockChartFragment : Fragment() {

    private lateinit var binding: FragmentStockChartBinding

    val candleChartData = arrayListOf(
        CandleShow(0, 1.2f, 1.8f, 1.1f, 1.9f),
        CandleShow(1, 1.8f, 2.2f, 1.6f, 2.1f),
        CandleShow(2, 2.2f, 1.2f, 1.1f, 2.9f),
        CandleShow(3, 1.3f, 1.7f, 1.2f, 1.8f),
        CandleShow(4, 1.7f, 2.0f, 1.6f, 2.1f),
        CandleShow(5, 2.1f, 1.5f, 1.4f, 2.2f),
        CandleShow(6, 1.4f, 1.9f, 1.3f, 2.0f),
        CandleShow(7, 1.9f, 2.1f, 1.8f, 2.2f),
        CandleShow(8, 2.1f, 1.7f, 1.6f, 2.3f),
        CandleShow(9, 1.7f, 2.0f, 1.6f, 2.2f),
    )

    val valueListData = arrayListOf(
        1,
        5,
        10,
        15,
        12,
        13,
        1,
        2,
        5,
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stock_chart, container, false)

        binding = FragmentStockChartBinding.bind(view)

        initChart()

        setChartData(candleChartData, valueListData)

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

            ccCandleChart.setVisibleXRange(5f, 5f)
            //ccCandleChart.setDrawGridBackground(false)

            // x축 설정
            ccCandleChart.xAxis.apply {
                textColor = Color.TRANSPARENT   // 레이블 텍스트 투명
                position = XAxis.XAxisPosition.BOTTOM   // 레이블 위치 아래

                this.setDrawGridLines(true)

                axisLineColor = Color.rgb(50, 59, 76)
                gridColor = Color.rgb(50, 59, 76)
            }
            // 왼쪽 y축 설정
            ccCandleChart.axisLeft.apply {
                textColor = Color.WHITE
                isEnabled = false
            }
            // 오른쪽 y축 설정
            ccCandleChart.axisRight.apply {
                setLabelCount(7, false)
                textColor = Color.WHITE
                // 가로선 표시 여부 설정
                setDrawGridLines(true)
                // 차트의 오른쪽 테두리 라인 설정
                setDrawAxisLine(true)
                axisLineColor = Color.rgb(50, 59, 76)
                gridColor = Color.rgb(50, 59, 76)
            }
            ccCandleChart.legend.isEnabled = false
        }

        // 바 차트
        binding.apply{
            ccBarChart.description.isEnabled = false
            ccBarChart.setDrawGridBackground(false)
            ccBarChart.setDrawBarShadow(false)
            ccBarChart.setDrawBorders(false)
            ccBarChart.setPinchZoom(false)

            // x 축 설정
            val xAxis: XAxis = ccBarChart.getXAxis()
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.textColor = ContextCompat.getColor(binding.root.context, R.color.black)
            xAxis.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)

            // 왼쪽 y 축 설정
            val leftAxis: YAxis = ccBarChart.getAxisLeft()
            leftAxis.setDrawAxisLine(false)
            leftAxis.textColor = ContextCompat.getColor(binding.root.context, R.color.black)

            // 오른쪽 y 축 설정
            val rightAxis: YAxis = ccBarChart.axisRight

        }
    }

    private fun setChartData(candles: ArrayList<CandleShow>, volumes: ArrayList<Int>) {

        // 캔들차트
        val priceEntries = ArrayList<CandleEntry>()
        for (candle in candles) {
            // 캔들 차트 entry 생성
            priceEntries.add(
                CandleEntry(
                    candle.createdAt.toFloat(),
                    candle.shadowHigh,
                    candle.shadowLow,
                    candle.open,
                    candle.close
                )
            )
        }

        val priceDataSet = CandleDataSet(priceEntries, "").apply {
            axisDependency = YAxis.AxisDependency.LEFT
            // 심지 부분 설정
            shadowColor = ContextCompat.getColor(binding.root.context, R.color.font_gray)
            shadowWidth = 0.7F
            // 음봉 설정
            decreasingColor = ContextCompat.getColor(binding.root.context, R.color.blue)
            decreasingPaintStyle = Paint.Style.FILL
            // 양봉 설정
            increasingColor = ContextCompat.getColor(binding.root.context, R.color.red)
            increasingPaintStyle = Paint.Style.FILL

            neutralColor = ContextCompat.getColor(binding.root.context, R.color.black)
            setDrawValues(false)

            // 터치시 노란 선 제거
            highLightColor = Color.TRANSPARENT
        }

        binding.ccCandleChart.apply {
            this.data = CandleData(priceDataSet)
            invalidate()
        }

        // 바 차트
        val volumeEntries = ArrayList<BarEntry>()
        for(i in 0 until volumes.size) {
            volumeEntries.add(
                BarEntry(
                    i.toFloat(), volumes[i].toFloat()
                )
            )
        }
        val barDataSet = BarDataSet(volumeEntries, "").apply {

        }

        binding.ccBarChart.apply {
            this.data = BarData(barDataSet)
            invalidate()
        }
    }
}