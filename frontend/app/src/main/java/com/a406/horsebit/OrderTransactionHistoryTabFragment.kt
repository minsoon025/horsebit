package com.a406.horsebit

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.a406.horsebit.databinding.FragmentOrderTransactionHistoryTabBinding
import java.util.Date

class OrderTransactionHistoryTabFragment : Fragment() {

    private lateinit var binding : FragmentOrderTransactionHistoryTabBinding

    val transactionItemList = arrayListOf(
        TransactionOrder(1, 1, 1, "asdf", 1, 1.1, 1.2, Date()),
        TransactionOrder(1, 1, 1, "asdf", 1, 1.1, 1.2, Date()),
        TransactionOrder(1, 1, 1, "asdf", 1, 1.1, 1.2, Date()),
        TransactionOrder(1, 1, 1, "asdf", 1, 1.1, 1.2, Date()),
        TransactionOrder(1, 1, 1, "asdf", 1, 1.1, 1.2, Date()),

    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_order_transaction_history_tab, container, false)

        binding = FragmentOrderTransactionHistoryTabBinding.bind(view)

        changeColor(0)

        binding.tvNotConclusion.setOnClickListener {
            changeColor(0)
        }

        binding.tvConclusion.setOnClickListener {
            changeColor(1)
        }

        binding.rvTransactionTable.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)   // VERTICAL은 세로로
        binding.rvTransactionTable.setHasFixedSize(true) // 성능 개선

        binding.rvTransactionTable.adapter = TransactionItemAdapter(transactionItemList)


        return view
    }
    private fun changeColor(i: Int) {

        when(i) {
            0 -> {
                var newStrokeColor = ContextCompat.getColor(binding.root.context, R.color.main_color)
                var drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.edge)

                drawable?.setColorFilter(newStrokeColor, android.graphics.PorterDuff.Mode.SRC_ATOP)
                binding.tvNotConclusion.background = drawable
                binding.tvNotConclusion.setTextColor(ContextCompat.getColor(binding.root.context, R.color.background))
                // binding.tvNotConclusion.setBackgroundResource(R.color.main_color)



                newStrokeColor = ContextCompat.getColor(binding.root.context, R.color.font_gray)
                drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.edge)

                drawable?.setColorFilter(newStrokeColor, android.graphics.PorterDuff.Mode.SRC_ATOP)
                binding.tvConclusion.background = drawable

                binding.tvConclusion.setTextColor(ContextCompat.getColor(binding.root.context, R.color.font_gray))
                //binding.tvConclusion.setBackgroundResource(R.color.font_gray)
            }
            1 -> {
                var newStrokeColor = ContextCompat.getColor(binding.root.context, R.color.font_gray)
                var drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.edge)

                drawable?.setColorFilter(newStrokeColor, android.graphics.PorterDuff.Mode.SRC_ATOP)
                binding.tvNotConclusion.background = drawable


                binding.tvNotConclusion.setTextColor(ContextCompat.getColor(binding.root.context, R.color.font_gray))
                //binding.tvNotConclusion.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.font_gray))


                newStrokeColor = ContextCompat.getColor(binding.root.context, R.color.main_color)
                drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.edge)

                drawable?.setColorFilter(newStrokeColor, android.graphics.PorterDuff.Mode.SRC_ATOP)
                binding.tvConclusion.background = drawable



                binding.tvConclusion.setTextColor(ContextCompat.getColor(binding.root.context, R.color.background))
                //binding.tvConclusion.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.main_color))
            }
        }
    }
}