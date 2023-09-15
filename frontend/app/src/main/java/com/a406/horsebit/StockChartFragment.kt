package com.a406.horsebit

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Half.toFloat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a406.horsebit.databinding.FragmentStockChartBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry

class StockChartFragment : Fragment() {

    private lateinit var binding: FragmentStockChartBinding

    val chartData = arrayListOf(
        Candle(1, 1.2f, 1.8f, 1.1f, 1.9f),
        Candle(2, 1.8f, 2.2f, 1.6f, 2.1f),
        Candle(3, 2.2f, 1.2f, 1.1f, 2.9f),
        Candle(4, 1.3f, 1.7f, 1.2f, 1.8f),
        Candle(5, 1.7f, 2.0f, 1.6f, 2.1f),
        Candle(6, 2.1f, 1.5f, 1.4f, 2.2f),
        Candle(7, 1.4f, 1.9f, 1.3f, 2.0f),
        Candle(8, 1.9f, 2.1f, 1.8f, 2.2f),
        Candle(9, 2.1f, 1.7f, 1.6f, 2.3f),
        Candle(10, 1.7f, 2.0f, 1.6f, 2.2f),
        
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stock_chart, container, false)

        binding = FragmentStockChartBinding.bind(view)

        initChart()

        setChartData(chartData)

        return view
    }

    private fun initChart() {
        binding.apply {
            tmpCandle.description.isEnabled = false
            tmpCandle.setMaxVisibleValueCount(200)
            tmpCandle.setPinchZoom(false)
            tmpCandle.setDrawGridBackground(false)
            // x축 설정
            tmpCandle.xAxis.apply {
                textColor = Color.TRANSPARENT
                position = XAxis.XAxisPosition.BOTTOM
                // 세로선 표시 여부 설정
                this.setDrawGridLines(true)
                axisLineColor = Color.rgb(50, 59, 76)
                gridColor = Color.rgb(50, 59, 76)
            }
            // 왼쪽 y축 설정
            tmpCandle.axisLeft.apply {
                textColor = Color.WHITE
                isEnabled = false
            }
            // 오른쪽 y축 설정
            tmpCandle.axisRight.apply {
                setLabelCount(7, false)
                textColor = Color.WHITE
                // 가로선 표시 여부 설정
                setDrawGridLines(true)
                // 차트의 오른쪽 테두리 라인 설정
                setDrawAxisLine(true)
                axisLineColor = Color.rgb(50, 59, 76)
                gridColor = Color.rgb(50, 59, 76)
            }
            tmpCandle.legend.isEnabled = false
        }
    }

    private fun setChartData(candles: ArrayList<Candle>) {
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
            shadowColor = Color.LTGRAY
            shadowWidth = 0.7F
            // 음봉 설정
            decreasingColor = Color.rgb(18, 98, 197)
            decreasingPaintStyle = Paint.Style.FILL
            // 양봉 설정
            increasingColor = Color.rgb(200, 74, 49)
            increasingPaintStyle = Paint.Style.FILL

            neutralColor = Color.rgb(6, 18, 34)
            setDrawValues(false)
            // 터치시 노란 선 제거
            highLightColor = Color.TRANSPARENT
        }

        binding.tmpCandle.apply {
            this.data = CandleData(priceDataSet)
            invalidate()
        }
    }


}