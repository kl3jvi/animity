package com.kl3jvi.animity.ui.fragments.schedule

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kl3jvi.animity.data.enums.WeekName
import com.kl3jvi.animity.data.mapper.AiringInfo

class DayPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(
    fragmentManager,
    lifecycle,
) {
    private val weekDays = WeekName.values()
    private var scheduleMap: Map<WeekName, List<AiringInfo>> = emptyMap()

    init {
        setData(scheduleMap)
    }

    override fun getItemCount(): Int = weekDays.size

    override fun createFragment(position: Int): Fragment {
        val day = weekDays[position]
        val airingInfo = scheduleMap[day]
        return DayAnimeFragment.newInstance(airingInfo ?: emptyList())
    }

    fun setData(newData: Map<WeekName, List<AiringInfo>>) {
        val diffCallback = ScheduleDiffCallback(scheduleMap, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        scheduleMap = newData
        diffResult.dispatchUpdatesTo(this)
    }
}

private class ScheduleDiffCallback(
    private val oldMap: Map<WeekName, List<AiringInfo>>,
    private val newMap: Map<WeekName, List<AiringInfo>>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = WeekName.values().size

    override fun getNewListSize(): Int = WeekName.values().size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
    ): Boolean {
        // Since WeekName is constant, items are always the same.
        return oldItemPosition == newItemPosition
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
    ): Boolean {
        val weekName = WeekName.values()[oldItemPosition]
        val oldData = oldMap[weekName]
        val newData = newMap[weekName]
        return oldData == newData
    }
}
