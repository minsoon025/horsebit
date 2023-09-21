package com.a406.horsebit

import android.content.Intent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.a406.horsebit.databinding.AssetTableItemBinding

class AssetTableItemAdapter(val tokenShowList: ArrayList<TokenShow>) : RecyclerView.Adapter<AssetTableItemAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetTableItemAdapter.CustomViewHolder {
        val binding = AssetTableItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding).apply {
            itemView.setOnClickListener{
                val curPos: Int = adapterPosition
                val assetItem: TokenShow = tokenShowList.get(curPos)

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
        return tokenShowList.size
    }

    override fun onBindViewHolder(holder: AssetTableItemAdapter.CustomViewHolder, position: Int) {
        val assetItem = tokenShowList[position]
        holder.bind(assetItem)
    }

    class CustomViewHolder(private val binding: AssetTableItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tokenShow: TokenShow) {
            binding.ivGraph.setImageResource(R.drawable.baseline_dashboard_customize_24)
            binding.tvItemAssetName.text = tokenShow.name
            binding.tvItemAssetTicker.text = tokenShow.code
            binding.ivNew.setImageResource(R.drawable.baseline_face_24)
            if(tokenShow.newFlag) {binding.ivNew.isVisible = true}
            else {binding.ivNew.isInvisible = true}

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

}