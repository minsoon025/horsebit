package com.a406.horsebit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a406.horsebit.databinding.FragmentOrderSellTabBinding

class OrderSellTabFragment : Fragment() {

    private lateinit var binding : FragmentOrderSellTabBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_order_sell_tab, container, false)

        binding = FragmentOrderSellTabBinding.bind(view)

        return view
    }
}