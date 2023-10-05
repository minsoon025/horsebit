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



        // 구글 토큰이 없다면 로그인 버튼 눌러서 진행하도록
        binding.ivGoogleLogin.setOnClickListener {
            displaySignIn()
        }
    }

    // 백에 데이터를 보내는 부분 (토큰, 프로바이더 네임)
    private fun tryLoginToServer(loadedGoogleToken: String?, isAutoLogin: Boolean) {
        if (loadedGoogleToken != null) {
            // 서버에 로그인 시도



            val loginRequestData = LoginRequestBodyModel(token = loadedGoogleToken)


            api.login(request = loginRequestData).enqueue(object : Callback<LoginResponseBodyModel> {
                override fun onResponse(call: Call<LoginResponseBodyModel>, response: Response<LoginResponseBodyModel>) {

                    if (response.code() == 400) {
                        Log.d("로그인", "로그인 400 Bad Request")

                        if (!isAutoLogin) {
                            val intent = Intent(applicationContext, LoginRegisterActivity::class.java)
                            binding.root.context.startActivity(intent)
                        }
                    } else if (response.code() == 200) {
                        Log.d("로그인", "로그인 200 OK")

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
                    Log.d("로그인", "로그인 onFailure")


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
                    Log.e("google login btn click", "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            ?.addOnFailureListener(this) { e ->
                // No Google Accounts found. Just continue presenting the signed-out UI
                displaySignUp()
                Log.d("google login btn click", e.localizedMessage!!)
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

            }
    }
}


