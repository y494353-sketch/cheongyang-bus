package com.example.cheongyangbus

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cheongyangbus.databinding.ActivityBusResultBinding

class BusResultActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBusResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareScreen(binding.root, binding.toolbar)
        val origin = intent.getStringExtra("origin") ?: "청양"
        val destination = intent.getStringExtra("destination") ?: "대전"
        binding.routeTitle.text = getString(R.string.route_format, origin, destination)
        binding.busList.layoutManager = LinearLayoutManager(this)
        binding.busList.adapter = BusTimeAdapter(DemoData.schedule(origin, destination)) {
            startActivity(it.detailIntent(this))
        }
    }
}
