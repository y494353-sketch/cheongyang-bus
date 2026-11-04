
package com.example.cheongyangbus

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cheongyangbus.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 관리자 로그인 여부 확인
        if (!AccountStore.isLoggedIn(this) ||
            !AccountStore.isAdmin(this)
        ) {
            AccountStore.logout(this)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 버스 시간표 관리 화면 이동
        binding.scheduleManageButton.setOnClickListener {
            startActivity(
                Intent(this, ScheduleManageActivity::class.java)
            )
        }

        // 관리자 로그아웃
        binding.logoutButton.setOnClickListener {
            AccountStore.logout(this)

            Toast.makeText(
                this,
                "관리자 로그아웃되었습니다.",
                Toast.LENGTH_SHORT
            ).show()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}