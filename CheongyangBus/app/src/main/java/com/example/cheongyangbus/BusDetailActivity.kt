package com.example.cheongyangbus

import android.os.Bundle
import com.example.cheongyangbus.databinding.ActivityBusDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class BusDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityBusDetailBinding
    private lateinit var bus: BusTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareScreen(binding.root, binding.toolbar)
        bus = BusTime.fromIntent(intent)
        binding.routeTitle.text = bus.route
        binding.departureTime.text = bus.departure
        binding.arrivalTime.text = bus.arrival
        binding.departureLabel.text = "${bus.origin} · ${getString(R.string.departure)}"
        binding.arrivalLabel.text = "${bus.destination} · ${getString(R.string.arrival)}"
        binding.durationText.text = bus.duration
        updateButtons()
        binding.favoriteButton.setOnClickListener {
            val saved = DemoData.favorites.any { it.key == bus.key }
            if (saved) DemoData.favorites.removeAll { it.key == bus.key }
            else DemoData.favorites.add(bus)
            updateButtons()
            Snackbar.make(binding.root, if (saved) R.string.favorite_removed else R.string.favorite_saved,
                Snackbar.LENGTH_SHORT).show()
        }
        binding.alarmButton.setOnClickListener {
            if (DemoData.alarms.remove(bus.key)) {
                Snackbar.make(binding.root, R.string.alarm_off, Snackbar.LENGTH_SHORT).show()
            } else {
                DemoData.alarms.add(bus.key)
                MaterialAlertDialogBuilder(this).setTitle(R.string.alarm_title)
                    .setMessage(R.string.alarm_message).setPositiveButton(R.string.confirm, null).show()
            }
            updateButtons()
        }
    }
    private fun updateButtons() {
        binding.favoriteButton.setText(if (DemoData.favorites.any { it.key == bus.key })
            R.string.remove_favorite else R.string.add_favorite)
        binding.alarmButton.setText(if (bus.key in DemoData.alarms) R.string.cancel_alarm else R.string.set_alarm)
    }
}
