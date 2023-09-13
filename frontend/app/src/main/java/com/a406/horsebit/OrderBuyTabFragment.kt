package com.a406.horsebit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a406.horsebit.databinding.FragmentOrderBuyTabBinding
import com.a406.horsebit.databinding.FragmentStockOrderBinding

class OrderBuyTabFragment : Fragment() {

    private lateinit var binding : FragmentOrderBuyTabBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_order_buy_tab, container, false)

        binding = FragmentOrderBuyTabBinding.bind(view)

        return view
    }
}