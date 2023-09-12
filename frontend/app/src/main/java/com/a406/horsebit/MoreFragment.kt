package com.a406.horsebit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a406.horsebit.databinding.FragmentMoreBinding

class MoreFragment : Fragment() {

    private lateinit var binding: FragmentMoreBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_more, container, false)

        binding = FragmentMoreBinding.bind(view)


        // lih_Notice LinearLayout에 클릭 리스너를 추가
        binding.lihNotice.setOnClickListener {
            // 웹 페이지로 이동할 URL 정의
            val websiteUrl = "https://m.kra.co.kr/park/jeju/parkNoticeList.do"

            // 웹 브라우저를 열기 위한 인텐트 생성
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))

            // 액티비티를 실행하여 웹 페이지로 이동
            startActivity(intent)
        }

        return view
    }
}