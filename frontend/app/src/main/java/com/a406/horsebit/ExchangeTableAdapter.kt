package com.a406.horsebit

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.a406.horsebit.databinding.ExchangeItemBinding

class ExchangeTableAdapter(val exchangeItemList: ArrayList<ExchangeDataResponseBodyModel>) : RecyclerView.Adapter<ExchangeTableAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ExchangeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return exchangeItemList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val exchange = exchangeItemList[position]
        holder.bind(exchange)
    }

    inner class CustomViewHolder(private val binding: ExchangeItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(exchange: ExchangeDataResponseBodyModel) {


            binding.tvIExOrderTime.setBackgroundResource(R.drawable.gray_border_background)
            binding.tvIExCoin.setBackgroundResource(R.drawable.gray_border_background)
            binding.tvIExType.setBackgroundResource(R.drawable.gray_border_background)
            binding.tvIExSeep.setBackgroundResource(R.drawable.gray_border_background)
            binding.tvIExOne.setBackgroundResource(R.drawable.gray_border_background)
            binding.tvIExMoney.setBackgroundResource(R.drawable.gray_border_background)
            binding.tvIExFee.setBackgroundResource(R.drawable.gray_border_background)
            binding.tvIExRealMoney.setBackgroundResource(R.drawable.gray_border_background)
            binding.tvIExOrderTime2.setBackgroundResource(R.drawable.gray_border_background)

            binding.tvIExOrderTime.setTypeface(null, Typeface.NORMAL)
            binding.tvIExCoin.setTypeface(null, Typeface.NORMAL)
            binding.tvIExType.setTypeface(null, Typeface.NORMAL)
            binding.tvIExSeep.setTypeface(null, Typeface.NORMAL)
            binding.tvIExOne.setTypeface(null, Typeface.NORMAL)
            binding.tvIExMoney.setTypeface(null, Typeface.NORMAL)
            binding.tvIExFee.setTypeface(null, Typeface.NORMAL)
            binding.tvIExRealMoney.setTypeface(null, Typeface.NORMAL)
            binding.tvIExOrderTime2.setTypeface(null, Typeface.NORMAL)






            binding.tvIExOrderTime.text = exchange.executionTime ?: "-"
            binding.tvIExCoin.text = exchange.code
            binding.tvIExType.text = exchange.transactionType
            binding.tvIExSeep.text = exchange.volume
            binding.tvIExOne.text = if (exchange.price.toIntOrNull() == 0) "-" else exchange.price
            binding.tvIExMoney.text = exchange.transactionAmount
            binding.tvIExFee.text = exchange.fee
            binding.tvIExRealMoney.text = exchange.amount
            binding.tvIExOrderTime2.text = exchange.orderTime ?: "-"

            // "executionTime"이 "채결시간"인 경우에만 볼드체와 배경색 적용
            if (exchange.executionTime == "채결시간") {
                applyBoldText()
                val context = binding.root.context
                applyBackgroundColor(ContextCompat.getColor(context, R.color.main_color ))
            }else{
                binding.tvIExOrderTime.setTypeface(null, Typeface.NORMAL)

            }


            when (exchange.transactionType) {
                "BID" -> {
                    binding.tvIExType.text = "매수"
                    binding.tvIExType.setTextColor(Color.RED)
                }
                "OFFER" -> {
                    binding.tvIExType.text = "매도"
                    binding.tvIExType.setTextColor(Color.BLUE)
                }
                "DEPOSIT" -> {
                    binding.tvIExType.text = "입금"
                    binding.tvIExType.setTextColor(Color.RED)
                }
                "WITHDRAW" -> {
                    binding.tvIExType.text = "출금"
                    binding.tvIExType.setTextColor(Color.BLUE)
                }
                "종류" -> {
                    binding.tvIExType.text = "종류"
                    binding.tvIExType.setTextColor(Color.BLACK)
                }
            }

            binding.lihIExBar2.setOnClickListener {
                val curPos: Int = adapterPosition
                val exchangeList: ExchangeDataResponseBodyModel = exchangeItemList[curPos]
                if (exchangeList.tokenNo.toInt() != 0) {
                    val intent = Intent(binding.root.context, OrderActivity::class.java)
                    intent.putExtra("tokenNo", exchangeList.tokenNo)
                    binding.root.context.startActivity(intent)
                } else {
                    // 현금 거래의 값이 들어오게 되면
                    Toast.makeText(binding.root.context, "현금 입출금내역입니다.", Toast.LENGTH_SHORT).show()
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
        fun applyBackgroundColor2(color: Int) {
            binding.root.setBackgroundResource(color)
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
