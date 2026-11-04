
package com.example.cheongyangbus

import android.app.AlertDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cheongyangbus.databinding.ActivityScheduleManageBinding

class ScheduleManageActivity : BaseActivity() {

    private lateinit var binding: ActivityScheduleManageBinding

    private val schedules = mutableListOf<BusTime>()

    private lateinit var adapter: ScheduleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 관리자 권한 확인
        if (!AccountStore.isLoggedIn(this) ||
            !AccountStore.isAdmin(this)
        ) {
            AccountStore.logout(this)
            startActivity(
                android.content.Intent(this, LoginActivity::class.java)
            )
            finish()
            return
        }

        binding = ActivityScheduleManageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareScreen(binding.root)

        // 저장된 시간표 불러오기
        schedules.addAll(BusScheduleStore.getSchedules(this))

        // RecyclerView 설정
        adapter = ScheduleAdapter(
            schedules,
            onEdit = { position ->
                showEditDialog(position)
            },
            onDelete = { position ->
                deleteSchedule(position)
            }
        )

        binding.scheduleRecyclerView.layoutManager =
            LinearLayoutManager(this)

        binding.scheduleRecyclerView.adapter = adapter

        // 시간표 추가
        binding.addScheduleButton.setOnClickListener {
            showAddDialog()
        }

        // 돌아가기
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    // 입력창 생성
    private fun createInput(
        hintText: String,
        value: String = ""
    ): EditText {

        return EditText(this).apply {
            hint = hintText
            setText(value)
            isSingleLine = true
        }
    }

    // 입력창 레이아웃
    private fun createInputLayout(
        origin: EditText,
        destination: EditText,
        departure: EditText,
        arrival: EditText
    ): LinearLayout {

        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 10, 40, 0)

            addView(origin)
            addView(destination)
            addView(departure)
            addView(arrival)
        }
    }

    // 시간표 추가
    private fun showAddDialog() {

        val originInput = createInput("출발지")
        val destinationInput = createInput("도착지")
        val departureInput = createInput("출발 시간 (예: 09:00)")
        val arrivalInput = createInput("도착 시간 (예: 10:20)")

        val layout = createInputLayout(
            originInput,
            destinationInput,
            departureInput,
            arrivalInput
        )

        AlertDialog.Builder(this)
            .setTitle("시간표 추가")
            .setView(layout)
            .setPositiveButton("추가", null)
            .setNegativeButton("취소", null)
            .create()
            .apply {

                setOnShowListener {

                    getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener {

                            val bus = BusTime(
                                originInput.text.toString().trim(),
                                destinationInput.text.toString().trim(),
                                departureInput.text.toString().trim(),
                                arrivalInput.text.toString().trim()
                            )

                            if (!isValidBus(bus)) {
                                Toast.makeText(
                                    this@ScheduleManageActivity,
                                    "출발지, 도착지와 올바른 시간을 입력하세요.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return@setOnClickListener
                            }

                            schedules.add(bus)
                            saveAndRefresh()
                            dismiss()
                        }
                }

                show()
            }
    }

    // 시간표 수정
    private fun showEditDialog(position: Int) {

        if (position !in schedules.indices) return

        val bus = schedules[position]

        val originInput = createInput("출발지", bus.origin)
        val destinationInput = createInput("도착지", bus.destination)
        val departureInput = createInput("출발 시간", bus.departure)
        val arrivalInput = createInput("도착 시간", bus.arrival)

        val layout = createInputLayout(
            originInput,
            destinationInput,
            departureInput,
            arrivalInput
        )

        AlertDialog.Builder(this)
            .setTitle("시간표 수정")
            .setView(layout)
            .setPositiveButton("수정", null)
            .setNegativeButton("취소", null)
            .create()
            .apply {

                setOnShowListener {

                    getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener {

                            val updatedBus = BusTime(
                                originInput.text.toString().trim(),
                                destinationInput.text.toString().trim(),
                                departureInput.text.toString().trim(),
                                arrivalInput.text.toString().trim()
                            )

                            if (!isValidBus(updatedBus)) {
                                Toast.makeText(
                                    this@ScheduleManageActivity,
                                    "입력 내용을 확인해 주세요.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return@setOnClickListener
                            }

                            schedules[position] = updatedBus

                            saveAndRefresh()
                            dismiss()
                        }
                }

                show()
            }
    }

    // 시간 형식 확인
    private fun isValidTime(time: String): Boolean {

        return Regex("^([01]\\d|2[0-3]):[0-5]\\d$")
            .matches(time)
    }

    // 입력값 확인
    private fun isValidBus(bus: BusTime): Boolean {

        return bus.origin.isNotBlank() &&
                bus.destination.isNotBlank() &&
                isValidTime(bus.departure) &&
                isValidTime(bus.arrival)
    }

    // 시간표 삭제
    private fun deleteSchedule(position: Int) {

        if (position !in schedules.indices) return

        AlertDialog.Builder(this)
            .setTitle("시간표 삭제")
            .setMessage("선택한 시간표를 삭제할까요?")
            .setPositiveButton("삭제") { _, _ ->

                schedules.removeAt(position)
                saveAndRefresh()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    // 저장 및 목록 갱신
    private fun saveAndRefresh() {

        BusScheduleStore.saveSchedules(this, schedules)

        adapter.notifyDataSetChanged()

        Toast.makeText(
            this,
            "시간표가 저장되었습니다.",
            Toast.LENGTH_SHORT
        ).show()
    }
}

// 시간표 목록 어댑터
class ScheduleAdapter(
    private val schedules: List<BusTime>,
    private val onEdit: (Int) -> Unit,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    class ScheduleViewHolder(
        val layout: LinearLayout
    ) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(
        parent: android.view.ViewGroup,
        viewType: Int
    ): ScheduleViewHolder {

        val layout = LinearLayout(parent.context).apply {

            orientation = LinearLayout.VERTICAL
            setPadding(24, 20, 24, 20)
            setBackgroundColor(android.graphics.Color.WHITE)

            // 카드 간격 설정
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 16
            }
        }

        return ScheduleViewHolder(layout)
    }

    override fun onBindViewHolder(
        holder: ScheduleViewHolder,
        position: Int
    ) {

        val bus = schedules[position]

        holder.layout.removeAllViews()

        val routeText = TextView(holder.layout.context).apply {
            text = "${bus.origin} → ${bus.destination}"
            textSize = 18f
            setTextColor(android.graphics.Color.BLACK)
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        val timeText = TextView(holder.layout.context).apply {
            text = "출발 ${bus.departure}  |  도착 ${bus.arrival}"
            textSize = 15f
            setTextColor(android.graphics.Color.DKGRAY)
            setPadding(0, 8, 0, 8)
        }

        val buttonLayout = LinearLayout(holder.layout.context).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        val editButton = android.widget.Button(holder.layout.context).apply {
            text = "수정"

            setOnClickListener {

                val currentPosition = holder.bindingAdapterPosition

                if (currentPosition != RecyclerView.NO_POSITION) {
                    onEdit(currentPosition)
                }
            }
        }

        val deleteButton = android.widget.Button(holder.layout.context).apply {
            text = "삭제"

            setOnClickListener {

                val currentPosition = holder.bindingAdapterPosition

                if (currentPosition != RecyclerView.NO_POSITION) {
                    onDelete(currentPosition)
                }
            }
        }

        buttonLayout.addView(editButton)
        buttonLayout.addView(deleteButton)

        holder.layout.addView(routeText)
        holder.layout.addView(timeText)
        holder.layout.addView(buttonLayout)
    }

    override fun getItemCount(): Int {
        return schedules.size
    }
}