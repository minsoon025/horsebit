package com.a406.horsebit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.a406.horsebit.databinding.ActivityLoginRegisterBinding

class LoginRegisterActivity : AppCompatActivity() {

    private var checkflag = arrayOf(false,false,false)
    private var flag = arrayOf(false,false,false)


    private val binding by lazy {
        ActivityLoginRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)





        // tvLookingText1의 클릭 리스너를 먼저 설정합니다.
        binding.tvLookingText1.setOnClickListener {
            // LinearLayout의 visibility를 visible로 변경합니다.
            if (binding.llhRegisterGone1.visibility == View.VISIBLE) {
                binding.llhRegisterGone1.visibility = View.GONE
            } else {
                binding.llhRegisterGone1.visibility = View.VISIBLE
                Toast.makeText(getApplicationContext(), "모든 약관을 확인하세요!", Toast.LENGTH_LONG).show();
            }
        }



        binding.tvUnderAgree1.setOnClickListener {
            // LinearLayout의 visibility를 visible로 변경합니다.

            if (binding.llhRegisterGone1.visibility == View.VISIBLE) {
                binding.llhRegisterGone1.visibility = View.GONE
            }
            else {
                binding.llhRegisterGone1.visibility = View.VISIBLE
                Toast.makeText(getApplicationContext(), "모든 약관을 확인하세요!", Toast.LENGTH_LONG).show();
            }

        }


        // ScrollView의 스크롤 리스너를 설정합니다.
        binding.svGone1.setOnScrollChangeListener { _, _, scrollY1, _, _ ->
            val totalHeight = binding.svGone1.getChildAt(0).height - binding.svGone1.height

            if (scrollY1 >= totalHeight) {
                flag[0] = true
            }
            // 스크롤 위치가 맨 아래로 도달하면 이미지를 변경합니다.
            if (flag[0]) {
                // 이미 클릭 리스너가 설정되어 있다면, 이미지를 변경합니다.
                binding.tvLookingText1.setOnClickListener {
                    binding.ivGreyCheck1.setImageResource(R.drawable.ok_green)
                    checkflag[0] = true
                    checkflag[0] = !checkflag[0]
                    registerPossible ()
                }
                binding.tvUnderAgree1.setOnClickListener {
                    binding.ivGreyCheck1.setImageResource(R.drawable.ok_green)
                    checkflag[0] = true
                    registerPossible ()
                }
            }
        }

        binding.tvLookingText2.setOnClickListener {
            // LinearLayout의 visibility를 visible로 변경합니다.
            if (binding.llhRegisterGone2.visibility == View.VISIBLE) {
                binding.llhRegisterGone2.visibility = View.GONE
            } else {
                binding.llhRegisterGone2.visibility = View.VISIBLE
                Toast.makeText(applicationContext, "모든 약관을 확인하세요!", Toast.LENGTH_LONG).show()
            }
        }

        binding.tvUnderAgree2.setOnClickListener {
            // LinearLayout의 visibility를 visible로 변경합니다.
            if (binding.llhRegisterGone2.visibility == View.VISIBLE) {
                binding.llhRegisterGone2.visibility = View.GONE
            } else {
                binding.llhRegisterGone2.visibility = View.VISIBLE
                Toast.makeText(applicationContext, "모든 약관을 확인하세요!", Toast.LENGTH_LONG).show()
            }
        }

// ScrollView의 스크롤 리스너를 설정합니다.
        binding.svGone2.setOnScrollChangeListener { _, _, scrollY2, _, _ ->
            val totalHeight = binding.svGone2.getChildAt(0).height - binding.svGone2.height

            if (scrollY2 >=  totalHeight) {
                flag[1] = true
                     }
                if (flag[1]) {
                    binding.tvLookingText2.setOnClickListener {
                        binding.ivGreyCheck2.setImageResource(R.drawable.ok_green)
                        checkflag[1] = true
                        registerPossible ()
                    }
                    binding.tvUnderAgree2.setOnClickListener {
                        binding.ivGreyCheck2.setImageResource(R.drawable.ok_green)
                        checkflag[1] = true
                        registerPossible ()
                    }
            }
        }







        binding.tvLookingText4.setOnClickListener {
            if (binding.llhRegisterGone4.visibility == View.VISIBLE) {
                binding.llhRegisterGone4.visibility = View.GONE
            }
            else {
                binding.llhRegisterGone4.visibility = View.VISIBLE
            }

        }

        binding.tvUnderAgree4.setOnClickListener {
            if (binding.llhRegisterGone4.visibility == View.VISIBLE) {
                binding.llhRegisterGone4.visibility = View.GONE
            }
            else {
                binding.llhRegisterGone4.visibility = View.VISIBLE
                binding.ivGreyCheck4.setImageResource(R.drawable.ok_green)
            }

        }

        // ScrollView의 스크롤 리스너를 설정합니다.
        binding.svGone3.setOnScrollChangeListener { _, _, scrollY3, _, _ ->
            val totalHeight = binding.svGone3.getChildAt(0).height - binding.svGone3.height

            if (scrollY3 >= totalHeight) {
                flag[2] = true
            }
            // 스크롤 위치가 맨 아래로 도달하면 이미지를 변경합니다.
            if (flag[2]) {
                // 이미 클릭 리스너가 설정되어 있다면, 이미지를 변경합니다.
                binding.tvLookingText4.setOnClickListener {
                    binding.ivGreyCheck4.setImageResource(R.drawable.ok_green)
                    checkflag[2] = true
                    registerPossible ()
                }
                binding.tvUnderAgree4.setOnClickListener {
                    binding.ivGreyCheck4.setImageResource(R.drawable.ok_green)
                    checkflag[2] = true
                    registerPossible ()
                }
            }
        }








    }
    private fun registerPossible() {
        if (checkflag[0] && checkflag[1]) {
            binding.vRegisterFinal.setBackgroundResource(R.drawable.rounded_shape_green_ractangle)
            binding.flRegisterFinal.setOnClickListener {
                val intent = Intent(this@LoginRegisterActivity, MainActivity::class.java)
                startActivity(intent)
            }
            if (checkflag[2]) {
                binding.ivCircleCheck.setBackgroundResource(R.drawable.rounded_shape_green_ractangle)
            }
        }
    }

}
