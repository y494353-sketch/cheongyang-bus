
package com.example.cheongyangbus

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cheongyangbus.databinding.ActivityBusResultBinding

class BusResultActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 로그인 및 관리자 권한 확인
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

        // 버스 결과 화면 설정
        val binding = ActivityBusResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareScreen(binding.root, binding.toolbar)

        // 출발지와 도착지 가져오기
        val origin = intent.getStringExtra("origin") ?: "청양"
        val destination = intent.getStringExtra("destination") ?: "대전"

        // 노선 제목 표시
        binding.routeTitle.text = getString(
            R.string.route_format,
            origin,
            destination
        )

        // 버스 시간 목록 설정
        binding.busList.layoutManager = LinearLayoutManager(this)

        // 관리자 화면에서 저장한 시간표 불러오기
        val busSchedules = BusScheduleStore.getSchedules(this)
            .filter {
                it.origin == origin &&
                        it.destination == destination
            }
            .sortedBy {
                it.departure
            }

        if (busSchedules.isEmpty()) {
            Toast.makeText(
                this,
                "등록된 버스 시간표가 없습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.busList.adapter = BusTimeAdapter(
            busSchedules
        ) {
            startActivity(it.detailIntent(this))
        }
    }
}