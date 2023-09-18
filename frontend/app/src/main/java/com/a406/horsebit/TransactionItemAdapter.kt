package com.a406.horsebit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.a406.horsebit.databinding.TransactionItemBinding

class TransactionItemAdapter(val transactionOrderList: ArrayList<TransactionShow>): RecyclerView.Adapter<TransactionItemAdapter.CustomViewHolder>() {

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
        fun bind(transactionItem : TransactionShow) {

            if(transactionItem.sellORBuy == "S") {
                binding.tvBuyOrSell.text = "매도"
                binding.tvBuyOrSell.setTextColor(ContextCompat.getColor(binding.root.context, R.color.blue))
            }
            else {
                binding.tvBuyOrSell.text = "매수"
                binding.tvBuyOrSell.setTextColor(ContextCompat.getColor(binding.root.context, R.color.red))
            }

            binding.tvBuyOrSellTime.text = "${(transactionItem.time.month + 1).toString()}.${transactionItem.time.date.toString()}. ${transactionItem.time.hours.toString()}:${transactionItem.time.minutes.toString()}"
            binding.tvTokenNameRight.text = transactionItem.tokenCode
            binding.tvPriceRight.text = transactionItem.price.toString()
            binding.tvQuantityRight.text = transactionItem.quantity.toString()
            binding.tvInfoRight.text = transactionItem.remainQuantityOrPrice.toString()

            if(transactionItem.completeOrNot) {binding.tvInfoLeft.text = "체결금액"}
            else {binding.tvInfoLeft.text = "미체결양"}
        }
    }

}