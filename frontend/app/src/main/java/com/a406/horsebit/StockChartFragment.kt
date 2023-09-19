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
        CandleShow(1, 1.2f, 1.8f, 1.1f, 1.9f),
        CandleShow(2, 1.8f, 2.2f, 1.6f, 2.1f),
        CandleShow(3, 2.2f, 1.2f, 1.1f, 2.9f),
        CandleShow(4, 1.3f, 1.7f, 1.2f, 1.8f),
        CandleShow(5, 1.7f, 2.0f, 1.6f, 2.1f),
        CandleShow(6, 2.1f, 1.5f, 1.4f, 2.2f),
        CandleShow(7, 1.4f, 1.9f, 1.3f, 2.0f),
        CandleShow(8, 1.9f, 2.1f, 1.8f, 2.2f),
        CandleShow(9, 2.1f, 1.7f, 1.6f, 2.3f),
        CandleShow(10, 1.7f, 2.0f, 1.6f, 2.2f),
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

    private fun initChart() {
        binding.apply {
            ccCandleChart.description.isEnabled = false
            ccCandleChart.setMaxVisibleValueCount(200)
            ccCandleChart.setPinchZoom(false)
            ccCandleChart.setDrawGridBackground(false)
            // x축 설정
            ccCandleChart.xAxis.apply {
                textColor = Color.TRANSPARENT
                position = XAxis.XAxisPosition.BOTTOM
                // 세로선 표시 여부 설정
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

        binding.apply{
            ccBarChart.setDrawGridBackground(false)
            ccBarChart.setDrawBarShadow(false)
            ccBarChart.setDrawBorders(false)

            //val description = Description()
            //description.isEnabled
            //ccBarChart.description(description)

            ccBarChart.animateY(1000)
            ccBarChart.animateX(1000)

            val xAxis: XAxis = ccBarChart.getXAxis()
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.textColor = Color.RED
            xAxis.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)

            //좌측 값 hiding the left y-axis line, default true if not set
            val leftAxis: YAxis = ccBarChart.getAxisLeft()
            leftAxis.setDrawAxisLine(false)
            leftAxis.textColor = Color.RED


            //우측 값 hiding the right y-axis line, default true if not set
            val rightAxis: YAxis = ccBarChart.getAxisRight()
            rightAxis.setDrawAxisLine(false)
            rightAxis.textColor = Color.RED


            //바차트의 타이틀
            val legend: Legend = ccBarChart.getLegend()
            //setting the shape of the legend form to line, default square shape
            legend.form = Legend.LegendForm.LINE
            //setting the text size of the legend
            legend.textSize = 11f
            legend.textColor = Color.YELLOW
            //setting the alignment of legend toward the chart
            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            //setting the stacking direction of legend
            legend.orientation = Legend.LegendOrientation.HORIZONTAL
            //setting the location of legend outside the chart, default false if not set
            legend.setDrawInside(false)
        }
    }

    private fun setChartData(candles: ArrayList<CandleShow>, volumes: ArrayList<Int>) {
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


        binding.ccBarChart.setScaleEnabled(false)


        val volumeEntries = ArrayList<BarEntry>()
        for(i in 0 until valueListData.size) {
            volumeEntries.add(
                BarEntry(
                    i.toFloat(), valueListData[i].toFloat()
                )
            )
        }
        val barDataSet = BarDataSet(volumeEntries, "dd")
        val data = BarData(barDataSet)
        binding.ccBarChart.data = data
        binding.ccBarChart.invalidate()

    }
}