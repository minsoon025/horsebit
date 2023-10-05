package com.a406.horsebit


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.a406.horsebit.databinding.MyassetItemBinding
class MyPageCoinItemAdapter(val myassetItemList: ArrayList<MyAssetResponseBodyModel>) : RecyclerView.Adapter<MyPageCoinItemAdapter.CustomViewHolder>() {

//    var myassetList = ArrayList<MyAssetResponseBodyModel>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyPageCoinItemAdapter.CustomViewHolder {
        val binding = MyassetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return myassetItemList.size
    }

    override fun onBindViewHolder(holder: MyPageCoinItemAdapter.CustomViewHolder, position: Int) {
        val myAsset = myassetItemList[position]
        holder.bind(myAsset)
    }


    inner class CustomViewHolder(private val binding: MyassetItemBinding) :

        RecyclerView.ViewHolder(binding.root) {

        fun bind(myAsset: MyAssetResponseBodyModel) {


            val num = myAsset.tokenNo // 이미지 선택에 사용할 값
            val resourceName = "pic_$num" // 이미지 리소스 이름 (확장자 제외)

            // 리소스 이름을 사용하여 리소스 ID를 가져옵니다.
            val imageResourceID = itemView.resources.getIdentifier(
                resourceName,
                "drawable",
                itemView.context.packageName
            )

            // 이미지 리소스 ID를 확인하고 설정합니다.
            binding.ivInformationHorseImg.setImageResource(imageResourceID)
9
            binding.llvMyAssetItem.setOnClickListener {
                val curPos: Int = adapterPosition
                val mycoinList: MyAssetResponseBodyModel = myassetItemList[curPos] // myassetItemList로 수정
                if (mycoinList.tokenNo.toLong() != 0L) {
                    val intent = Intent(binding.root.context, OrderActivity::class.java)
                    intent.putExtra("tokenNo", mycoinList.tokenNo)
                    binding.root.context.startActivity(intent)
                }
            }

            // 나머지 데이터 설정
            binding.tvMyAssetCoinTitle.text = myAsset.name
            binding.tvMyAssetCoinName.text = myAsset.code
            binding.tvMyAssetValue.text = myAsset.profitOrLoss
            binding.tvMyAssetRate.text = "${myAsset.returnRate}%"
        }
    }
}





