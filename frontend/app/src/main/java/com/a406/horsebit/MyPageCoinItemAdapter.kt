package com.a406.horsebit


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.a406.horsebit.databinding.MyassetItemBinding

class MyPageCoinItemAdapter(val myAssetList: ArrayList<MyAsset>) : RecyclerView.Adapter<MyPageCoinItemAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageCoinItemAdapter.CustomViewHolder {
        val binding = MyassetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return myAssetList.size
    }

    override fun onBindViewHolder(holder: MyPageCoinItemAdapter.CustomViewHolder, position: Int) {
        val myAsset = myAssetList[position]
        holder.bind(myAsset)
    }



    class CustomViewHolder(private val binding: MyassetItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(myAsset: MyAsset) {
            binding.ivInformationHorseImg.setImageResource(myAsset.horseImage)
            binding.tvMyAssetCoinTitle.text = myAsset.coinTicker
            binding.tvMyAssetCoinName.text = myAsset.coinName
            binding.tvMyAssetValue.text = myAsset.value
            binding.tvMyAssetRate.text = "${myAsset.rate}%"


        }
    }
}





