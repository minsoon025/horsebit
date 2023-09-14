package com.a406.horsebit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.a406.horsebit.databinding.ExchangeItemBinding

class ExchangeTableAdapter(val exchangeList: ArrayList<Exchange>) : RecyclerView.Adapter<ExchangeTableAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeTableAdapter.CustomViewHolder {
        val binding = ExchangeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding).apply {

        }
    }

    override fun onBindViewHolder(holder: ExchangeTableAdapter.CustomViewHolder, position: Int) {
        val exchangeItem = exchangeList[position]
        holder.bind(exchangeItem)
    }

    override fun getItemCount(): Int {
        return exchangeList.size
    }

    class CustomViewHolder(private val binding: ExchangeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(exchangeItem: Exchange) {

            binding.tv1.text = exchangeItem.data1.toString()
            binding.tv2.text = exchangeItem.data2.toString()
            binding.tv3.text = exchangeItem.data3.toString()

        }
    }

}