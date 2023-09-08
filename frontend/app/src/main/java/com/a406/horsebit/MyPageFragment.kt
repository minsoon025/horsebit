package com.a406.horsebit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a406.horsebit.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment() {

    private lateinit var binding: FragmentMyPageBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my_page, container, false)

        binding = FragmentMyPageBinding.bind(view)

        return view
    }
}