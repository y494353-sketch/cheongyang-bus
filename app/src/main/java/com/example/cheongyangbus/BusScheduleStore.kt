
package com.example.cheongyangbus

import android.content.Context

object BusScheduleStore {

    private const val PREF_NAME = "cheongyang_bus_schedule"
    private const val KEY_SCHEDULES = "schedules"

    // 저장된 시간표 불러오기
    fun getSchedules(context: Context): List<BusTime> {

        val prefs = context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )

        val savedData = prefs.getString(KEY_SCHEDULES, null)

        if (savedData.isNullOrEmpty()) {
            return getDefaultSchedules()
        }

        return try {
            savedData.split("|").mapNotNull { item ->

                val parts = item.split(",")

                if (parts.size != 4) {
                    null
                } else {
                    BusTime(
                        parts[0],
                        parts[1],
                        parts[2],
                        parts[3]
                    )
                }
            }
        } catch (e: Exception) {
            getDefaultSchedules()
        }
    }

    // 기본 시간표
    private fun getDefaultSchedules(): List<BusTime> {

        return listOf(
            BusTime("청양", "대전", "09:00", "10:20"),
            BusTime("청양", "대전", "10:30", "11:50"),
            BusTime("청양", "대전", "13:00", "14:20"),
            BusTime("청양", "대전", "15:30", "16:50"),
            BusTime("청양", "대전", "18:00", "19:20"),
            BusTime("청양", "서울", "13:00", "15:30")
        )
    }

    // 시간표 전체 저장
    fun saveSchedules(
        context: Context,
        schedules: List<BusTime>
    ) {

        val savedData = schedules.joinToString("|") { bus ->
            "${bus.origin},${bus.destination},${bus.departure},${bus.arrival}"
        }

        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        ).edit()
            .putString(KEY_SCHEDULES, savedData)
            .apply()
    }

    // 시간표 추가
    fun addSchedule(
        context: Context,
        bus: BusTime
    ) {

        val schedules = getSchedules(context).toMutableList()
        schedules.add(bus)

        saveSchedules(context, schedules)
    }

    // 시간표 삭제
    fun deleteSchedule(
        context: Context,
        index: Int
    ) {

        val schedules = getSchedules(context).toMutableList()

        if (index in schedules.indices) {
            schedules.removeAt(index)
            saveSchedules(context, schedules)
        }
    }
}
