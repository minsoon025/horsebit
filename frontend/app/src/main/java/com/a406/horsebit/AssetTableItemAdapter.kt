package com.a406.horsebit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.a406.horsebit.databinding.AssetTableItemBinding
import kotlinx.coroutines.NonDisposableHandle.parent


class AssetTableItemAdapter(val assetItemList: ArrayList<AssetItem>) : RecyclerView.Adapter<AssetTableItemAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetTableItemAdapter.CustomViewHolder {
        val binding = AssetTableItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding)
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
            binding.tvItemCurrentPrice.text = assetItem.currentPrice.toString()
            binding.tvItemYesterdayPrice.text = assetItem.yesterdayPrice.toString()
            binding.tvItemTransactionPrice.text = assetItem.transactionPrice.toString()
        }
    }

}