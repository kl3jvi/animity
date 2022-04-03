package com.kl3jvi.animity.ui.fragments.details.animeDetails

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.chip.Chip
import com.kl3jvi.animity.R
import com.kl3jvi.animity.data.model.ui_models.Genre
import com.kl3jvi.animity.databinding.FragmentDetailsBinding
import com.kl3jvi.animity.episodeList
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.activities.player.PlayerActivity
import com.kl3jvi.animity.ui.base.BaseFragment
import com.kl3jvi.animity.ui.fragments.favorites.FavoritesViewModel
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Constants.Companion.getBackgroundColor
import com.kl3jvi.animity.utils.Constants.Companion.getColor
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import com.kl3jvi.animity.utils.collectFlow
import com.kl3jvi.animity.utils.launchActivity
import com.kl3jvi.animity.utils.setHtmlText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DetailsFragment : BaseFragment<DetailsViewModel, FragmentDetailsBinding>() {

    override val viewModel: DetailsViewModel by viewModels()
    private val favoritesViewModel: FavoritesViewModel by activityViewModels()
    private val args: DetailsFragmentArgs by navArgs()
    private val animeDetails get() = args.animeDetails

    private lateinit var menu: Menu
    private lateinit var title: String
    private var check = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun observeViewModel() {
        fetchAnimeInfo()
        fetchEpisodeList()
        showLatestEpisodeReleaseTime()
    }

    override fun initViews() {
        animeDetails.let { animeInfo ->
            viewModel.animeMetaModel.value = animeInfo
            binding.apply {
                detailsPoster.load(animeInfo.coverImage.large) { crossfade(true) }
                resultTitle.text = animeInfo.title.romaji
                title = animeInfo.title.romaji
            }
        }
    }


    private fun fetchAnimeInfo() {
        animeDetails.let { info ->

            binding.animeInfoLayout.textOverview.setHtmlText(info.description)

            binding.releaseDate.text = info.startDate?.getDate()
            binding.status.text = info.status?.name
            binding.type.text = info.type?.rawValue
            binding.releaseTime.text =
                if (info.nextAiringEpisode != null) displayInDayDateTimeFormat(info.nextAiringEpisode) else ""
            binding.animeInfoLayout.textOverview.visibility = VISIBLE
            binding.releaseDate.visibility = VISIBLE
            binding.status.visibility = VISIBLE
            binding.type.visibility = VISIBLE
            binding.detailsProgress.visibility = GONE

            binding.apply {
                resultEpisodesText.visibility = VISIBLE
                resultPlayMovie.visibility = GONE
                episodeListRecycler.visibility = VISIBLE
            }
            createGenreChips(info.genres)
        }
    }

    private fun displayInDayDateTimeFormat(seconds: Int): String {
        val dateFormat = SimpleDateFormat("E, dd MMM yyyy, hh:mm a", Locale.getDefault())
        val date = Date(seconds * 1000L)
        return dateFormat.format(date)
    }

    private fun createGenreChips(genre: List<Genre>) {
        genre.forEach { data ->
            binding.genreGroup.removeAllViews()
            val chip = Chip(requireContext())
            chip.apply {
                text = data.name
                setTextColor(Color.WHITE)
                chipStrokeColor = getColor()
                chipStrokeWidth = 3f
                chipBackgroundColor = getBackgroundColor()
            }
            binding.genreGroup.addView(chip)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_menu, menu)
        this.menu = menu
        observeDatabase()
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun observeDatabase() {
        collectFlow(favoritesViewModel.favoriteAniListAnimeList) {
            check = it?.any { media ->
                media.idAniList == animeDetails.idAniList
            } ?: false
            if (!check) {
                menu[1].setIcon(R.drawable.ic_favorite_uncomplete)
            } else {
                menu[1].setIcon(R.drawable.ic_favorite_complete)
            }
        }
        binding.setType.setOnClickListener { v ->
            showMenu(v, R.menu.popup_menu)
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.option_1 -> {
                    binding.setType.text = requireContext().getText(R.string.completed)
                }
                R.id.option_2 -> {
                    binding.setType.text = requireContext().getText(R.string.watching)
                }
                R.id.option_3 -> {
                    binding.setType.text = requireContext().getText(R.string.planning)
                }
            }
            false
        }
        // Show the popup menu.
        popup.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_to_favorites -> {
                check = if (!check) {
                    menu[1].setIcon(R.drawable.ic_favorite_complete)
                    viewModel.updateAnimeFavorite()
                    showSnack(binding.root, "Anime added to Favorites")
                    true
                } else {
                    menu[1].setIcon(R.drawable.ic_favorite_uncomplete)
                    viewModel.updateAnimeFavorite()
                    showSnack(binding.root, "Anime removed from Favorites")
                    false
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @ExperimentalCoroutinesApi
    private fun fetchEpisodeList() {
        collectFlow(viewModel.episodeList) { episodeListResponse ->
            episodeListResponse?.let { episodeList ->
                binding.detailsProgress.visibility = GONE
                binding.episodeListRecycler.withModels {
                    episodeList.forEach {
                        episodeList {
                            id(it.episodeNumber)
                            clickListener { _ ->
                                requireContext().launchActivity<PlayerActivity> {
                                    putExtra(Constants.EPISODE_DETAILS, it)
                                    putExtra(Constants.ANIME_TITLE, animeDetails.title.romaji)
                                }
                            }
                            episodeInfo(it)
                        }
                    }
                }
                binding.resultEpisodesText.text =
                    requireContext().getString(
                        R.string.total_episodes,
                        episodeList.size.toString()
                    )
                if (episodeList.isNotEmpty()) {
                    binding.resultPlayMovie.setOnClickListener {
                        requireActivity().launchActivity<PlayerActivity> {
                            putExtra(
                                Constants.EPISODE_DETAILS,
                                episodeList.first()
                            )
                            putExtra(Constants.ANIME_TITLE, title)
                        }
                        binding.resultPlayMovie.visibility = VISIBLE
                    }
                } else {
                    binding.resultPlayMovie.visibility = GONE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.hideBottomNavBar()
        }
    }

    private fun showLatestEpisodeReleaseTime() {
//        viewModel.lastEpisodeReleaseTime.observe(viewLifecycleOwner) { res ->
//            when (res) {
//                is Resource.Success -> {
//                    res.data?.let {
//                        if (it.time.isNotEmpty()) {
//                            binding.nextEpisodeContainer.visibility = VISIBLE
//                            binding.releaseTime.text = " ${it.time}"
//                        } else binding.nextEpisodeContainer.visibility = GONE
//                    }
//                }
//                is Resource.Error -> {
//                    binding.nextEpisodeContainer.visibility = GONE
//                }
//                is Resource.Loading -> {
//                    binding.nextEpisodeContainer.visibility = GONE
//                }
//            }
//        }
    }

    override fun getViewBinding(): FragmentDetailsBinding =
        FragmentDetailsBinding.inflate(layoutInflater)
}

