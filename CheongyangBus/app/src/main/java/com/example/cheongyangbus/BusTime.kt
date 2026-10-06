package com.example.cheongyangbus

import android.content.Context
import android.content.Intent

data class BusTime(
    val origin: String,
    val destination: String,
    val departure: String,
    val arrival: String,
    val duration: String = "1시간 20분",
    val status: String = "운행예정"
) {
    val route: String get() = "$origin → $destination"
    val key: String get() = "$origin|$destination|$departure|$arrival"

    // 단순 문자열 Extra를 사용하므로 Parcelable 설정이 필요 없습니다.
    fun detailIntent(context: Context) = Intent(context, BusDetailActivity::class.java).apply {
        putExtra("origin", origin)
        putExtra("destination", destination)
        putExtra("departure", departure)
        putExtra("arrival", arrival)
        putExtra("duration", duration)
        putExtra("status", status)
    }

    companion object {
        fun fromIntent(intent: Intent) = BusTime(
            intent.getStringExtra("origin") ?: "청양",
            intent.getStringExtra("destination") ?: "대전",
            intent.getStringExtra("departure") ?: "09:00",
            intent.getStringExtra("arrival") ?: "10:20",
            intent.getStringExtra("duration") ?: "1시간 20분",
            intent.getStringExtra("status") ?: "운행예정"
        )
    }
}
