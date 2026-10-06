package com.example.cheongyangbus

import android.content.Intent
import android.os.Bundle
import com.example.cheongyangbus.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareScreen(binding.root)
        binding.loginButton.setOnClickListener {
            // 현재 단계에서는 입력값 검증 없이 이동합니다.
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
