
package com.example.cheongyangbus

object DemoData {

    val favorites = mutableListOf(
        BusTime("청양", "대전", "09:00", "10:20"),
        BusTime("청양", "서울", "13:00", "15:30", "2시간 30분")
    )

    val alarms = mutableSetOf<String>()

    var recentRoute: String? = null

    // 저장된 시간표를 기준으로 검색
    fun schedule(
        context: android.content.Context,
        origin: String,
        destination: String
    ): List<BusTime> {

        return BusScheduleStore.getSchedules(context)
            .filter {
                it.origin == origin &&
                        it.destination == destination
            }
            .sortedBy { it.departure }
            .mapIndexed { index, bus ->
                bus.copy(
                    status = if (index == 0) "운행중" else "운행예정"
                )
            }
    }
}
