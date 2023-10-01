package com.a406.horsebit

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.a406.horsebit.databinding.ActivityLoginMainBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginMainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginMainBinding.inflate(layoutInflater)
    }

    private val api = APIS.create()

    private var oneTapClient: SignInClient? = null
    private var signUpRequest: BeginSignInRequest? = null
    private var signInRequest: BeginSignInRequest? = null

    private val oneTapResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            try {
                val credential = oneTapClient?.getSignInCredentialFromIntent(result.data)

                val idToken = credential?.googleIdToken
                print("이것은 아이디토큰입니다!!!!!!!! "+ idToken)
                when {
                    idToken != null -> {
                        Log.d("이것은 아이디토큰입니다!!!!!!!! ", idToken)
                        // sharedPreference에 저장
                        val pref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                        val editor = pref.edit()
                        editor.putString("GOOGLE_TOKEN", idToken).apply()

                        tryLoginToServer(idToken, isAutoLogin = false)
                    }

                    else -> {
                        Snackbar.make(binding.root, "No ID token!", Snackbar.LENGTH_INDEFINITE)
                            .show()
                    }
                }

            } catch (e: ApiException) {
                when (e.statusCode) {
                    CommonStatusCodes.CANCELED -> {
                        Log.d("google one tap", "One-tap dialog was closed")
                    }

                    CommonStatusCodes.NETWORK_ERROR -> {
                        Log.d("google one tap", "One-tap encountered a network error")
                        Snackbar.make(
                            binding.root,
                            "One-tap encountered a network error",
                            Snackbar.LENGTH_INDEFINITE
                        ).show()
                    }

                    else -> {
                        Log.d(
                            "google one tap",
                            "Couldn't get credential from result (${e.localizedMessage})"
                        )
                        Snackbar.make(
                            binding.root,
                            "Couldn't get credential from result (${e.localizedMessage})",
                            Snackbar.LENGTH_INDEFINITE
                        ).show()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val edit = pref.edit()

        binding.tvLoginPass.setOnClickListener {
            edit.putBoolean("firstLoginChk", false)
            edit.apply()

            val intent = Intent(binding.root.context, MainActivity::class.java)
            binding.root.context.startActivity(intent)
        }

        oneTapClient = Identity.getSignInClient(this)
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID
                    .setServerClientId(getString(R.string.your_web_client_id))
                    // Show all accounts on the device
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID
                    .setServerClientId(getString(R.string.your_web_client_id))
                    // Show all accounts on the device
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .build()

//        // 이미 구글 토큰을 발급 받았을 경우 자동 로그인 시도
//        val loadedGoogleToken = pref.getString("GOOGLE_TOKEN", null)
//        tryLoginToServer(loadedGoogleToken, isAutoLogin = true)

        // 구글 토큰이 없다면 로그인 버튼 눌러서 진행하도록
        binding.ivGoogleLogin.setOnClickListener {
            displaySignIn()
        }
    }

    // 백에 데이터를 보내는 부분 (토큰, 프로바이더 네임)
    private fun tryLoginToServer(loadedGoogleToken: String?, isAutoLogin: Boolean) {
        if (loadedGoogleToken != null) {
            // 서버에 로그인 시도

//            val pref = PreferenceManager.getDefaultSharedPreferences(this)
//            val edit = pref.edit()
//            edit.putBoolean("firstLoginChk", false).apply()


            val loginRequestData = LoginRequestBodyModel(token = loadedGoogleToken)


            api.login(request = loginRequestData).enqueue(object : Callback<LoginResponseBodyModel> {
                override fun onResponse(call: Call<LoginResponseBodyModel>, response: Response<LoginResponseBodyModel>) {
                    Log.d("로그 응답 코드를 확인합니다", response.code().toString())

                    if (response.code() == 400) {
                        Log.d("로그11111", "로그인 400 Bad Request")

                        // 오류 메시지를 좀 더 자세히 출력
                        val errorBodyStr = response.errorBody()?.string()
                        Log.d("로그 400 오류 상세", errorBodyStr ?: "오류 응답 본문이 없습니다")

                        if (!isAutoLogin) {
                            val intent = Intent(applicationContext, LoginRegisterActivity::class.java)
                            binding.root.context.startActivity(intent)
                        }
                    } else if (response.code() == 200) {
                        Log.d("로그2000000 서버에 보내졋다!!!", "로그인 200 OK")

                        val responseBody = response.body()
                        if (responseBody != null) {
                            val loginModel = responseBody

                            val pref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                            val editor = pref.edit()
                            editor.putString("SERVER_ACCESS_TOKEN", loginModel.accessToken)
                                .putString("SERVER_REFRESH_TOKEN", loginModel.refreshToken)
                                .putString("SERVER_USER_EMAIL", loginModel.userDTO.email)
                                .putString("NICK_NAME", loginModel.userDTO.nickname)
                                .putString("USER_NAME", loginModel.userDTO.userName)
                                .putString("ID", loginModel.userDTO.id.toString())
                                .putString("SERVER_REFRESH_TOKEN", loginModel.userDTO.refreshToken)
                                .putBoolean("firstLoginChk", false)
                                .apply()


                            val intent = Intent(binding.root.context, MainActivity::class.java)
                            binding.root.context.startActivity(intent)
                        }
                    }
                }

                override fun onFailure(call: Call<LoginResponseBodyModel>, t: Throwable) {
                    Log.d("로그???????????", "로그인 onFailure")

                    // 로그인 실패 시 회원가입 페이지로 이동 (토큰은 발급받은 상태)
                    // 거기서 회원가입 정보를 입력 받고 회원가입 진행
                    // 자동 로그인 때는 이 작업을 강제시키지 말자
                    // 네트워크 에러인 경우, 저 주소를 찾을 수 없다고 나온다s

                    val intent = Intent(binding.root.context, LoginMainActivity::class.java)
                    binding.root.context.startActivity(intent)
                }
            })
        }
    }


    private fun displaySignIn() {
        oneTapClient?.beginSignIn(signInRequest!!)
            ?.addOnSuccessListener(this) { result ->
                try {
                    val ib = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    oneTapResult.launch(ib)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("1111111 google login btn click", "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            ?.addOnFailureListener(this) { e ->
                // No Google Accounts found. Just continue presenting the signed-out UI
                displaySignUp()
                Log.d("2222222google login btn click", e.localizedMessage!!)
            }
    }

    private fun displaySignUp() {
        oneTapClient?.beginSignIn(signUpRequest!!)
            ?.addOnSuccessListener(this) { result ->
                try {
                    val ib = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    oneTapResult.launch(ib)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("google login btn click", "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            ?.addOnFailureListener(this) { e ->
                // No Google Accounts found. Just continue presenting the signed-out UI
                // displaySignUp()
                Log.d("google login btn click", e.localizedMessage!!)
                Log.d("333333333", e.toString())
            }
    }
}


