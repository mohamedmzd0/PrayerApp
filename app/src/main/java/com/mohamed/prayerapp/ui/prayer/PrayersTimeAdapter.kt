package com.mohamed.prayerapp.ui.prayer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.data.model.pray_times.PrayItem
import com.mohamed.prayerapp.databinding.ItemPrayerTimeBinding

// create adapter
class PrayersTimeAdapter : RecyclerView.Adapter<PrayersTimeAdapter.PrayersTimeViewHolder>() {

    // list of data
    private val items = mutableListOf<PrayItem>()

    // add data
    fun addData(data: List<PrayItem>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    // clear data
    fun clearData() {
        items.clear()
        notifyDataSetChanged()
    }

    // view holder
    inner class PrayersTimeViewHolder(private val binding: ItemPrayerTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PrayItem) {
            binding.apply {
                tvTitle.text = data.name
                tvTime.text = data.time
            }
        }
    }

    // onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrayersTimeViewHolder {
        val binding =
            ItemPrayerTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PrayersTimeViewHolder(binding)
    }

    // onBindViewHolder
    override fun onBindViewHolder(holder: PrayersTimeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    // getItemCount
    override fun getItemCount(): Int = items.size
}