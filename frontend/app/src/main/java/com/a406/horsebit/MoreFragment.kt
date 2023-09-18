package com.a406.horsebit

import ExchangeFragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
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

        // lih_InOut LinearLayout에 클릭 리스너를 추가
        binding.lihInOut.setOnClickListener {

            // FragmentTransaction을 시작하여 화면 전환
            val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            val ft = requireActivity().supportFragmentManager.beginTransaction()

            val exchangeFragment = ExchangeFragment()

            ft.replace(R.id.fl_MainFrameLayout, exchangeFragment)
            ft.addToBackStack(null) // 백 스택에 추가하면 뒤로 가기 버튼으로 이전 프래그먼트로 이동 가능
            ft.commit()


        }

        // lih_EditMe LinearLayout에 클릭 리스너를 추가
        binding.lihEditMe.setOnClickListener {

            // FragmentTransaction을 시작하여 화면 전환
            val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            val ft = requireActivity().supportFragmentManager.beginTransaction()

            val myEditFragment = MyEditFragment()

            // FragmentTransaction을 시작하여 화면 전환
            ft.replace(R.id.fl_MainFrameLayout, myEditFragment) // R.id.fl_MainFrameLayout,  Fragment를 표시할 레이아웃 컨테이너의 ID입니다.
            ft.addToBackStack(null) // 뒤로 가기 버튼을 누를 때 이전 Fragment로 이동할 수 있도록 스택에 추가
            ft.commit()
        }

        return view
    }
}