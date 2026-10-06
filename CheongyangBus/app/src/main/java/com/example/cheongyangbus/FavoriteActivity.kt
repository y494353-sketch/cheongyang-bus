package com.example.cheongyangbus

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cheongyangbus.databinding.ActivityFavoriteBinding

class FavoriteActivity : BaseActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareScreen(binding.root, binding.toolbar)
        binding.favoriteList.layoutManager = LinearLayoutManager(this)
    }
    override fun onResume() {
        super.onResume()
        // 상세 화면에서 해제한 즐겨찾기도 즉시 반영합니다.
        binding.favoriteList.adapter = BusTimeAdapter(DemoData.favorites.toList(), true) {
            startActivity(it.detailIntent(this))
        }
        binding.emptyMessage.visibility = if (DemoData.favorites.isEmpty()) View.VISIBLE else View.GONE
    }
}
