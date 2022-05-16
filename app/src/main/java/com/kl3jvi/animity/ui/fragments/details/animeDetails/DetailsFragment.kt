package com.kl3jvi.animity.ui.fragments.details.animeDetails

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.kl3jvi.animity.data.model.ui_models.getHexColor
import com.kl3jvi.animity.databinding.FragmentDetailsBinding
import com.kl3jvi.animity.episodeList
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.activities.player.PlayerActivity
import com.kl3jvi.animity.ui.base.BaseFragment
import com.kl3jvi.animity.ui.fragments.favorites.FavoritesViewModel
import com.kl3jvi.animity.utils.Constants
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
                resultTitle.text = animeInfo.title.romaji
                title = animeInfo.title.romaji
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
                releaseTime.text =
                    if (info.nextAiringEpisode != null) displayInDayDateTimeFormat(info.nextAiringEpisode) else ""
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
                val color = data.getHexColor()
                setTextColor(Color.WHITE)
                chipStrokeColor = getColor()
                chipStrokeWidth = 3f
                chipBackgroundColor = ColorStateList.valueOf(Color.parseColor(color))
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
        collectFlow(favoritesViewModel.favoriteAniListAnimeList) { mediaList ->
            check = mediaList?.any { media ->
                media.idAniList == animeDetails.idAniList
            } ?: false
            menu[0].setIcon(
                if (!check) R.drawable.ic_favorite_uncomplete
                else R.drawable.ic_favorite_complete
            )
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
                    viewModel.updateAnimeFavorite()
                    showSnack(binding.root, "Anime added to Favorites")
                    true
                } else {
                    menu[0].setIcon(R.drawable.ic_favorite_uncomplete)
                    viewModel.updateAnimeFavorite()
                    showSnack(binding.root, "Anime removed from Favorites")
                    false
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * It fetches the episode list from the view model and then populates the recycler view with the
     * episode list
     */
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

    /**
     * It inflates the layout and returns the binding object.
     */
    override fun getViewBinding(): FragmentDetailsBinding =
        FragmentDetailsBinding.inflate(layoutInflater)
}

