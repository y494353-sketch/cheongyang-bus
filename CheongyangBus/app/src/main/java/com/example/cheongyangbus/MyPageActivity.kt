package com.example.cheongyangbus

import android.content.Intent
import android.os.Bundle
import com.example.cheongyangbus.databinding.ActivityMyPageBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MyPageActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareScreen(binding.root, binding.toolbar)
        binding.favoritesButton.setOnClickListener { startActivity(Intent(this, FavoriteActivity::class.java)) }
        binding.notificationsButton.setOnClickListener {
            MaterialAlertDialogBuilder(this).setTitle(R.string.notifications_title)
                .setMessage(R.string.notifications_message).setPositiveButton(R.string.confirm, null).show()
        }
        binding.logoutButton.setOnClickListener {
            // 이전 화면 스택을 비워 뒤로가기로 메인 화면에 재진입하지 않습니다.
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }
    }
}
