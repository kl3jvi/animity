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
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.chip.Chip
import com.kl3jvi.animity.R
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.data.model.ui_models.Genre
import com.kl3jvi.animity.data.model.ui_models.getHexColor
import com.kl3jvi.animity.data.model.ui_models.toStateListColor
import com.kl3jvi.animity.databinding.FragmentDetailsBinding
import com.kl3jvi.animity.episodeLarge
import com.kl3jvi.animity.ui.activities.player.PlayerActivity
import com.kl3jvi.animity.utils.*
import com.kl3jvi.animity.utils.Constants.Companion.getColor
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val viewModel: DetailsViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()
    private val animeDetails get() = args.animeDetails
    private var binding: FragmentDetailsBinding? = null

    private lateinit var bookMarkMenuItem: MenuItem
    private lateinit var title: String
    private var check = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)
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
            viewModel.animeMetaModel.update { animeInfo }
            binding?.apply {
                detailsPoster.load(animeInfo.coverImage.large) { crossfade(true) }
                resultTitle.text = animeInfo.title.userPreferred
                title = animeInfo.title.userPreferred
                imageButton.setOnClickListener {
                    viewModel.reverseState.value = !viewModel.reverseState.value.also {
                        imageButton.load(
                            if (it) R.drawable.ic_up_arrow else R.drawable.ic_down_arrow
                        )
                    }
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
                animeInfoLayout.textOverview.setHtmlText(info.description)
                releaseDate.text = info.startDate?.getDate()
                status.text = info.status?.name
                type.text = info.type?.rawValue

                /* Checking if the nextAiringEpisode is not null, if it is not null, then it will run
                the displayInDayDateTimeFormat function on it. */
                releaseTime.text = info.nextAiringEpisode.takeIf {
                    it != null
                }?.run(::displayInDayDateTimeFormat)

                animeInfoLayout.textOverview.visibility = VISIBLE
                releaseDate.visibility = VISIBLE
                status.visibility = VISIBLE
                type.visibility = VISIBLE

                resultEpisodesText.visibility = VISIBLE
                resultPlayMovie.visibility = GONE
                episodeListRecycler.visibility = VISIBLE
                createGenreChips(info.genres)
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
    private fun createGenreChips(genre: List<Genre>) {
        binding?.genreGroup?.removeAllViews()
        genre.forEach { data ->
            val chip = Chip(requireContext())
            chip.apply {
                text = data.name
                val color = data.getHexColor().toStateListColor()
                setTextColor(Color.WHITE)
                chipStrokeColor = getColor()
                chipStrokeWidth = 3f
                chipBackgroundColor = color
            }
            binding?.genreGroup?.addView(chip)
        }
    }

    /**
     * We're inflating the menu, setting the menu to the menu we just inflated, and then observing the
     * database to see if the user has favorite the current movie
     *
     * @param menu The menu object that you want to inflate.
     * @param inflater The MenuInflater that can be used to inflate menu items from XML into the menu.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_menu, menu)
        bookMarkMenuItem = menu[0]
        updateFavoriteStatus()
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * It observes the database for changes and updates the menu icon accordingly.
     */
    private fun updateFavoriteStatus() = runIfFragmentIsAttached {
        check = animeDetails.isFavourite
        bookMarkMenuItem.setIcon(
            if (!check) {
                R.drawable.ic_favorite_uncomplete
            } else {
                R.drawable.ic_favorite_complete
            }
        )
        binding?.setType?.setOnClickListener { v ->
            showMenu(v, R.menu.popup_menu)
        }
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
                }

                R.id.option_2 -> {
                    binding?.setType?.text = requireContext().getText(R.string.watching)
                }

                R.id.option_3 -> {
                    binding?.setType?.text = requireContext().getText(R.string.planning)
                }
            }
            false
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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                runIfFragmentIsAttached {
                    viewModel.episodeList.collect { listOfEpisodeModel ->
                        when (listOfEpisodeModel) {
                            is EpisodeListUiState.Success -> {
                                bindEpisodeList(listOfEpisodeModel.data)
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }

    private fun bindEpisodeList(episodes: List<EpisodeModel>) {
        binding?.episodeListRecycler?.withModels {
            episodes.forEachIndexed { index, episodeModel ->
                episodeLarge {
                    id(UUID.randomUUID().toString())
                    clickListener { _ ->
                        requireContext().launchActivity<PlayerActivity> {
                            putExtra(Constants.EPISODE_DETAILS, episodeModel)
                            putExtra(Constants.ANIME_TITLE, animeDetails.title.userPreferred)
                            putExtra(Constants.MAL_ID, animeDetails.idMal)
                        }
                    }
                    showTitle(episodeModel.episodeName.isNotEmpty())
                    isFiller(episodeModel.isFiller)
                    imageUrl(
                        when {
                            animeDetails.streamingEpisode?.getOrNull(index)?.thumbnail == null -> {
                                animeDetails.bannerImage.ifEmpty { animeDetails.coverImage.large }
                            }

                            animeDetails.streamingEpisode?.getOrNull(index)?.thumbnail != null -> {
                                animeDetails.streamingEpisode?.getOrNull(index)?.thumbnail
                            }

                            else -> animeDetails.coverImage.large
                        }
                    )
                    episodeInfo(episodeModel)
                }
            }
        }
        binding?.resultEpisodesText?.text =
            requireContext().getString(R.string.total_episodes, episodes.size.toString())
        if (episodes.isNotEmpty()) {
            binding?.resultPlayMovie?.setOnClickListener {
                requireActivity().launchActivity<PlayerActivity> {
                    putExtra(Constants.EPISODE_DETAILS, episodes.first())
                    putExtra(Constants.ANIME_TITLE, animeDetails.title.userPreferred)
                    putExtra(Constants.MAL_ID, animeDetails.idMal)
                }
            }
        }
    }

    private fun showLatestEpisodeReleaseTime() {
        binding?.releaseTime?.text = animeDetails.nextAiringEpisode?.parseTime {
            binding?.nextEpisodeContainer?.isVisible = false
        }
    }

    /**
     * When the user clicks on the add to favorites icon, the icon changes to a filled heart and the
     * anime is added to the favorites list
     *
     * @param item MenuItem - The menu item that was selected.
     * @return The superclass method is being returned.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_to_favorites -> {
                check = if (!check) { /* Setting the icon of the menu item at index 0 to the icon with the id
                    `R.drawable.ic_favorite_complete`. */
                    bookMarkMenuItem.setIcon(R.drawable.ic_favorite_complete)
                    showSnack(binding?.root, "Anime added to Favorites")
                    true
                } else {
                    bookMarkMenuItem.setIcon(R.drawable.ic_favorite_uncomplete)
                    showSnack(binding?.root, "Anime removed from Favorites")
                    false
                }
                viewModel.updateAnimeFavorite()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // letting go of the resources to avoid memory leak.
        binding = null
    }
}
