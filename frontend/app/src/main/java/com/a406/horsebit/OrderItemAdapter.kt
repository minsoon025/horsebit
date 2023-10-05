package com.a406.horsebit

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.a406.horsebit.databinding.OrderItemBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.CandleEntry

class OrderItemAdapter(val orderList: ArrayList<Order>, val maxVolume: Float): RecyclerView.Adapter<OrderItemAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemAdapter.CustomViewHolder {
        val binding = OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderItemAdapter.CustomViewHolder, position: Int) {
        val order = orderList[position]
        holder.bind(order, maxVolume)
    }

    class CustomViewHolder(private val binding: OrderItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order, maxVolume: Float) {
            binding.tvPrice.text = order.price.toString()
            binding.tvTrend.text = order.priceRateOfChange.toString()
            binding.tvVolume.text = order.volume.toString()

            var chartBackColor = ContextCompat.getColor(binding.root.context, R.color.black)

            if(binding.tvTrend.text.toString().toFloat() > 0) {
                chartBackColor = ContextCompat.getColor(binding.root.context, R.color.order_back_red)
            }
            else if(binding.tvTrend.text.toString().toFloat() < 0) {
                chartBackColor = ContextCompat.getColor(binding.root.context, R.color.order_back_blue)
            }

            binding.llvOrderBack.setBackgroundColor(chartBackColor)

            initChart(maxVolume)
            setChartData(order.volume)

        }

        private fun setChartData(volume: Double) {
            val volumeEntries = ArrayList<BarEntry>()

            volumeEntries.add(
                BarEntry(
                    0f, volume.toFloat(),
                )
            )

            val barDataSet = BarDataSet(volumeEntries, "").apply {
                this.valueTextColor = Color.TRANSPARENT

                var chartFrontColor = ContextCompat.getColor(binding.root.context, R.color.black)

                if(binding.tvTrend.text.toString().toFloat() > 0) {
                    chartFrontColor = ContextCompat.getColor(binding.root.context, R.color.order_front_red)
                }
                else if(binding.tvTrend.text.toString().toFloat() < 0) {
                    chartFrontColor = ContextCompat.getColor(binding.root.context, R.color.order_front_blue)
                }

                this.color = chartFrontColor
            }

            binding.ccOrderVolumeHorizontalBarChart.apply {
                this.data = BarData(barDataSet)
                invalidate()
            }
        }

        private fun initChart(maxVolume: Float) {
            binding.apply {
                ccOrderVolumeHorizontalBarChart.description.isEnabled = false
                ccOrderVolumeHorizontalBarChart.setTouchEnabled(false)

                ccOrderVolumeHorizontalBarChart.setFitBars(true)
                ccOrderVolumeHorizontalBarChart.setExtraOffsets(0f, 0f, 0f, 0f) // 모든 여백을 0으로 설정

                ccOrderVolumeHorizontalBarChart.xAxis.apply {
                    this.isEnabled = false
                }

                ccOrderVolumeHorizontalBarChart.axisLeft.apply {
                    this.axisMinimum = 0f // 최소값을 0으로 고정
                    this.axisMaximum = maxVolume
                    this.isEnabled = false
                }

                ccOrderVolumeHorizontalBarChart.axisRight.apply {
                    this.isEnabled = false
                }

                ccOrderVolumeHorizontalBarChart.legend.isEnabled = false
            }
        }
    }

}