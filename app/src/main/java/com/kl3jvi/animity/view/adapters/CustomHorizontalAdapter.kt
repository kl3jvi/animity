package com.kl3jvi.animity.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy

import com.kl3jvi.animity.databinding.ItemCardAnimeBinding
import com.kl3jvi.animity.model.entities.AnimeMetaModel
import com.kl3jvi.animity.view.fragments.home.HomeFragment

class CustomHorizontalAdapter(
    private val fragment: Fragment,
    private val animes: ArrayList<AnimeMetaModel>
) : RecyclerView.Adapter<CustomHorizontalAdapter.ViewHolder>() {

    inner class ViewHolder(view: ItemCardAnimeBinding) : RecyclerView.ViewHolder(view.root) {
        val title = view.animeTitle
        val image = view.animeImage
        val episodeNumber = view.episodeNumber
        val card = view.backgroundImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCardAnimeBinding =
            ItemCardAnimeBinding.inflate(LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = animes[position]
        holder.image.load(element.imageUrl) {
            crossfade(true)
            diskCachePolicy(CachePolicy.ENABLED)
        }

        holder.title.text = element.title
        holder.episodeNumber.text = element.episodeNumber

        holder.card.setOnClickListener {
            if (fragment is HomeFragment) {
                fragment.animeDetails(element)
            }
        }
    }

    override fun getItemCount() = animes.size





    fun addAnimes(animes:List<AnimeMetaModel>) {
        this.animes.apply {
            clear()
            addAll(animes)
        }
    }


}