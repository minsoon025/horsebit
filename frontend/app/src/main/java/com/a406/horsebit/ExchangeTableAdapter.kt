package com.a406.horsebit


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.a406.horsebit.databinding.ExchangeItemBinding

class ExchangeTableAdapter(val exchangItemList: ArrayList<ExchangeDataResponseBodyModel>) : RecyclerView.Adapter<ExchangeTableAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeTableAdapter.CustomViewHolder {
        val binding = ExchangeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return exchangItemList.size
    }

    override fun onBindViewHolder(holder: ExchangeTableAdapter.CustomViewHolder, position: Int) {
        val exchange = exchangItemList[position]
        holder.bind(exchange)
    }



    class CustomViewHolder(private val binding: ExchangeItemBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(exchange: ExchangeDataResponseBodyModel) {

            binding.tvIExOrderTime.text = exchange.executionTime
            binding.tvIExCoin.text = exchange.code
            binding.tvIExType.text = exchange.transactionType
            binding.tvIExSeep.text = exchange.volume
            binding.tvIExOne.text = exchange.price
            binding.tvIExMoney.text = exchange.transactionAmount
            binding.tvIExFee.text = exchange.fee
            binding.tvIExRealMoney.text = exchange.amount 
            binding.tvIExOrderTime2.text = exchange.orderTime


        }

    }
}





