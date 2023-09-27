// ExchangeFragment.kt

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a406.horsebit.APIS
import com.a406.horsebit.ExchangeDataResponseBodyModel
import com.a406.horsebit.ExchangeTableAdapter
import com.a406.horsebit.MyTotalAssetResponseBodyModel
import com.a406.horsebit.R
import com.a406.horsebit.databinding.FragmentExchangeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExchangeFragment : Fragment() {

    private lateinit var binding: FragmentExchangeBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var exchangeTableAdapter: ExchangeTableAdapter

    private val api = APIS.create();
    var exchangeList: ArrayList<ExchangeDataResponseBodyModel> = ArrayList()

    private fun showTransactionPopup() {
        // 팝업창을 위한 레이아웃을 가져옵니다.
        val popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_transaction, null)

        // 팝업창 내부의 뷰를 참조합니다.
        val amountEditText = popupView.findViewById<EditText>(R.id.amountEditText)
        val depositRadioButton = popupView.findViewById<RadioButton>(R.id.depositRadioButton)
        val withdrawRadioButton = popupView.findViewById<RadioButton>(R.id.withdrawRadioButton)
        val sendButton = popupView.findViewById<Button>(R.id.sendButton)
        val closeButton = popupView.findViewById<Button>(R.id.closeButton)

        // AlertDialog를 생성하고 팝업창을 설정합니다.
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(popupView)
        val alertDialog = builder.create()

        // "보내기" 버튼 클릭 이벤트 처리
        sendButton.setOnClickListener {
            val amount = amountEditText.text.toString()
            val transactionType = when {
                depositRadioButton.isChecked -> "입금"
                withdrawRadioButton.isChecked -> "출금"
                else -> "선택 안 함"
            }

            // TODO: 입력된 금액과 입출금 유형을 처리하거나 전달합니다.
            // 여기에 데이터 처리 로직을 추가하세요.

            alertDialog.dismiss() // 팝업창을 닫습니다.
        }

        // "닫기" 버튼 클릭 이벤트 처리
        closeButton.setOnClickListener {
            alertDialog.dismiss() // 팝업창을 닫습니다.
        }

        // 팝업창을 표시합니다.
        alertDialog.show()
    }


    override fun onCreateView( inflater: LayoutInflater,  container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {



        binding = FragmentExchangeBinding.inflate(inflater, container, false)
        val view = binding.root

        recyclerView = view.findViewById(R.id.rv_ExchangeTable)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        // 데이터 생성
        val newData = ExchangeDataResponseBodyModel(
            executionTime = "채결시간",
            tokenNo = 1,
            code = "코인명",
            transactionType = "종류",
            volume = "거래수량",
            price = "거래단가",
            transactionAmount = "거래금액",
            fee = "수수료",
            amount = "정산금액",
            orderTime = "주문시간",
        )

        // 데이터 리스트에 추가
        exchangeList.add(0, newData) // 0 인덱스에 추가하면 맨 위에 추가됩니다.
        exchangeTableAdapter = ExchangeTableAdapter(exchangeList)

        // 어댑터 설정 (여기서 dataList는 데이터 리스트로 대체해야 합니다)
        val dataList = ArrayList<ExchangeDataResponseBodyModel>() // 실제 데이터 리스트로 대체
        exchangeTableAdapter = ExchangeTableAdapter(exchangeList)
        recyclerView.adapter = exchangeTableAdapter


        binding.rvExchangeTable.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvExchangeTable.setHasFixedSize(true)

        //exchangeList.clear()

        // 어뎁터 불러와서 값 돌리기
        api.ExchangeDataModel(authorization = "Bearer ${1}").enqueue(object:
        Callback<ArrayList<ExchangeDataResponseBodyModel>> {
                override fun onResponse(call: Call<ArrayList<ExchangeDataResponseBodyModel>>,
                                    response: Response<ArrayList<ExchangeDataResponseBodyModel>>) {
                if(response.code() == 200) {    // 200 Success
                    Log.d("로그", "입출금 조회 조회: 200 Success")
                    val responseBody = response.body()
                    if (responseBody != null) {
                        for (coin in responseBody) {
                            exchangeList.add(coin)
                        }
                    }
                    exchangeTableAdapter = ExchangeTableAdapter(exchangeList)
                    binding.rvExchangeTable.adapter = exchangeTableAdapter
                }
                else if(response.code() == 400) {   // 400 Bad Request - Message에 누락 필드명 기입
                    Log.d("로그", "입출금 조회 조회: 400 Bad Request")
                }
                else if(response.code() == 401) {   // 401 Unauthorized - 인증 토큰값 무효
                    Log.d("로그", "입출금 조회 조회: 401 Unauthorized")
                }
                else if(response.code() == 403) {
                    Log.d("로그", "입출금 조회 조회: 403 Forbidden")
                }
                else if(response.code() == 404) {   // 404 Not Found
                    Log.d("로그", "입출금 조회 조회: 404 Not Found")
                }
            }
            override fun onFailure(call: Call<ArrayList<ExchangeDataResponseBodyModel>>, t: Throwable) {
                Log.d("로그", "입출금 조회 조회: onFailure")
                Log.d("ddddd", t.toString())
            }
        })

        //보유 krw API
        api.MyTotalAsset(authorization = "Bearer ${1}").enqueue(object : Callback<MyTotalAssetResponseBodyModel> {
            override fun onResponse(call: Call<MyTotalAssetResponseBodyModel>, response: Response<MyTotalAssetResponseBodyModel>) {
                if (response.code() == 200) {    // 200 Success
                    Log.d("로그", "내 자산 전체 조회: 200 Success")

                    val responseBody = response.body()

                    if (responseBody != null) {
                        // totalAssetList에서 총 자산 값 가져오기
                        val cashBalanceValue = responseBody.cashBalance


                        // TextView에 총 자산 값 설정
                        binding.tvMyKrw.text = cashBalanceValue.toString()
                    }
                }
                else if(response.code() == 400) {   // 400 Bad Request - Message에 누락 필드명 기입
                    Log.d("로그", "내 자산 전체 조회: 400 Bad Request")
                }
                else if(response.code() == 401) {   // 401 Unauthorized - 인증 토큰값 무효
                    Log.d("로그", "내 자산 전체 조회: 401 Unauthorized")
                }
                else if(response.code() == 403) {
                    Log.d("로그", "내 자산 전체 조회: 403 Forbidden")
                }
                else if(response.code() == 404) {   // 404 Not Found
                    Log.d("로그", "내 자산 전체 조회: 404 Not Found")
                }
            }
            override fun onFailure(call: Call<MyTotalAssetResponseBodyModel>, t: Throwable) {
                Log.d("로그", "내 자산 전체 조회: onFailure")

            }
        })



        // 입출금 팝업 뜨는 곳
        binding.btnExchange.setOnClickListener {
            authenticateToEncrypt() // 지문인식
        }

        return view
    }
    private fun setPromptInfo(): BiometricPrompt.PromptInfo {
        val promptBuilder = BiometricPrompt.PromptInfo.Builder()

        promptBuilder.setTitle("Biometric login for my app")
        promptBuilder.setSubtitle("Log in using your biometric credential")
        promptBuilder.setNegativeButtonText("Use account password")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            promptBuilder.setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
        }

        return promptBuilder.build()
    }



    private fun setBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(requireContext())


        return BiometricPrompt(requireActivity(), executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(requireContext(), "Biometric authentication error: $errString", Toast.LENGTH_SHORT).show()
            }

            // onAuthenticationSucceeded 메서드 내에서 버튼 가시성을 숨깁니다.
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(requireContext(), "Biometric authentication succeeded", Toast.LENGTH_SHORT).show()
                
                // 지문 완료
                showTransactionPopup()   // 팝업 불러오기


            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(requireContext(), "Biometric authentication failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun authenticateToEncrypt() {
        val biometricManager = BiometricManager.from(requireContext())

        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                val biometricPrompt = setBiometricPrompt() // BiometricPrompt를 생성
                val promptInfo = setPromptInfo() // BiometricPrompt.PromptInfo를 설정
                biometricPrompt.authenticate(promptInfo) // 지문 인식을 시작
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(requireContext(), "No biometric hardware available", Toast.LENGTH_SHORT).show()
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(requireContext(), "Biometric hardware unavailable", Toast.LENGTH_SHORT).show()
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                showEnrollmentDialog()
            }
        }
    }


    private fun showEnrollmentDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder
            .setTitle("Biometric Enrollment")
            .setMessage("Biometric authentication is not enrolled. Do you want to enroll now?")
            .setPositiveButton("Enroll") { dialog, which -> goBiometricSettings() }
            .setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
            .show()
    }

    private fun goBiometricSettings() {
        val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
            putExtra(
                Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                BiometricManager.Authenticators.BIOMETRIC_STRONG
            )
        }
        startActivityForResult(enrollIntent, 123)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123) {
            // After returning from biometric enrollment settings, attempt biometric authentication again.
            authenticateToEncrypt()
        }
    }
}
