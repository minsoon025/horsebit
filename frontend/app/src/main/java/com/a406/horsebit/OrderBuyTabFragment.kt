    package com.a406.horsebit

    import android.os.Bundle
    import android.text.Editable
    import android.text.TextWatcher
    import android.util.Log
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.ArrayAdapter
    import android.widget.Spinner
    import com.a406.horsebit.databinding.FragmentOrderBuyTabBinding
    import com.a406.horsebit.databinding.FragmentStockOrderBinding
    import java.util.Locale

    class OrderBuyTabFragment : Fragment() {

        private lateinit var binding : FragmentOrderBuyTabBinding
        private lateinit var orderNum: Spinner

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_order_buy_tab, container, false)

            binding = FragmentOrderBuyTabBinding.bind(view)

            binding.tvOrderBuyNumType.text = arguments?.getString("ticker").toString()

            binding.etOrderBuyNum.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    calculateTotalPrice()
                }
            })

            binding.etOrderBuyPrice.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    calculateTotalPrice()
                }
            })

            return view
        }

        private fun calculateTotalPrice() {
            val orderNumText = binding.etOrderBuyNum.text.toString()
            val orderPriceText = binding.etOrderBuyPrice.text.toString()

            val orderNum = orderNumText.toDoubleOrNull() ?: 0.0
            val orderPrice = orderPriceText.toDoubleOrNull() ?: 0.0

            val totalPrice = orderNum * orderPrice

            val formattedTotalPrice = String.format(Locale.US, "%.0f", totalPrice)

            binding.tvOrderBuyTotalPrice.text = formattedTotalPrice
        }
    }