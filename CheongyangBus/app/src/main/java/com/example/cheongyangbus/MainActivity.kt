package com.example.cheongyangbus

import android.content.Intent
import android.os.Bundle
import com.example.cheongyangbus.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareScreen(binding.root)
        binding.favoriteButton.setOnClickListener { startActivity(Intent(this, FavoriteActivity::class.java)) }
        binding.myPageButton.setOnClickListener { startActivity(Intent(this, MyPageActivity::class.java)) }
        binding.destinationCard.setOnClickListener { openSearch(false) }
        binding.originCard.setOnClickListener { openSearch(true) }
    }
    private fun openSearch(selectOrigin: Boolean) {
        startActivity(Intent(this, BusSearchActivity::class.java).putExtra("selectOrigin", selectOrigin))
    }
    override fun onResume() {
        super.onResume()
        binding.recentRoute.text = DemoData.recentRoute ?: getString(R.string.recent_empty)
    }
}
