package com.a406.horsebit


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.a406.horsebit.databinding.MyassetItemBinding
class MyPageCoinItemAdapter(val myassetItemList: ArrayList<MyAssetModel>) : RecyclerView.Adapter<MyPageCoinItemAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageCoinItemAdapter.CustomViewHolder {
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



    class CustomViewHolder(private val binding: MyassetItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(myAsset: MyAssetModel) {
            val num = myAsset.tokenNo // 이미지 선택에 사용할 값
            val resourceName = "pic_$num" // 이미지 리소스 이름 (확장자 제외)

            // 리소스 이름을 사용하여 리소스 ID를 가져옵니다.
            val imageResourceID = itemView.resources.getIdentifier(resourceName, "drawable", itemView.context.packageName)

            // 이미지 리소스 ID를 확인하고 설정합니다.
            Log.d("마이ㅣㅣㅣㅣㅣㅣ토큰 넘버값",myAsset.toString())
            Log.d("넘버입니다dfdfdfs",num.toString())
            Log.d("리소스네임입니다아ㅏㅏㅏㅏㅏㅏㅏㅏㅏdfdfdfs",resourceName.toString())
                Log.d("!!!!!!!!!dfdfdfs",imageResourceID.toString())
                binding.ivInformationHorseImg.setImageResource(imageResourceID)


            // 나머지 데이터 설정
            binding.tvMyAssetCoinTitle.text = myAsset.name
            binding.tvMyAssetCoinName.text = myAsset.code
            binding.tvMyAssetValue.text = myAsset.profitOrLoss
            binding.tvMyAssetRate.text = "${myAsset.returnRate}%"
        }

    }
}





