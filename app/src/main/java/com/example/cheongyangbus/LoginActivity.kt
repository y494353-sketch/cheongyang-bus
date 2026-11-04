
package com.example.cheongyangbus

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.cheongyangbus.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareScreen(binding.root)

        // 이미 로그인한 사용자 확인
        if (AccountStore.isLoggedIn(this)) {
            if (AccountStore.isAdmin(this)) {
                openAdminScreen()
            } else {
                openMainScreen()
            }
            return
        }

        // 회원가입 후 전달받은 아이디 자동 입력
        binding.userId.setText(
            intent.getStringExtra("registered_id").orEmpty()
        )

        // 로그인 버튼
        binding.loginButton.setOnClickListener {

            val id = binding.userId.text?.toString()
                ?.trim().orEmpty()

            val password = binding.password.text?.toString()
                .orEmpty()

            when {

                id.isEmpty() -> {
                    binding.userId.error = "아이디를 입력하세요."
                    binding.userId.requestFocus()
                }

                password.isEmpty() -> {
                    binding.password.error = "비밀번호를 입력하세요."
                    binding.password.requestFocus()
                }

                !AccountStore.login(this, id, password) -> {
                    Toast.makeText(
                        this,
                        "아이디 또는 비밀번호가 올바르지 않습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {

                    // 관리자 계정 확인
                    val isAdmin = AccountStore.isAdminAccount(
                        id,
                        password
                    )

                    // 로그인 상태와 권한 저장
                    AccountStore.setLoggedIn(
                        this,
                        true,
                        isAdmin
                    )

                    if (isAdmin) {

                        Toast.makeText(
                            this,
                            "관리자로 로그인되었습니다.",
                            Toast.LENGTH_SHORT
                        ).show()

                        openAdminScreen()

                    } else {

                        Toast.makeText(
                            this,
                            "로그인되었습니다.",
                            Toast.LENGTH_SHORT
                        ).show()

                        openMainScreen()
                    }
                }
            }
        }

        // 회원가입 화면으로 이동
        binding.registerButton.setOnClickListener {
            startActivity(
                Intent(this, RegisterActivity::class.java)
            )
        }
    }

    // 일반 사용자 메인 화면
    private fun openMainScreen() {

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        startActivity(intent)
        finish()
    }

    // 관리자 화면
    private fun openAdminScreen() {

        val intent = Intent(this, AdminActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        startActivity(intent)
        finish()
    }
}
