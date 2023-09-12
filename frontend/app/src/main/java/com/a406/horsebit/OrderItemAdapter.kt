package com.a406.horsebit

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.a406.horsebit.databinding.OrderItemBinding

class OrderItemAdapter(val orderList: ArrayList<Order>): RecyclerView.Adapter<OrderItemAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemAdapter.CustomViewHolder {
        val binding = OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderItemAdapter.CustomViewHolder, position: Int) {
        val order = orderList[position]
        holder.bind(order)
    }


    class CustomViewHolder(private val binding: OrderItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            Log.d("asdfsaddsaffdsa", order.toString())
            binding.tvPrice.text = order.price.toString()
            binding.tvTrend.text = order.trend.toString()
            binding.tvVolume.text = order.volume.toString()

            var color = ContextCompat.getColor(binding.root.context, R.color.black)

            if(binding.tvTrend.text.toString().toFloat() > 0) {color = ContextCompat.getColor(binding.root.context, R.color.order_back_red)}
            else if(binding.tvTrend.text.toString().toFloat() < 0) {color = ContextCompat.getColor(binding.root.context, R.color.order_back_blue)}

            binding.llvOrderBack.setBackgroundColor(color)

        }
    }

}