package com.a406.horsebit

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
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

        // 첫 번째 항목에만 볼드 텍스트와 배경색 적용
        if (position == 0) {
            val context = holder.itemView.context
            holder.applyBoldText()
            holder.applyBackgroundColor(ContextCompat.getColor(context, R.color.main_color))
        }
    }

    inner class CustomViewHolder(private val binding: ExchangeItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(exchange: ExchangeDataResponseBodyModel) {
            binding.tvIExOrderTime.text = exchange.executionTime ?: "-"
            binding.tvIExCoin.text = exchange.code
            binding.tvIExType.text = exchange.transactionType
            binding.tvIExSeep.text = exchange.volume
            binding.tvIExOne.text = exchange.price
            binding.tvIExMoney.text = exchange.transactionAmount
            binding.tvIExFee.text = exchange.fee
            binding.tvIExRealMoney.text = exchange.amount
            binding.tvIExOrderTime2.text = exchange.orderTime ?: "-"


            binding.lihIExBar2.setOnClickListener {
                    val curPos: Int = adapterPosition
                val exchangeList: ExchangeDataResponseBodyModel = exchangItemList.get(curPos)
                if (exchangeList.tokenNo.toInt() != 0) {
                    val intent = Intent(binding.root.context, OrderActivity::class.java)
                    intent.putExtra("tokenNo", exchangeList.tokenNo)
                    binding.root.context.startActivity(intent)
                }
                else{
                    // 현금 거래의 값이 들어오게 되면
                    Toast.makeText(binding.root.context,"현금 입출금내역입니다.", Toast.LENGTH_SHORT).show()
                }
            }



        }

        // 볼드 텍스트를 적용하는 함수
        fun applyBoldText() {
            binding.tvIExOrderTime.setTypeface(null, Typeface.BOLD)
            binding.tvIExCoin.setTypeface(null, Typeface.BOLD)
            binding.tvIExType.setTypeface(null, Typeface.BOLD)
            binding.tvIExSeep.setTypeface(null, Typeface.BOLD)
            binding.tvIExOne.setTypeface(null, Typeface.BOLD)
            binding.tvIExMoney.setTypeface(null, Typeface.BOLD)
            binding.tvIExFee.setTypeface(null, Typeface.BOLD)
            binding.tvIExRealMoney.setTypeface(null, Typeface.BOLD)
            binding.tvIExOrderTime2.setTypeface(null, Typeface.BOLD)
        }

        // 볼드 텍스트를 초기화하는 함수
        fun resetBoldText() {
            binding.tvIExOrderTime.setTypeface(null, Typeface.NORMAL)
            binding.tvIExCoin.setTypeface(null, Typeface.NORMAL)
            binding.tvIExType.setTypeface(null, Typeface.NORMAL)
            binding.tvIExSeep.setTypeface(null, Typeface.NORMAL)
            binding.tvIExOne.setTypeface(null, Typeface.NORMAL)
            binding.tvIExMoney.setTypeface(null, Typeface.NORMAL)
            binding.tvIExFee.setTypeface(null, Typeface.NORMAL)
            binding.tvIExRealMoney.setTypeface(null, Typeface.NORMAL)
            binding.tvIExOrderTime2.setTypeface(null, Typeface.NORMAL)
        }

        // 배경색을 적용하는 함수
        fun applyBackgroundColor(color: Int) {
            binding.tvIExOrderTime.setBackgroundColor(color)
            binding.tvIExCoin.setBackgroundColor(color)
            binding.tvIExType.setBackgroundColor(color)
            binding.tvIExSeep.setBackgroundColor(color)
            binding.tvIExOne.setBackgroundColor(color)
            binding.tvIExMoney.setBackgroundColor(color)
            binding.tvIExFee.setBackgroundColor(color)
            binding.tvIExRealMoney.setBackgroundColor(color)
            binding.tvIExOrderTime2.setBackgroundColor(color)
        }

        // 배경색을 초기화하는 함수
        fun resetBackgroundColor() {
            binding.tvIExOrderTime.setBackgroundColor(Color.TRANSPARENT)
            binding.tvIExCoin.setBackgroundColor(Color.TRANSPARENT)
            binding.tvIExType.setBackgroundColor(Color.TRANSPARENT)
            binding.tvIExSeep.setBackgroundColor(Color.TRANSPARENT)
            binding.tvIExOne.setBackgroundColor(Color.TRANSPARENT)
            binding.tvIExMoney.setBackgroundColor(Color.TRANSPARENT)
            binding.tvIExFee.setBackgroundColor(Color.TRANSPARENT)
            binding.tvIExRealMoney.setBackgroundColor(Color.TRANSPARENT)
            binding.tvIExOrderTime2.setBackgroundColor(Color.TRANSPARENT)
        }
    }
}
