// ExchangeFragment.kt

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
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
import com.a406.horsebit.ExchangeData
import com.a406.horsebit.R
import com.a406.horsebit.databinding.FragmentExchangeBinding

class ExchangeFragment : Fragment() {

    private lateinit var binding: FragmentExchangeBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExchangeTableAdapter
    private lateinit var exchangeDataList: List<ExchangeData>

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExchangeBinding.inflate(inflater, container, false)
        val view = binding.root

        recyclerView = view.findViewById(R.id.rv_ExchangeTable)



        // 입출금 팝업 뜨는 곳
        binding.btnExchange.setOnClickListener {
            authenticateToEncrypt() // 지문인식
        }
        // RecyclerView의 레이아웃 매니저를 수평 방향으로 설정합니다.
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        // 더미 데이터 생성 (실제 데이터로 대체해야 함)
        exchangeDataList = listOf(
            ExchangeData("체결시간", "코인명", "종류", "거래수량", "거래단가", "거래금액", "수수료", "정산금액", "주문시간"),
            ExchangeData("2023-09-15 10:30:00", "Bitcoin", "매수", "2.5 BTC", "$45,000", "$112,500", "$10", "$112,490", "2023-09-15 10:29:55"),
            ExchangeData("2023-09-14 15:20:00", "Ethereum", "매도", "10 ETH", "$3,500", "$35,000", "$5", "$34,995", "2023-09-14 15:19:55"),
            ExchangeData("2023-09-13 09:45:00", "Ripple", "매수", "100 XRP", "$1.25", "$125", "$2", "$123", "2023-09-13 09:44:55"),
            ExchangeData("2023-09-12 14:15:00", "Litecoin", "매도", "5 LTC", "$150", "$750", "$3", "$747", "2023-09-12 14:14:55"),
            ExchangeData("2023-09-11 11:10:00", "Cardano", "매수", "50 ADA", "$2", "$100", "$1", "$99", "2023-09-11 11:09:55"),
            ExchangeData("2023-09-10 16:40:00", "Polkadot", "매도", "20 DOT", "$30", "$600", "$4", "$596", "2023-09-10 16:39:55"),
            ExchangeData("2023-09-09 12:05:00", "Chainlink", "매수", "30 LINK", "$25", "$750", "$5", "$745", "2023-09-09 12:04:55"),
            ExchangeData("2023-09-08 14:50:00", "Stellar", "매도", "200 XLM", "$0.5", "$100", "$1", "$99", "2023-09-08 14:49:55"),
            ExchangeData("2023-09-07 17:55:00", "Tezos", "매수", "15 XTZ", "$6", "$90", "$2", "$88", "2023-09-07 17:54:55"),
            ExchangeData("2023-09-06 11:25:00", "VeChain", "매도", "1000 VET", "$0.1", "$100", "$3", "$97", "2023-09-06 11:24:55"),
            ExchangeData("2023-09-15 10:30:00", "Bitcoin", "매수", "2.5 BTC", "$45,000", "$112,500", "$10", "$112,490", "2023-09-15 10:29:55"),
            ExchangeData("2023-09-14 15:20:00", "Ethereum", "매도", "10 ETH", "$3,500", "$35,000", "$5", "$34,995", "2023-09-14 15:19:55"),
            ExchangeData("2023-09-13 09:45:00", "Ripple", "매수", "100 XRP", "$1.25", "$125", "$2", "$123", "2023-09-13 09:44:55"),
            ExchangeData("2023-09-12 14:15:00", "Litecoin", "매도", "5 LTC", "$150", "$750", "$3", "$747", "2023-09-12 14:14:55"),
            ExchangeData("2023-09-11 11:10:00", "Cardano", "매수", "50 ADA", "$2", "$100", "$1", "$99", "2023-09-11 11:09:55"),
            ExchangeData("2023-09-10 16:40:00", "Polkadot", "매도", "20 DOT", "$30", "$600", "$4", "$596", "2023-09-10 16:39:55"),
            ExchangeData("2023-09-09 12:05:00", "Chainlink", "매수", "30 LINK", "$25", "$750", "$5", "$745", "2023-09-09 12:04:55"),
            ExchangeData("2023-09-08 14:50:00", "Stellar", "매도", "200 XLM", "$0.5", "$100", "$1", "$99", "2023-09-08 14:49:55"),
            ExchangeData("2023-09-07 17:55:00", "Tezos", "매수", "15 XTZ", "$6", "$90", "$2", "$88", "2023-09-07 17:54:55"),
            ExchangeData("2023-09-06 11:25:00", "VeChain", "매도", "1000 VET", "$0.1", "$100", "$3", "$97", "2023-09-06 11:24:55")
        )
        adapter = ExchangeTableAdapter(requireActivity(), exchangeDataList)
        recyclerView.adapter = adapter

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
