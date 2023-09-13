package com.a406.horsebit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a406.horsebit.databinding.FragmentMyEditBinding

class MyEditFragment : Fragment() {

        private lateinit var binding: FragmentMyEditBinding

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_my_edit, container, false)

            binding = FragmentMyEditBinding.bind(view)

            return view
        }
    }
