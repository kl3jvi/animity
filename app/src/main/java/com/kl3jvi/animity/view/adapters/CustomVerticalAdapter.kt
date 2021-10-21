package com.kl3jvi.animity.view.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.google.android.material.chip.Chip
import com.kl3jvi.animity.databinding.ItemTodaySelectionBinding
import com.kl3jvi.animity.model.entities.AnimeMetaModel
import com.kl3jvi.animity.utils.Constants.Companion.getVerticalAdapterBackgroundColor
import com.kl3jvi.animity.view.fragments.home.HomeFragment

class CustomVerticalAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<CustomVerticalAdapter.ViewHolder>() {

    private var list: List<AnimeMetaModel> = listOf()

    inner class ViewHolder(view: ItemTodaySelectionBinding) : RecyclerView.ViewHolder(view.root) {
        val title = view.animeTitle
        val image = view.animeImage
        val episodeNumber = view.episodeNumber
        val chipGroup = view.chipGroup
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemTodaySelectionBinding =
            ItemTodaySelectionBinding.inflate(LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = list[position]
        holder.image.load(element.imageUrl) {
            crossfade(true)
            diskCachePolicy(CachePolicy.ENABLED)
        }
        holder.title.text = element.title
        holder.episodeNumber.text = element.episodeNumber
        holder.chipGroup.removeAllViews()
        val arrayOfGenres = element.genreList
        arrayOfGenres?.let { genres ->
            genres.forEach { data ->
                if (data.genreName.isNotBlank()) {
                    val chip = Chip(fragment.requireContext())
                    chip.apply {
                        text = data.genreName
                        setTextColor(Color.WHITE)
                        chipStrokeColor = getColor()
                        chipStrokeWidth = 3f
                        chipBackgroundColor = getVerticalAdapterBackgroundColor()
                    }
                    holder.chipGroup.addView(chip)
                } else
                    holder.chipGroup.removeAllViews()
            }
        }

        holder.itemView.setOnClickListener {
            if (fragment is HomeFragment) {
                fragment.animeDetails(element)
            }
        }
    }

    private fun getColor(): ColorStateList {
        val color: Int = Color.argb(255, 4, 138, 129)
        return ColorStateList.valueOf(color)
    }

    override fun getItemCount() = list.size

    fun getSelectedAnime(entry: List<AnimeMetaModel>) {
        list = entry
        notifyDataSetChanged()
    }
}