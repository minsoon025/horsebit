package com.a406.horsebit

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a406.horsebit.databinding.FragmentOrderSellTabBinding
import java.util.Locale

class OrderSellTabFragment : Fragment() {

    private lateinit var binding : FragmentOrderSellTabBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_order_sell_tab, container, false)

        binding = FragmentOrderSellTabBinding.bind(view)

        binding.tvOrderCanSellPrice.text = "${arguments?.getString("ticker").toString()}"
        binding.tvOrderSellNumType.text = arguments?.getString("ticker").toString()

        binding.etOrderSellNum.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                calculateTotalPrice()
            }
        })

        binding.etOrderSellPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                calculateTotalPrice()
            }
        })

        return view
    }

    private fun calculateTotalPrice() {
        val orderNumText = binding.etOrderSellNum.text.toString()
        val orderPriceText = binding.etOrderSellPrice.text.toString()

        val orderNum = orderNumText.toDoubleOrNull() ?: 0.0
        val orderPrice = orderPriceText.toDoubleOrNull() ?: 0.0

        val totalPrice = orderNum * orderPrice

        val formattedTotalPrice = String.format(Locale.US, "%.0f", totalPrice)

        binding.tvOrderSellTotalPrice.text = formattedTotalPrice
    }
}