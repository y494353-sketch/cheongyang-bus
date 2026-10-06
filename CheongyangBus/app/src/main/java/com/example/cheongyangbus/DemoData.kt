package com.example.cheongyangbus

// 서버와 DB 없이 화면을 확인하는 예시 데이터입니다.
// 프로세스가 종료되면 즐겨찾기·알림·최근 검색은 초기화됩니다.
object DemoData {
    val favorites = mutableListOf(
        BusTime("청양", "대전", "09:00", "10:20"),
        BusTime("청양", "서울", "13:00", "15:30", "2시간 30분")
    )
    val alarms = mutableSetOf<String>()
    var recentRoute: String? = null

    fun schedule(origin: String, destination: String): List<BusTime> {
        val times = listOf("09:00" to "10:20", "10:30" to "11:50",
            "13:00" to "14:20", "15:30" to "16:50", "18:00" to "19:20")
        // 모든 지역 조합에 동일한 시각을 표시하는 UI 전용 샘플입니다.
        return times.mapIndexed { index, time ->
            BusTime(origin, destination, time.first, time.second,
                status = if (index == 0) "운행중" else "운행예정")
        }
    }
}
