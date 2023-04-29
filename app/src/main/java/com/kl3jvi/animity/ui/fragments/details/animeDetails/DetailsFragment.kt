@file:OptIn(FlowPreview::class)

package com.kl3jvi.animity.ui.fragments.details.animeDetails

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayoutMediator
import com.kl3jvi.animity.R
import com.kl3jvi.animity.data.mapper.MediaStatusAnimity
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.data.model.ui_models.Genre
import com.kl3jvi.animity.data.model.ui_models.getColors
import com.kl3jvi.animity.databinding.FragmentDetailsBinding
import com.kl3jvi.animity.type.MediaListStatus
import com.kl3jvi.animity.ui.activities.player.PlayerActivity
import com.kl3jvi.animity.utils.*
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val viewModel: DetailsViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()
    private val animeDetails get() = args.animeDetails
    private val desiredPosition: Int get() = args.desiredPosition
    private var binding: FragmentDetailsBinding? = null

    private lateinit var bookMarkMenuItem: MenuItem
    private lateinit var title: String
    private var check by Delegates.notNull<Boolean>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)
        check = animeDetails.isFavourite
        observeViewModel()
        initViews()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun observeViewModel() {
        fetchAnimeInfo()
        fetchEpisodeList()
        showLatestEpisodeReleaseTime()
    }

    private fun initViews() {
        animeDetails.let { animeInfo ->
            viewModel.animeMetaModel.update {
                it.copy(
                    idAniList = animeInfo.idAniList,
                    idMal = animeInfo.idMal
                )
            }
            binding?.apply {
                detailsPoster.load(animeInfo.coverImage.large) { crossfade(true) }
                resultTitle.text = animeInfo.title.userPreferred
                title = animeInfo.title.userPreferred
                imageButton.setOnClickListener {
                    viewModel.reverseState.value = !viewModel.reverseState.value
                }
                collect(viewModel.reverseState) {
                    imageButton.load(if (it) R.drawable.ic_up_arrow else R.drawable.ic_down_arrow)
                }
                setType.setOnClickListener {
                    showMenu(it, R.menu.popup_menu)
                }
            }
        }
    }

    /**
     * It fetches the anime info and displays it on the screen.
     */
    private fun fetchAnimeInfo() {
        animeDetails.let { info ->
            binding?.apply {
                animeInfoLayout.synopsisExpand.setHtmlText(info.description)
                releaseDate.text = info.startDate?.getDate()
                status.text = info.status?.name
                type.text = info.type?.rawValue

                /* Checking if the nextAiringEpisode is not null, if it is not null, then it will run
                the displayInDayDateTimeFormat function on it. */
                releaseTime.text = info.nextAiringEpisode.takeIf {
                    it != null
                }?.run(::displayInDayDateTimeFormat)

                animeInfoLayout.expandableText.visibility = VISIBLE
                releaseDate.visibility = VISIBLE
                status.visibility = VISIBLE
                type.visibility = VISIBLE

                resultEpisodesText.visibility = VISIBLE
                resultPlayMovie.visibility = GONE
                episodeListRecycler.visibility = VISIBLE
                createGenreChips(info.genres)
                setType.text = info.mediaListEntry?.let {
                    when (it) {
                        MediaStatusAnimity.COMPLETED -> "Completed"
                        MediaStatusAnimity.WATCHING -> "Watching"
                        MediaStatusAnimity.DROPPED -> "Dropped"
                        MediaStatusAnimity.PAUSED -> "Paused"
                        MediaStatusAnimity.PLANNING -> "Planning"
                        MediaStatusAnimity.REPEATING -> "Repeating"
                        MediaStatusAnimity.NOTHING -> "Add to list"
                    }
                }
            }
        }
    }

    /**
     * It takes a number of seconds since the epoch and returns a string in the format "Day, dd Month
     * yyyy, hh:mm a"
     *
     * @param seconds The number of seconds since January 1, 1970 00:00:00 UTC.
     * @return The date in the format of Day, Date Month Year, Hour:Minute AM/PM
     */
    private fun displayInDayDateTimeFormat(seconds: Int): String {
        val dateFormat = SimpleDateFormat("E, dd MMM yyyy, hh:mm a", Locale.getDefault())
        val date = Date(seconds * 1000L)
        return dateFormat.format(date)
    }

    /**
     * It creates a chip for each genre in the list.
     *
     * @param genre List<Genre> - The list of genres that we want to display.
     */
    private fun createGenreChips(genres: List<Genre>) {
        binding?.genreGroup?.apply {
            removeAllViews()
            genres.forEach { genre ->
                val (bgColor, outlineColor) = genre.getColors()
                addView(
                    Chip(requireContext()).apply {
                        text = genre.name
                        setTextColor(Color.WHITE)
                        chipStrokeWidth = 3f
                        chipBackgroundColor = bgColor
                        chipStrokeColor = outlineColor
                    }
                )
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_to_favorites -> {
                toggleFavoriteStatus()
                viewModel.updateAnimeFavorite()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_menu, menu)
        bookMarkMenuItem = menu.findItem(R.id.add_to_favorites)
        updateFavoriteIcon()
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun toggleFavoriteStatus() {
        check = !check
        updateFavoriteIcon()
        val message = if (check) {
            "Anime added to Favorites"
        } else {
            "Anime removed from Favorites"
        }
        showSnack(binding?.root, message)
    }

    private fun updateFavoriteIcon() {
        val iconResId = if (check) {
            R.drawable.ic_favorite_complete
        } else {
            R.drawable.ic_favorite_uncomplete
        }
        bookMarkMenuItem.setIcon(iconResId)
    }

    /**
     * It shows a popup menu when the user clicks on the view.
     *
     * @param v View - The view that the popup menu should be anchored to.
     * @param menuRes The menu resource to inflate.
     */
    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.option_1 -> {
                    binding?.setType?.text = requireContext().getText(R.string.completed)
                    viewModel.changeAnimeStatus(MediaListStatus.COMPLETED)
                }

                R.id.option_2 -> {
                    binding?.setType?.text = requireContext().getText(R.string.watching)
                    viewModel.changeAnimeStatus(MediaListStatus.CURRENT)
                }

                R.id.option_3 -> {
                    binding?.setType?.text = requireContext().getText(R.string.planning)
                    viewModel.changeAnimeStatus(MediaListStatus.PLANNING)
                }

                R.id.option_4 -> {
                    binding?.setType?.text = requireContext().getText(R.string.dropped)
                    viewModel.changeAnimeStatus(MediaListStatus.DROPPED)
                }

                R.id.option_5 -> {
                    binding?.setType?.text = requireContext().getText(R.string.paused)
                    viewModel.changeAnimeStatus(MediaListStatus.PAUSED)
                }
            }
            true
        }
        // Show the popup menu.
        popup.show()
    }

    /**
     * It fetches the episode list from the view model and then populates the recycler view with the
     * episode list
     */
    @ExperimentalCoroutinesApi
    private fun fetchEpisodeList() {
        val viewPager = binding?.episodeListRecycler

        val adapter = EpisodeChunkAdapter(
            childFragmentManager,
            lifecycle,
            emptyList(),
            animeDetails,
            desiredPosition = -1
        )
        viewPager?.adapter = adapter

        collect(viewModel.episodeList) { listOfEpisodeModel ->
            when (listOfEpisodeModel) {
                is EpisodeListUiState.Success -> {
                    adapter.setData(listOfEpisodeModel.episodeChunks)

                    TabLayoutMediator(
                        binding?.chunkedEpisodeTab!!,
                        viewPager!!
                    ) { tab, position ->
                        tab.text = listOfEpisodeModel.chunkTitles[position]
                    }.attach()

                    val episodes = listOfEpisodeModel.episodeChunks.flatten()
                    updateEpisodeNumber(episodes)
                    showLoading(false)
                    goToDesiredPosition()
                }

                is EpisodeListUiState.Error -> {
                    startAppBarCloseTimer()
                    showLoading(false)
                }

                is EpisodeListUiState.Loading -> showLoading(true)
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding?.apply {
            loading.isVisible = show
            episodeListRecycler.isVisible = show.not()
        }
    }

    private fun updateEpisodeNumber(episodes: List<EpisodeModel>) {
        binding?.resultEpisodesText?.text =
            requireContext().getString(
                R.string.total_episodes,
                episodes.size.toString()
            )
        if (episodes.isNotEmpty() && episodes.size == 1) {
            binding?.resultPlayMovie?.setOnClickListener {
                requireActivity().launchActivity<PlayerActivity> {
                    putExtra(Constants.EPISODE_DETAILS, episodes.first())
                    putExtra(Constants.ANIME_TITLE, animeDetails.title.userPreferred)
                    putExtra(Constants.MAL_ID, animeDetails.idMal)
                }
            }
        }
    }

    private fun startAppBarCloseTimer() {
        lifecycleScope.launch {
            while (true) {
                delay(500)
                binding
                    ?.appbar
                    ?.setExpanded(true, true)
            }
        }
    }

    private fun goToDesiredPosition() {
        if (desiredPosition != 0) {
            binding?.appbar?.setExpanded(false, true)
            binding?.chunkedEpisodeTab?.getTabAt(desiredPosition / 50)?.select()
        }
    }

    private fun showLatestEpisodeReleaseTime() {
        binding?.releaseTime?.text = animeDetails.nextAiringEpisode?.parseTime {
            binding?.nextEpisodeContainer?.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // letting go of the resources to avoid memory leak.
        binding = null
    }
}

