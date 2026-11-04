
package com.example.cheongyangbus

import android.content.Intent
import android.os.Bundle
import com.example.cheongyangbus.databinding.ActivityMyPageBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MyPageActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 로그인 여부 확인
        if (!AccountStore.isLoggedIn(this)) {
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            startActivity(intent)
            finish()
            return
        }

        // 마이페이지 화면 설정
        val binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareScreen(binding.root, binding.toolbar)

        // 즐겨찾기
        binding.favoritesButton.setOnClickListener {
            startActivity(
                Intent(this, FavoriteActivity::class.java)
            )
        }

        // 알림 안내
        binding.notificationsButton.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.notifications_title)
                .setMessage(R.string.notifications_message)
                .setPositiveButton(R.string.confirm, null)
                .show()
        }

        // 로그아웃
        binding.logoutButton.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("로그아웃")
                .setMessage("로그아웃하시겠습니까?")
                .setNegativeButton("취소", null)
                .setPositiveButton("로그아웃") { _, _ ->

                    // 로그인 상태 해제
                    AccountStore.setLoggedIn(this, false)

                    // 이전 화면을 제거하고 로그인 화면으로 이동
                    val intent = Intent(
                        this,
                        LoginActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }

                    startActivity(intent)
                    finish()
                }
                .show()
        }
    }
}
