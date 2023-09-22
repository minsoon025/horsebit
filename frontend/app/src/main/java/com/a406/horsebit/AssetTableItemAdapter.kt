package com.a406.horsebit


import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import com.a406.horsebit.databinding.AssetTableItemBinding

class AssetTableItemAdapter(val tokenShowList: ArrayList<TokenShow>) : RecyclerView.Adapter<AssetTableItemAdapter.CustomViewHolder>(), Filterable {

    var filteredTokenShowList = ArrayList<TokenShow>()
    var itemFilter = ItemFilter()

    init {
        filteredTokenShowList.addAll(tokenShowList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetTableItemAdapter.CustomViewHolder {
        val binding = AssetTableItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding).apply {
            itemView.setOnClickListener{
                val curPos: Int = adapterPosition
                val assetItem: TokenShow = filteredTokenShowList.get(curPos)

                val intent = Intent(binding.root.context, OrderActivity::class.java)
                intent.putExtra("assetName", assetItem.name)
                intent.putExtra("assetTicker", assetItem.code)
                intent.putExtra("currentPrice", assetItem.currentPrice.toString())
                intent.putExtra("yesterdayPrice", assetItem.priceTrend.toString())
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredTokenShowList.size
    }

    override fun getFilter(): Filter {
        return itemFilter
    }

    inner class ItemFilter: Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filterString = charSequence.toString()
            val results = FilterResults()

            val filteredList: ArrayList<TokenShow> = ArrayList<TokenShow>()
            if (filterString.trim { it <= ' ' }.isEmpty()) {
                results.values = tokenShowList
                results.count = tokenShowList.size

                return results
            }
            else {
                for(tokenShow in tokenShowList) {
                    if (tokenShow.code.contains(filterString) || tokenShow.name.contains(filterString)) {
                        filteredList.add(tokenShow)
                    }
                }
            }

            results.values = filteredList
            results.count = filteredList.size

            return results
        }
        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
            filteredTokenShowList.clear()
            filteredTokenShowList.addAll(filterResults.values as ArrayList<TokenShow>)
            notifyDataSetChanged() // RecyclerView 어댑터를 업데이트
        }
    }

    override fun onBindViewHolder(holder: AssetTableItemAdapter.CustomViewHolder, position: Int) {
        val assetItem = filteredTokenShowList[position]
        holder.bind(assetItem)
    }

    class CustomViewHolder(private val binding: AssetTableItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tokenShow: TokenShow) {

            binding.ivGraph.setImageResource(R.drawable.baseline_dashboard_customize_24)
            binding.tvItemAssetName.text = tokenShow.name
            binding.hsvAssetTableItemName.isHorizontalScrollBarEnabled = false
            binding.tvItemAssetTicker.text = tokenShow.code
            binding.ivNew.setImageResource(R.drawable.baseline_face_24)
            if(tokenShow.newFlag) {binding.ivNew.isVisible = true}
            else {
                binding.ivNew.isInvisible = true
                val params = binding.hsvAssetTableItemName.layoutParams as LinearLayout.LayoutParams
                params.weight = 5f
                binding.hsvAssetTableItemName.layoutParams = params
            }

            var color = ContextCompat.getColor(binding.root.context, R.color.black)

            if(tokenShow.priceTrend > 0) {color = ContextCompat.getColor(binding.root.context, R.color.red)}
            else if(tokenShow.priceTrend < 0) {color = ContextCompat.getColor(binding.root.context, R.color.blue)}

            binding.tvItemCurrentPrice.setTextColor(color)
            binding.tvItemYesterdayPrice.setTextColor(color)
            binding.tvItemCurrentPrice.text = tokenShow.currentPrice.toString()
            binding.tvItemYesterdayPrice.text = "${tokenShow.priceTrend.toString()}%"
            binding.tvItemTransactionPrice.text = "${tokenShow.priceTrend.toString()}만원"
        }
    }

    fun sortByAssetNameDescending() {
        filteredTokenShowList.sortByDescending { it.name }
        notifyDataSetChanged()
    }

    fun sortByAssetNameAscending() {
        filteredTokenShowList.sortBy { it.name }
        notifyDataSetChanged()
    }

}
