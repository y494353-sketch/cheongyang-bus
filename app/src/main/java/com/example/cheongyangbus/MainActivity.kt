
package com.example.cheongyangbus

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.cheongyangbus.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private val handler = Handler(Looper.getMainLooper())

    // 1초마다 카운트다운 갱신
    private val countdownRunnable = object : Runnable {
        override fun run() {
            updateCountdown()
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 로그인 여부 확인
        if (!AccountStore.isLoggedIn(this) ||
            AccountStore.isAdmin(this)
        ) {
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            startActivity(intent)
            finish()
            return
        }

        // 메인 화면 설정
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareScreen(binding.root)

        // 즐겨찾기
        binding.favoriteButton.setOnClickListener {
            startActivity(
                Intent(this, FavoriteActivity::class.java)
            )
        }

        // 마이페이지
        binding.myPageButton.setOnClickListener {
            startActivity(
                Intent(this, MyPageActivity::class.java)
            )
        }

        // 청양 도착 버스 검색
        binding.destinationCard.setOnClickListener {
            openSearch(BusSearchActivity.ARRIVE_AT_CHEONGYANG)
        }

        // 청양 출발 버스 검색
        binding.originCard.setOnClickListener {
            openSearch(BusSearchActivity.DEPART_FROM_CHEONGYANG)
        }

        // 카운트다운 노선 표시
        binding.countdownRoute.text = "청양 → 대전"
    }

    // 버스 검색 화면 이동
    private fun openSearch(searchMode: String) {
        startActivity(
            Intent(this, BusSearchActivity::class.java)
                .putExtra(
                    BusSearchActivity.EXTRA_SEARCH_MODE,
                    searchMode
                )
        )
    }

    // 다음 버스 출발 시간 계산
    private fun getNextBusTime(): Calendar? {

        // 관리자 화면에서 저장한 시간표 불러오기
        val busList = DemoData.schedule(
            this,
            "청양",
            "대전"
        )

        val now = Calendar.getInstance()
        val availableTimes = mutableListOf<Calendar>()

        // 오늘과 다음 날의 시간표 확인
        for (dayOffset in 0..1) {

            for (bus in busList) {

                val parts = bus.departure.split(":")

                if (parts.size != 2) continue

                val hour = parts[0].toIntOrNull() ?: continue
                val minute = parts[1].toIntOrNull() ?: continue

                if (hour !in 0..23 || minute !in 0..59) continue

                val busCalendar = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, dayOffset)
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                if (busCalendar.after(now)) {
                    availableTimes.add(busCalendar)
                }
            }
        }

        return availableTimes.minByOrNull {
            it.timeInMillis
        }
    }

    // 카운트다운 화면 갱신
    private fun updateCountdown() {

        val nextBus = getNextBusTime()

        if (nextBus == null) {
            binding.countdownTimer.text = "--:--:--"
            binding.countdownDeparture.text =
                "출발 예정 시간을 확인할 수 없습니다."
            binding.countdownStatus.text =
                "시간표를 확인해 주세요."
            return
        }

        val now = Calendar.getInstance()

        val remaining =
            nextBus.timeInMillis - now.timeInMillis

        val totalSeconds = remaining / 1000

        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        // 남은 시간 표시
        binding.countdownTimer.text =
            String.format(
                Locale.KOREA,
                "%02d:%02d:%02d",
                hours,
                minutes,
                seconds
            )

        // 출발 예정 시각 표시
        val departureFormat =
            SimpleDateFormat("a hh:mm", Locale.KOREA)

        binding.countdownDeparture.text =
            "출발 예정: ${departureFormat.format(nextBus.time)}"

        // 출발 임박 안내
        binding.countdownStatus.text =
            if (remaining <= 10 * 60 * 1000) {
                "곧 출발합니다! 탑승을 준비해 주세요."
            } else {
                "다음 버스 출발을 기다리고 있습니다."
            }
    }

    // 화면이 보일 때 카운트다운 시작
    override fun onResume() {
        super.onResume()

        if (::binding.isInitialized) {

            binding.recentRoute.text =
                DemoData.recentRoute
                    ?: getString(R.string.recent_empty)

            handler.removeCallbacks(countdownRunnable)
            handler.post(countdownRunnable)
        }
    }

    // 다른 화면으로 이동하면 타이머 중지
    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(countdownRunnable)
    }

    // 액티비티 종료 시 타이머 정리
    override fun onDestroy() {
        handler.removeCallbacks(countdownRunnable)
        super.onDestroy()
    }
}