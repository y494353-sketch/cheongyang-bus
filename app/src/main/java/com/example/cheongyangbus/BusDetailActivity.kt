
package com.example.cheongyangbus

import android.content.Intent
import android.os.Bundle
import com.example.cheongyangbus.databinding.ActivityBusDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class BusDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityBusDetailBinding
    private lateinit var bus: BusTime

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

        // 버스 상세 화면 설정
        binding = ActivityBusDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareScreen(binding.root, binding.toolbar)

        // 버스 정보 가져오기
        bus = BusTime.fromIntent(intent)

        binding.routeTitle.text = bus.route
        binding.departureTime.text = bus.departure
        binding.arrivalTime.text = bus.arrival

        binding.departureLabel.text =
            "${bus.origin} · ${getString(R.string.departure)}"

        binding.arrivalLabel.text =
            "${bus.destination} · ${getString(R.string.arrival)}"

        binding.durationText.text = bus.duration

        updateButtons()

        // 즐겨찾기 추가 및 해제
        binding.favoriteButton.setOnClickListener {
            val saved = DemoData.favorites.any {
                it.key == bus.key
            }

            if (saved) {
                DemoData.favorites.removeAll {
                    it.key == bus.key
                }
            } else {
                DemoData.favorites.add(bus)
            }

            updateButtons()

            Snackbar.make(
                binding.root,
                if (saved) R.string.favorite_removed
                else R.string.favorite_saved,
                Snackbar.LENGTH_SHORT
            ).show()
        }

        // 알림 설정 및 취소
        binding.alarmButton.setOnClickListener {
            if (DemoData.alarms.remove(bus.key)) {
                Snackbar.make(
                    binding.root,
                    R.string.alarm_off,
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                DemoData.alarms.add(bus.key)

                MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.alarm_title)
                    .setMessage(R.string.alarm_message)
                    .setPositiveButton(R.string.confirm, null)
                    .show()
            }

            updateButtons()
        }
    }

    // 버튼 문구 갱신
    private fun updateButtons() {
        binding.favoriteButton.setText(
            if (DemoData.favorites.any { it.key == bus.key }) {
                R.string.remove_favorite
            } else {
                R.string.add_favorite
            }
        )

        binding.alarmButton.setText(
            if (bus.key in DemoData.alarms) {
                R.string.cancel_alarm
            } else {
                R.string.set_alarm
            }
        )
    }
}
