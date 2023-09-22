package com.a406.horsebit

import MoreFragment
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.a406.horsebit.databinding.FragmentMyEditBinding
import com.google.android.material.button.MaterialButton

class MyEditFragment : Fragment() {

    private lateinit var binding: FragmentMyEditBinding
    private lateinit var spinnerBankOrSecurities: Spinner
    private lateinit var tvSend: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_edit, container, false)

        binding = FragmentMyEditBinding.bind(view)

        // 이름 변경 팝업 / 일단 안됨
        binding.btnEditName.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setView(R.layout.fragment_my_edit)
                .show()
                .also { alertDialog ->

                    if (alertDialog == null) {
                        return@also
                    }

                    val userName = alertDialog.findViewById<EditText>(R.id.tv_EditName)?.text
                    val button = alertDialog.findViewById<MaterialButton>(R.id.btn_EditName)

                    button?.setOnClickListener {
                        alertDialog.dismiss()
                        Log.d("MyTag", "userName : $userName")
                    }
                }
        }


        // 회원탈퇴 - 팝업 추가
        binding.tvOut.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("경고")
                .setMessage(" 홀스빗을 떠나시겠습니까??")
                .setPositiveButton("ok", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        Log.d("MyTag", "positive")
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
            val ft = requireActivity().supportFragmentManager.beginTransaction()
            val homeFragment = HomeFragment()
            ft.replace(R.id.fl_MainFrameLayout, homeFragment)
            ft.addToBackStack(null)
            ft.commit()
        }

        spinnerBankOrSecurities = view.findViewById(R.id.spinner_bank_or_securities)
        val bankList = arrayOf("  은행 / 증권사", "  신한은행", "  국민은행", "  우리은행", "  기타")

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
