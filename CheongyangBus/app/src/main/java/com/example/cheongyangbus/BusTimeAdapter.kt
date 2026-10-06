package com.example.cheongyangbus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cheongyangbus.databinding.ItemBusTimeBinding

class BusTimeAdapter(
    private val buses: List<BusTime>,
    private val isFavorite: Boolean = false,
    private val onDetail: (BusTime) -> Unit
) : RecyclerView.Adapter<BusTimeAdapter.BusViewHolder>() {
    class BusViewHolder(val binding: ItemBusTimeBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusViewHolder {
        return BusViewHolder(ItemBusTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun getItemCount(): Int = buses.size
    override fun onBindViewHolder(holder: BusViewHolder, position: Int) {
        val bus = buses[position]
        with(holder.binding) {
            originName.text = bus.origin
            destinationName.text = bus.destination
            departureTime.text = bus.departure
            arrivalTime.text = bus.arrival
            statusBadge.text = if (isFavorite) "★ ${root.context.getString(R.string.favorite)}" else bus.status
            durationText.text = root.context.getString(R.string.duration_format, bus.duration)
            detailButton.contentDescription = root.context.getString(R.string.detail_accessibility, bus.departure, bus.arrival)
            detailButton.setOnClickListener { onDetail(bus) }
            root.setOnClickListener { onDetail(bus) }
        }
    }
}
