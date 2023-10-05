import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.a406.horsebit.MyEditFragment
import com.a406.horsebit.R
import com.a406.horsebit.databinding.FragmentMoreBinding

class MoreFragment : Fragment() {

    private lateinit var binding: FragmentMoreBinding
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: PromptInfo

    // SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreBinding.inflate(inflater, container, false)
        val view = binding.root

        // SharedPreferences 초기화
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // 버튼 상태 로드
        val isButtonEnabled = loadButtonState()

        // 버튼 초기 상태 설정
        binding.btnBodySwitch.isChecked = isButtonEnabled

        // 버튼 상태 변경 시 SharedPreferences에 저장
        binding.btnBodySwitch.setOnCheckedChangeListener { _, isChecked ->
            saveButtonState(isChecked)
            updateUIBasedOnButtonState(isChecked)
        }

        // 버튼 활성화 여부에 따라 지문 인증 버튼 표시 여부 설정
            binding.btnBioSet.visibility = View.INVISIBLE


        // 나머지 코드는 여기에 추가하면 됩니다.

        // lih_EditMe LinearLayout에 클릭 리스너를 추가
        binding.lihEditMe.setOnClickListener {
            // FragmentTransaction을 시작하여 화면 전환
            val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            val ft = requireActivity().supportFragmentManager.beginTransaction()
            val myEditFragment = MyEditFragment()

            // FragmentTransaction을 시작하여 화면 전환
            ft.replace(R.id.fl_MainFrameLayout, myEditFragment) // R.id.fl_MainFrameLayout,  Fragment를 표시할 레이아웃 컨테이너의 ID입니다.
            //ft.addToBackStack(null) // 뒤로 가기 버튼을 누를 때 이전 Fragment로 이동할 수 있도록 스택에 추가
            ft.commit()
        }

        val expandAnimation = AnimationUtils.loadAnimation(context, R.drawable.slide_up)
        val collapseAnimation = AnimationUtils.loadAnimation(context, R.drawable.slide_down)
        // gone slide 적용
        binding.llhMoreGone2.setOnClickListener {
            val llMoreGone2 = view.findViewById<LinearLayout>(R.id.llh_MoreGone2)
            // llMoreGone2 펼치기
            llMoreGone2.visibility = View.VISIBLE
            llMoreGone2.startAnimation(expandAnimation)
            llMoreGone2.startAnimation(collapseAnimation)
            llMoreGone2.visibility = View.GONE
        }

        binding.llhMoreGone1.setOnClickListener {
            val llMoreGone1 = view.findViewById<LinearLayout>(R.id.llh_MoreGone1)

            // llMoreGone1 펼치기
            llMoreGone1.visibility = View.VISIBLE
            llMoreGone1.startAnimation(expandAnimation)
            llMoreGone1.startAnimation(collapseAnimation)
            llMoreGone1.visibility = View.GONE
        }

        binding.lihMy.setOnClickListener {
            // LinearLayout의 visibility를 visible로 변경합니다.
            if (binding.llhMoreGone2.visibility == View.VISIBLE) {
                binding.llhMoreGone2.visibility = View.GONE
            } else {
                binding.llhMoreGone2.visibility = View.VISIBLE
            }
        }

        binding.lihPromise.setOnClickListener {
            // LinearLayout의 visibility를 visible로 변경합니다.
            if (binding.llhMoreGone1.visibility == View.VISIBLE) {
                binding.llhMoreGone1.visibility = View.GONE
            } else {
                binding.llhMoreGone1.visibility = View.VISIBLE
            }
        }

        // lih_Notice LinearLayout에 클릭 리스너를 추가
        binding.lihNotice.setOnClickListener {
            // 웹 페이지로 이동할 URL 정의
            val websiteUrl = "https://m.kra.co.kr/park/jeju/parkNoticeList.do"

            // 웹 브라우저를 열기 위한 인텐트 생성
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))

            // 액티비티를 실행하여 웹 페이지로 이동
            startActivity(intent)
        }

        // lih_InOut LinearLayout에 클릭 리스너를 추가
        binding.lihInOut.setOnClickListener {
            // FragmentTransaction을 시작하여 화면 전환
            val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            val ft = requireActivity().supportFragmentManager.beginTransaction()
            val exchangeFragment = ExchangeFragment()
            ft.replace(R.id.fl_MainFrameLayout, exchangeFragment)
            ft.addToBackStack(null) // 백 스택에 추가하면 뒤로 가기 버튼으로 이전 프래그먼트로 이동 가능
            ft.commit()
        }

        // 초기 상태에서 "불가능" 텍스트의 색상을 빨간색으로 설정
        updateUIBasedOnButtonState(isButtonEnabled)

        binding.btnBioSet.setOnClickListener {
            authenticateToEncrypt()
        }

        return view
    }

    private fun saveButtonState(isEnabled: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("buttonEnabled", isEnabled)
        editor.apply()
    }

    private fun loadButtonState(): Boolean {
        return sharedPreferences.getBoolean("buttonEnabled", false)
    }

    private fun updateUIBasedOnButtonState(isEnabled: Boolean) {
        if (isEnabled) {
            binding.tvTradePossible.text = "가능"
            binding.tvTradePossible.setTextColor(Color.BLUE)
            binding.tvDepositDrawalPossible.text = "가능"
            binding.tvDepositDrawalPossible.setTextColor(Color.BLUE)
            binding.tvDepositDrawalPossible2.text = "가능"
            binding.tvDepositDrawalPossible2.setTextColor(Color.BLUE)
            binding.btnBioSet.visibility = View.INVISIBLE
        } else {
            binding.tvTradePossible.text = "불가"
            binding.tvTradePossible.setTextColor(Color.RED)
            binding.tvDepositDrawalPossible.text = "불가"
            binding.tvDepositDrawalPossible.setTextColor(Color.RED)
            binding.tvDepositDrawalPossible2.text = "불가"
            binding.tvDepositDrawalPossible2.setTextColor(Color.RED)
            binding.btnBioSet.visibility = View.INVISIBLE
        }
    }

    private fun setPromptInfo(): PromptInfo {
        val promptBuilder = PromptInfo.Builder()

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

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(requireContext(), "Biometric authentication succeeded", Toast.LENGTH_SHORT).show()

                // 버튼 상태를 저장하고 UI를 업데이트합니다.
                saveButtonState(true)
                updateUIBasedOnButtonState(true)
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
                biometricPrompt = setBiometricPrompt()
                promptInfo = setPromptInfo()
                biometricPrompt.authenticate(promptInfo)
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
