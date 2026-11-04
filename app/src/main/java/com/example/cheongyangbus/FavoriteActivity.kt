
package com.example.cheongyangbus

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cheongyangbus.databinding.ActivityFavoriteBinding

class FavoriteActivity : BaseActivity() {

    private lateinit var binding: ActivityFavoriteBinding

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

        // 즐겨찾기 화면 설정
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareScreen(binding.root, binding.toolbar)

        binding.favoriteList.layoutManager =
            LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()

        // 상세 화면에서 해제한 즐겨찾기도 즉시 반영
        if (!::binding.isInitialized) return

        binding.favoriteList.adapter = BusTimeAdapter(
            DemoData.favorites.toList(),
            true
        ) {
            startActivity(it.detailIntent(this))
        }

        // 즐겨찾기가 없으면 안내 메시지 표시
        binding.emptyMessage.visibility =
            if (DemoData.favorites.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
    }
}
