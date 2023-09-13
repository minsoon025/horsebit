package com.a406.horsebit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.a406.horsebit.databinding.FragmentOrderBuyTabBinding
import com.a406.horsebit.databinding.FragmentStockOrderBinding

class OrderBuyTabFragment : Fragment() {

    private lateinit var binding : FragmentOrderBuyTabBinding
    private lateinit var orderNum: Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_order_buy_tab, container, false)

        binding = FragmentOrderBuyTabBinding.bind(view)


        return view
    }
}