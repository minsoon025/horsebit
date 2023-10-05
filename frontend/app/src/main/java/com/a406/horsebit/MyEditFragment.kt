package com.a406.horsebit

import MoreFragment
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.a406.horsebit.databinding.FragmentMyEditBinding

class MyEditFragment : Fragment() {

    private lateinit var binding: FragmentMyEditBinding
    private lateinit var spinnerBankOrSecurities: Spinner
    private lateinit var tvSend: TextView
    private fun showEditNamePopup() {
        // 팝업창을 위한 레이아웃을 가져옵니다.
        val popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_edit_name, null)

        // 팝업창 내부의 뷰를 참조합니다.
        val editNameEditText = popupView.findViewById<EditText>(R.id.editNameEditText)
        val confirmButton = popupView.findViewById<Button>(R.id.confirmButton)
        val cancelButton = popupView.findViewById<Button>(R.id.cancelButton)

        // AlertDialog를 생성하고 팝업창을 설정합니다.
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(popupView)
        val alertDialog = builder.create()

        // "확인" 버튼 클릭 이벤트 처리
        confirmButton.setOnClickListener {
            val newName = editNameEditText.text.toString()
            // 여기에서 새로운 이름을 처리하고 저장할 수 있습니다.
            Toast.makeText(binding.root.context,"구글에 새로운 이름을 전송하였습니다. 심사 중 입니다.", Toast.LENGTH_SHORT).show()

            alertDialog.dismiss() // 팝업창을 닫습니다.
        }

        // "취소" 버튼 클릭 이벤트 처리
        cancelButton.setOnClickListener {
            alertDialog.dismiss() // 팝업창을 닫습니다.
        }

        // 팝업창을 표시합니다.
        alertDialog.show()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_edit, container, false)

        binding = FragmentMyEditBinding.bind(view)

        // 이름 변경 팝업 / 일단 안됨
        binding.btnEditName.setOnClickListener {
            showEditNamePopup()
        }

        binding.tvSend.setOnClickListener {
            Toast.makeText(binding.root.context,"계좌정보가 등록 되었습니다.", Toast.LENGTH_SHORT).show()

        }


        // 회원탈퇴 - 팝업 추가
        binding.tvOut.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("경고")
                .setMessage(" 홀스빗을 떠나시겠습니까??")
                .setPositiveButton("ok", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        Log.d("MyTag", "positive")
                        val intent = Intent(binding.root.context, LoginMainActivity::class.java)
                        binding.root.context.startActivity(intent)

                    }
                })
                .setNegativeButton("cancel", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        Log.d("MyTag", "negative")
                    }
                })
                .setNeutralButton("후원하기", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        Log.d("MyTag", "neutral")
                        Toast.makeText(binding.root.context,"김민태 국민 517102-01-441818", Toast.LENGTH_SHORT).show()

                    }
                })
                .create()
                .show()
        }

        binding.tvEditBack.setOnClickListener {
            val ft = requireActivity().supportFragmentManager.beginTransaction()
            val moreFragment = MoreFragment()
            ft.replace(R.id.fl_MainFrameLayout, moreFragment)
            ft.addToBackStack(null)
            ft.commit()
        }

        binding.btnLogout.setOnClickListener {
            val intent = Intent(binding.root.context, LoginMainActivity::class.java)
            binding.root.context.startActivity(intent)
        }

        spinnerBankOrSecurities = view.findViewById(R.id.spinner_bank_or_securities)
        val bankList = arrayOf("  은행 / 증권사", "  신한은행", "  국민은행", "  우리은행", "  부산은행",  "  기타")

        val adapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            bankList
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view.findViewById<TextView>(android.R.id.text1)

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

        spinnerBankOrSecurities.setSelection(0)

        return view
    }
}
