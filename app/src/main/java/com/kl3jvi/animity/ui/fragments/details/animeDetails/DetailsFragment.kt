package com.kl3jvi.animity.ui.fragments.details.animeDetails

import android.graphics.Color
import android.os.Bundle
import android.text.format.DateUtils
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.chip.Chip
import com.kl3jvi.animity.R
import com.kl3jvi.animity.data.model.ui_models.Genre
import com.kl3jvi.animity.data.model.ui_models.getHexColor
import com.kl3jvi.animity.data.model.ui_models.toStateListColor
import com.kl3jvi.animity.databinding.FragmentDetailsBinding
import com.kl3jvi.animity.episodeLarge
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.activities.player.PlayerActivity
import com.kl3jvi.animity.ui.base.BaseFragment
import com.kl3jvi.animity.ui.fragments.favorites.FavoritesUiState
import com.kl3jvi.animity.ui.fragments.favorites.FavoritesViewModel
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Constants.Companion.getColor
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import com.kl3jvi.animity.utils.collectFlow
import com.kl3jvi.animity.utils.launchActivity
import com.kl3jvi.animity.utils.setHtmlText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.ParseException
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
    ): View = binding.root


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
                resultTitle.text = animeInfo.title.userPreferred
                title = animeInfo.title.userPreferred
            }
        }
    }


    /**
     * It fetches the anime info and displays it on the screen.
     */
    private fun fetchAnimeInfo() {
        animeDetails.let { info ->
            binding.apply {
                animeInfoLayout.textOverview.setHtmlText(info.description)
                releaseDate.text = info.startDate?.getDate()
                status.text = info.status?.name
                type.text = info.type?.rawValue

                /* Checking if the nextAiringEpisode is not null, if it is not null, then it will run
                the displayInDayDateTimeFormat function on it. */
                releaseTime.text = info.nextAiringEpisode.takeIf {
                    it != null
                }?.run { displayInDayDateTimeFormat(this) }

                animeInfoLayout.textOverview.visibility = VISIBLE
                releaseDate.visibility = VISIBLE
                status.visibility = VISIBLE
                type.visibility = VISIBLE
                detailsProgress.visibility = GONE

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
        binding.genreGroup.removeAllViews()
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
            binding.genreGroup.addView(chip)
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
        this.menu = menu
        observeDatabase()
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * It observes the database for changes and updates the menu icon accordingly.
     */
    private fun observeDatabase() {
        collectFlow(favoritesViewModel.favoritesList) { mediaList ->
            if (mediaList is FavoritesUiState.Success) {
                check = mediaList.data.any { media -> media.idAniList == animeDetails.idAniList }
                menu[0].setIcon(
                    if (!check) R.drawable.ic_favorite_uncomplete
                    else R.drawable.ic_favorite_complete
                )
            } else check = false
        }
        binding.setType.setOnClickListener { v ->
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


    /**
     * It fetches the episode list from the view model and then populates the recycler view with the
     * episode list
     */
    @ExperimentalCoroutinesApi
    private fun fetchEpisodeList() {
        collectFlow(viewModel.episodeList) { episodeListResponse ->
            when (episodeListResponse) {
                EpisodeListUiState.Error -> {}
                EpisodeListUiState.Loading -> {}
                is EpisodeListUiState.Success -> {
                    binding.detailsProgress.visibility = GONE
                    binding.episodeListRecycler.withModels {
                        episodeListResponse.data.forEachIndexed { index, episodeModel ->
                            episodeLarge {
                                id(episodeModel.episodeNumber)
                                clickListener { _ ->
                                    requireContext().launchActivity<PlayerActivity> {
                                        putExtra(Constants.EPISODE_DETAILS, episodeModel)
                                        putExtra(
                                            Constants.ANIME_TITLE,
                                            animeDetails.title.userPreferred
                                        )
                                        putExtra(
                                            Constants.MAL_ID,
                                            animeDetails.idMal
                                        )
                                    }
                                }
                                showTitle(episodeModel.episodeName.isNotEmpty())
                                isFiller(episodeModel.isFiller)
                                imageUrl(
                                    when {
                                        animeDetails.streamingEpisode?.getOrNull(index)?.thumbnail == null -> {
                                            animeDetails.bannerImage.ifEmpty {
                                                animeDetails.coverImage.large
                                            }
                                        }

                                        animeDetails.streamingEpisode?.getOrNull(index)?.thumbnail != null -> {
                                            animeDetails.streamingEpisode?.getOrNull(index)?.thumbnail
                                        }

                                        else -> {
                                            animeDetails.coverImage.large
                                        }
                                    }
                                )
                                episodeInfo(episodeModel)
                            }
                        }
                    }
                    binding.resultEpisodesText.text =
                        requireContext().getString(
                            R.string.total_episodes,
                            episodeListResponse.data.size.toString()
                        )
                    if (episodeListResponse.data.isNotEmpty()) {
                        binding.resultPlayMovie.setOnClickListener {
                            requireActivity().launchActivity<PlayerActivity> {
                                putExtra(
                                    Constants.EPISODE_DETAILS,
                                    episodeListResponse.data.first()
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
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.hideBottomNavBar()
        }
    }

    private fun showLatestEpisodeReleaseTime() {
        binding.releaseTime.text = animeDetails.nextAiringEpisode?.parseTime()
    }

    private fun Int.parseTime(): CharSequence {
        return try {
            val now = System.currentTimeMillis()
            DateUtils.getRelativeTimeSpanString(now, toLong(), DateUtils.MINUTE_IN_MILLIS)
        } catch (e: ParseException) {
            e.printStackTrace()
            binding.nextEpisodeContainer.isVisible = false
            ""
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
                check = if (!check) {
                    /* Setting the icon of the menu item at index 0 to the icon with the id
                    `R.drawable.ic_favorite_complete`. */
                    menu[0].setIcon(R.drawable.ic_favorite_complete)
                    showSnack(binding.root, "Anime added to Favorites")
                    true
                } else {
                    menu[0].setIcon(R.drawable.ic_favorite_uncomplete)
                    showSnack(binding.root, "Anime removed from Favorites")
                    false
                }
                viewModel.updateAnimeFavorite()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * It inflates the layout and returns the binding object.
     */
    override fun getViewBinding(): FragmentDetailsBinding =
        FragmentDetailsBinding.inflate(layoutInflater)
}



