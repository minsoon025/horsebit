package com.a406.horsebit

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.a406.horsebit.databinding.FragmentMyEditBinding

class MyEditFragment : Fragment() {

        private lateinit var binding: FragmentMyEditBinding
        private lateinit var spinnerBankOrSecurities: Spinner // 스피너 추가

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_my_edit, container, false)

            binding = FragmentMyEditBinding.bind(view)

        // Spinner 초기화 및 은행 목록 설정
        spinnerBankOrSecurities = view.findViewById(R.id.spinner_bank_or_securities)
        val bankList = arrayOf("  은행 / 증권사", "  신한은행", "  국민은행", "  우리은행", "  기타") // 원하는 은행 목록 추가

        // 어댑터 생성 및 설정
        val adapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            bankList
        ) {
            override fun isEnabled(position: Int): Boolean {
                // "은행 / 증권사" 항목만 비활성화
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view.findViewById<TextView>(android.R.id.text1)

                // "은행 / 증권사" 항목의 글씨를 회색으로 설정
                if (position == 0) {
                    textView.setTextColor(Color.GRAY)
                } else {
                    textView.setTextColor(Color.BLACK)
                }
                return view
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBankOrSecurities.adapter = adapter

        // 디폴트 선택 항목 설정
        spinnerBankOrSecurities.setSelection(0) // "은행 / 증권사"를 선택된 상태로 만듭니다.

        return view
        }
    }
