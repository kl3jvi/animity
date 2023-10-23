package com.kl3jvi.animity.ui.fragments.schedule

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.kl3jvi.animity.R
import com.kl3jvi.animity.data.enums.WeekName
import com.kl3jvi.animity.databinding.FragmentScheduleBinding
import com.kl3jvi.animity.utils.collect
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class ScheduleFragment : Fragment(R.layout.fragment_schedule) {
    private var binding: FragmentScheduleBinding? = null
    private val viewModel: ScheduleViewModel by activityViewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentScheduleBinding.bind(view)
        if (isAdded) {
            setupTabsAndViewPager()
        }
    }

    private fun setupTabsAndViewPager() {
        val viewPager = binding?.viewPager
        val tabLayout = binding?.tabs
        val calendar = Calendar.getInstance()
        val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val adjustedDayOfWeek = if (currentDayOfWeek == Calendar.SUNDAY) 6 else currentDayOfWeek - 2
        val adapter = DayPagerAdapter(childFragmentManager, lifecycle)
        viewPager?.adapter = adapter
        collect(viewModel.airingAnimeSchedule) {
            if (!isAdded) return@collect
            if (viewPager != null && tabLayout != null) {
                adapter.setData(it)
                viewPager.setCurrentItem(adjustedDayOfWeek, false)
                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    tab.text = WeekName.values()[position].toString()
                }.attach()
            }
        }
    }
}
