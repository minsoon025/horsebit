package com.a406.horsebit

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.a406.horsebit.databinding.AssetTableItemBinding


class AssetTableItemAdapter(val assetItemList: ArrayList<AssetItem>) : RecyclerView.Adapter<AssetTableItemAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetTableItemAdapter.CustomViewHolder {
        val binding = AssetTableItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding).apply {
            itemView.setOnClickListener{
                val curPos: Int = adapterPosition
                val assetItem: AssetItem = assetItemList.get(curPos)

                val intent = Intent(binding.root.context, OrderActivity::class.java)
                intent.putExtra("assetName", assetItem.assetName)
                intent.putExtra("assetTicker", assetItem.assetTicker)
                intent.putExtra("currentPrice", assetItem.currentPrice.toString())
                intent.putExtra("yesterdayPrice", assetItem.yesterdayPrice.toString())
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return assetItemList.size
    }

    override fun onBindViewHolder(holder: AssetTableItemAdapter.CustomViewHolder, position: Int) {
        val assetItem = assetItemList[position]
        holder.bind(assetItem)
    }

    class CustomViewHolder(private val binding: AssetTableItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(assetItem: AssetItem) {
            binding.ivGraph.setImageResource(R.drawable.baseline_dashboard_customize_24)
            binding.tvItemAssetName.text = assetItem.assetName
            binding.tvItemAssetTicker.text = assetItem.assetTicker
            binding.ivNew.setImageResource(R.drawable.baseline_face_24)
            if(assetItem.new) {binding.ivNew.isVisible = true}
            else {binding.ivNew.isInvisible = true}

            var color = ContextCompat.getColor(binding.root.context, R.color.black)

            if(assetItem.yesterdayPrice > 0) {color = ContextCompat.getColor(binding.root.context, R.color.red)}
            else if(assetItem.yesterdayPrice < 0) {color = ContextCompat.getColor(binding.root.context, R.color.blue)}

            binding.tvItemCurrentPrice.setTextColor(color)
            binding.tvItemYesterdayPrice.setTextColor(color)
            binding.tvItemCurrentPrice.text = assetItem.currentPrice.toString()
            binding.tvItemYesterdayPrice.text = "${assetItem.yesterdayPrice.toString()}%"
            binding.tvItemTransactionPrice.text = "${assetItem.transactionPrice.toString()}만원"
        }
    }

}