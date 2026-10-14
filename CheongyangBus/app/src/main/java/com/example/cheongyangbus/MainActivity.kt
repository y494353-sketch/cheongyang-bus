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
        binding.destinationCard.setOnClickListener { openSearch(BusSearchActivity.ARRIVE_AT_CHEONGYANG) }
        binding.originCard.setOnClickListener { openSearch(BusSearchActivity.DEPART_FROM_CHEONGYANG) }
    }
    private fun openSearch(searchMode: String) {
        startActivity(Intent(this, BusSearchActivity::class.java).putExtra(BusSearchActivity.EXTRA_SEARCH_MODE, searchMode))
    }
    override fun onResume() {
        super.onResume()
        binding.recentRoute.text = DemoData.recentRoute ?: getString(R.string.recent_empty)
    }
}
