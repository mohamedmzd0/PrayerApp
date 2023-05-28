package com.mohamed.prayerapp.ui.prayer

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.data.model.pray_times.PrayItem
import com.app.data.model.pray_times.PrayerTimes
import com.mohamed.prayerapp.databinding.ItemPageBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

// create recycler adapter
class PrayerPageAdapter(private val onNextPress: () -> Unit, private val onPrevPress: () -> Unit) :
    RecyclerView.Adapter<PrayerPageAdapter.PrayerPageViewHolder>() {

    // list of data
    private val items = mutableListOf<PrayerTimes>()

    // add data
    fun addData(data: List<PrayerTimes>) {
        items.addAll(data)
        notifyDataSetChanged()
    }

    // clear data
    fun clearData() {
        items.clear()
        notifyDataSetChanged()
    }

    // view holder
    inner class PrayerPageViewHolder(private val binding: ItemPageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val adapter = PrayersTimeAdapter()

        init {
            binding.rvPrayersTime.adapter = adapter
            binding.rvPrayersTime.layoutManager = LinearLayoutManager(binding.root.context)
            binding.rvPrayersTime.setHasFixedSize(true)

            binding.ivNext.setOnClickListener {
                if (adapterPosition == -1) return@setOnClickListener
                onNextPress.invoke()
            }
            binding.ivPrev.setOnClickListener {
                if (adapterPosition == -1) return@setOnClickListener
                onPrevPress.invoke()
            }
        }

        fun bind(data: PrayerTimes) {
            adapter.addData(data.timingsArrayList)

            binding.tvCurrentDate.text = data.date?.readable
            binding.tvCurrentAddress.text = data.meta?.timezone

            binding.tvNextPrayer.text = data.nextPrayingTime?.name
            binding.tvNextPrayerTime.text = data.nextPrayingTime?.let {
                getDurationBetweenPrayerAndCurrentTime(it)
            }

        }
    }

    private fun getDurationBetweenPrayerAndCurrentTime(prayer: PrayItem): String {
        val pattern = "hh:mm a"
        val currentCalender= Calendar.getInstance()
        val formatter = SimpleDateFormat(pattern, Locale.US)
        val prayerTime = formatter.parse(prayer.time)
        val prayerCalender= Calendar.getInstance().apply { time=prayerTime }
        prayerCalender.apply {
            set(Calendar.YEAR,currentCalender.get(Calendar.YEAR))
            set(Calendar.MONTH,currentCalender.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH,currentCalender.get(Calendar.DAY_OF_MONTH))
            set(Calendar.SECOND,0)
            set(Calendar.MILLISECOND,0)

        }
        Log.e(
            "TAG",
            "getDurationBetweenPrayerAndCurrentTime: ${prayerCalender.time}  ,currnet ${currentCalender.time}",
        )

        val durationInMillis = prayerCalender.time.time - currentCalender.time.time
        val hours = TimeUnit.MILLISECONDS.toHours(durationInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis) % 60
        return "${hours}h:${minutes}m"

    }

    // onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrayerPageViewHolder {
        val binding = ItemPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PrayerPageViewHolder(binding)
    }

    // onBindViewHolder
    override fun onBindViewHolder(holder: PrayerPageViewHolder, position: Int) {
        holder.bind(items[position])
    }

    // getItemCount
    override fun getItemCount(): Int = items.size
}