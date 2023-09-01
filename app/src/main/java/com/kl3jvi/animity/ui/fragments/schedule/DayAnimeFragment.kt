package com.kl3jvi.animity.ui.fragments.schedule

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.kl3jvi.animity.R
import com.kl3jvi.animity.data.mapper.AiringInfo
import com.kl3jvi.animity.databinding.FragmentAiringBinding
import com.kl3jvi.animity.todaySelection

class DayAnimeFragment : Fragment(R.layout.fragment_airing) {

    private var binding: FragmentAiringBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAiringBinding.bind(view)
        val args = requireArguments()
        val data = args.getParcelableArrayList<AiringInfo>(ARG_ANIME_LIST)!!

        loadData(data)
    }

    private fun loadData(data: List<AiringInfo>) {
        binding?.epoxyRecyclerView?.withModels {
            data.forEach { airingInfo ->
                todaySelection {
                    id(airingInfo.media?.idAniList)
                    animeInfo(airingInfo)
                    clickListener { view ->
                        airingInfo.media?.let {
                            val directions = ScheduleFragmentDirections.scheduleToDetails(
                                it,
                                airingInfo.episode ?: 0,
                            )
                            view.findNavController().navigate(directions)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        private const val ARG_ANIME_LIST = "arg_anime_list"

        fun newInstance(airingInfo: List<AiringInfo>): DayAnimeFragment {
            return DayAnimeFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_ANIME_LIST, ArrayList(airingInfo))
                }
            }
        }
    }
}
