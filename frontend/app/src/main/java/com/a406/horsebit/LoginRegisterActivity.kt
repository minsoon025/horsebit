package com.a406.horsebit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.a406.horsebit.databinding.ActivityLoginRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginRegisterBinding
    private val api = APIS.create();

    private var checkflag = arrayOf(false,false,false)
    private var flag = arrayOf(false,false,false)


//    private val binding by lazy {
//        ActivityLoginRegisterBinding.inflate(layoutInflater)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()  // loadData()에서 토큰을 반환하도록 수정


        // tvLookingText1의 클릭 리스너를 먼저 설정합니다.
        binding.llhRegisterWord1.setOnClickListener {
            // LinearLayout의 visibility를 visible로 변경합니다.
            if (binding.llhRegisterGone1.visibility == View.VISIBLE) {
                binding.llhRegisterGone1.visibility = View.GONE
            } else {
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

        binding.llhRegisterWord2.setOnClickListener {
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







        binding.llhRegisterWord4.setOnClickListener {
            if (binding.llhRegisterGone4.visibility == View.VISIBLE) {
                binding.llhRegisterGone4.visibility = View.GONE
            }
            else {
                binding.llhRegisterGone4.visibility = View.VISIBLE
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

    private fun loadData(): String {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        return pref.getString("token", "") ?: ""
    }

    private fun saveData(nickname: String) {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val edit = pref.edit()

        // nickname 매개 변수를 제대로 사용하도록 수정:
        edit.putString("userName", nickname)
        edit.apply()

        val intent = Intent(this@LoginRegisterActivity, LoginMainActivity::class.java)
        startActivity(intent)
    }

    private fun registerPossible() {
        if (checkflag[0] && checkflag[1]) {
            binding.vRegisterFinal.setBackgroundResource(R.drawable.rounded_shape_green_ractangle)
            binding.flRegisterFinal.setOnClickListener {

                val pref = PreferenceManager.getDefaultSharedPreferences(this)
                val token: String = pref.getString("GOOGLE_TOKEN", "") ?: ""
                val userName = binding.etNicknameMake.text.toString() // 실제 userName 값을 할당해야 합니다.

                val signUpRequest = SignUpRequestBodyModel(token = token, userName = userName)


                val accesstoken = pref.getString("SERVER_ACCESS_TOKEN", "1")

                api.SingUp(authorization = "Bearer ${accesstoken}", request = signUpRequest).enqueue(object:
                    Callback<SignUpResponseBodyModel> {
                    override fun onResponse(call: Call<SignUpResponseBodyModel>, response: Response<SignUpResponseBodyModel>) {
                        if(response.code() == 200) {    // 200 Success
                            Log.d("로그", "회원 가입: 200 Success")

                            val responseBody = response.body()


                            Log.d("dddd", responseBody.toString())


                        }
                        else if(response.code() == 400) {   // 400 Bad Request - Message에 누락 필드명 기입
                            Log.d("로그", "회원 가입: 400 Bad Request")
                        }
                        else if(response.code() == 401) {   // 401 Unauthorized - 인증 토큰값 무효
                            Log.d("로그", "회원 가입: 401 Unauthorized")
                        }
                        else if(response.code() == 404) {   // 404 Not Found
                            Log.d("로그", "회원 가입: 404 Not Found")
                        }
                    }
                    override fun onFailure(call: Call<SignUpResponseBodyModel>, t: Throwable) {
                        Log.d("로그", "회원 가입: onFailure")
                    }
                })

                val intent = Intent(this@LoginRegisterActivity, MainActivity::class.java)
                startActivity(intent)

            }
            if (checkflag[2]) {
                binding.ivCircleCheck.setBackgroundResource(R.drawable.rounded_shape_green_ractangle)
                binding.flRegisterFinal.setOnClickListener {

                    val pref = PreferenceManager.getDefaultSharedPreferences(this)
                    val token: String = pref.getString("GOOGLE_TOKEN", "") ?: ""
                    val accesstoken = pref.getString("SERVER_ACCESS_TOKEN", "1")
                    val userName = binding.etNicknameMake.text.toString() // 실제 userName 값을 할당해야 합니다.

                    val signUpRequest = SignUpRequestBodyModel(token = token, userName = userName)


                    api.SingUp(authorization = "Bearer ${accesstoken}", request = signUpRequest).enqueue(object: Callback<SignUpResponseBodyModel> {
                        override fun onResponse(call: Call<SignUpResponseBodyModel>, response: Response<SignUpResponseBodyModel>) {
                            if (response.isSuccessful) {
                                val responseBody = response.body()
                                if (responseBody != null) {
                                    Log.d("로그", "회원 가입: 200 Success")

                                    val responseBody = response.body()



                                } else if (response.code() == 400) {   // 400 Bad Request - Message에 누락 필드명 기입
                                    Log.d("로그", "회원 가입: 400 Bad Request")
                                } else if (response.code() == 401) {   // 401 Unauthorized - 인증 토큰값 무효
                                    Log.d("로그", "회원 가입: 401 Unauthorized")
                                } else if (response.code() == 404) {   // 404 Not Found
                                    Log.d("로그", "회원 가입: 404 Not Found")
                                }
                            }
                        }
                        override fun onFailure(call: Call<SignUpResponseBodyModel>, t: Throwable) {
                            Log.d("로그", "회원 가입: onFailure")
                        }
                    })

                    val intent = Intent(this@LoginRegisterActivity, MainActivity::class.java)
                    startActivity(intent)

                }
            }
        }



    }
}
