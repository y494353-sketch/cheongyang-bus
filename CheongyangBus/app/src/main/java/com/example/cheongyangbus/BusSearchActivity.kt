package com.example.cheongyangbus

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.cheongyangbus.databinding.ActivityBusSearchBinding
import com.google.android.material.snackbar.Snackbar

class BusSearchActivity : BaseActivity() {
    private lateinit var binding: ActivityBusSearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareScreen(binding.root, binding.toolbar)
        val regions = resources.getStringArray(R.array.regions)
        binding.originInput.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, regions))
        binding.destinationInput.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, regions))
        val selectOrigin = intent.getBooleanExtra("selectOrigin", false)
        binding.modeLabel.setText(if (selectOrigin) R.string.search_mode_from else R.string.search_mode_to)
        binding.originInput.setText(savedInstanceState?.getString("origin") ?: if (selectOrigin) "대전" else "청양", false)
        binding.destinationInput.setText(savedInstanceState?.getString("destination") ?: if (selectOrigin) "청양" else "대전", false)
        binding.searchButton.setOnClickListener {
            val origin = binding.originInput.text.toString()
            val destination = binding.destinationInput.text.toString()
            if (origin == destination) {
                Snackbar.make(binding.root, R.string.same_region, Snackbar.LENGTH_SHORT).show()
            } else {
                DemoData.recentRoute = getString(R.string.route_format, origin, destination)
                startActivity(Intent(this, BusResultActivity::class.java).apply {
                    putExtra("origin", origin)
                    putExtra("destination", destination)
                })
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("origin", binding.originInput.text.toString())
        outState.putString("destination", binding.destinationInput.text.toString())
        super.onSaveInstanceState(outState)
    }
}
