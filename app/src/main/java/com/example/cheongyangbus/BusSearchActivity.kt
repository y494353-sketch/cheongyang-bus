
package com.example.cheongyangbus

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.example.cheongyangbus.databinding.ActivityBusSearchBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class BusSearchActivity : BaseActivity() {

    companion object {
        const val EXTRA_SEARCH_MODE = "searchMode"
        const val DEPART_FROM_CHEONGYANG = "DEPART_FROM_CHEONGYANG"
        const val ARRIVE_AT_CHEONGYANG = "ARRIVE_AT_CHEONGYANG"
    }

    private lateinit var binding: ActivityBusSearchBinding

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

        binding = ActivityBusSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareScreen(binding.root, binding.toolbar)

        // 검색 방향 설정
        val departing =
            intent.getStringExtra(EXTRA_SEARCH_MODE) != ARRIVE_AT_CHEONGYANG

        val cheongyang = getString(R.string.cheongyang)

        val regions = resources
            .getStringArray(R.array.regions)
            .filter { it != cheongyang }

        val fixedLayout =
            if (departing) binding.originLayout
            else binding.destinationLayout

        val fixedInput =
            if (departing) binding.originInput
            else binding.destinationInput

        val selectableInput =
            if (departing) binding.destinationInput
            else binding.originInput

        val savedKey =
            if (departing) "destination"
            else "origin"

        binding.searchHeading.setText(
            if (departing) R.string.search_intro
            else R.string.search_arrival_intro
        )

        binding.modeLabel.setText(
            if (departing) R.string.search_mode_to
            else R.string.search_mode_from
        )

        // 청양 고정 필드 설정
        fixedInput.setText(cheongyang, false)
        fixedLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
        fixedLayout.setEndIconDrawable(R.drawable.ic_lock)
        fixedLayout.setEndIconContentDescription(
            R.string.fixed_cheongyang
        )
        fixedLayout.setEndIconOnClickListener(null)
        fixedLayout.isEndIconCheckable = false

        fixedLayout.boxBackgroundColor =
            ContextCompat.getColor(this, R.color.green_soft)

        fixedLayout.isEnabled = false
        fixedInput.isEnabled = false
        fixedInput.isFocusable = false
        fixedInput.isClickable = false

        fixedInput.setTextColor(
            ContextCompat.getColor(this, R.color.muted)
        )

        fixedInput.contentDescription =
            getString(R.string.fixed_cheongyang)

        // 선택 가능한 지역 목록
        selectableInput.setAdapter(
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                regions
            )
        )

        // 화면 회전 시 선택 지역 복원
        val selected = savedInstanceState
            ?.getString(savedKey)
            ?.takeIf { it in regions }
            ?: regions.first()

        selectableInput.setText(selected, false)

        // 검색 버튼
        binding.searchButton.setOnClickListener {
            val region = selectableInput.text.toString()

            if (region !in regions) {
                Snackbar.make(
                    binding.root,
                    R.string.select_region,
                    Snackbar.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // 검색 방향 확정
            val origin =
                if (departing) cheongyang else region

            val destination =
                if (departing) region else cheongyang

            // 최근 경로 저장
            DemoData.recentRoute = getString(
                R.string.route_format,
                origin,
                destination
            )

            // 버스 결과 화면 이동
            startActivity(
                Intent(this, BusResultActivity::class.java).apply {
                    putExtra("origin", origin)
                    putExtra("destination", destination)
                }
            )
        }
    }

    // 화면 회전 시 선택 지역 저장
    override fun onSaveInstanceState(outState: Bundle) {
        if (::binding.isInitialized) {
            outState.putString(
                "origin",
                binding.originInput.text.toString()
            )

            outState.putString(
                "destination",
                binding.destinationInput.text.toString()
            )
        }

        super.onSaveInstanceState(outState)
    }
}
