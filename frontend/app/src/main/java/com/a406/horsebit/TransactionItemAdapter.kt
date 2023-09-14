package com.a406.horsebit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.a406.horsebit.databinding.TransactionItemBinding

class TransactionItemAdapter(val transactionOrderList: ArrayList<TransactionOrder>): RecyclerView.Adapter<TransactionItemAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionItemAdapter.CustomViewHolder {
        val binding = TransactionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionItemAdapter.CustomViewHolder, position: Int) {
        val transactionItem = transactionOrderList[position]
        holder.bind(transactionItem)
    }

    override fun getItemCount(): Int {
        return transactionOrderList.size
    }

    class CustomViewHolder(private val binding: TransactionItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(transactionItem : TransactionOrder) {

            binding.tvBuyOrSell.text = "매도"
            binding.tvBuyOrSellTime.text = "${(transactionItem.orderTime.month + 1).toString()}.${transactionItem.orderTime.date.toString()}. ${transactionItem.orderTime.hours.toString()}:${transactionItem.orderTime.minutes.toString()}"
            binding.tvTokenNameRight.text = transactionItem.tokenCode
            binding.tvPriceRight.text = transactionItem.price.toString()
            binding.tvQuantityRight.text = transactionItem.quantity.toString()
            binding.tvInfoRight.text = transactionItem.remain_quantity.toString()
        }
    }

}