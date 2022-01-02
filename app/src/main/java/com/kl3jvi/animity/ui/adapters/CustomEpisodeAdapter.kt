package com.kl3jvi.animity.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kl3jvi.animity.data.model.EpisodeModel
import com.kl3jvi.animity.databinding.ItemEpisodeListBinding
import com.kl3jvi.animity.ui.activities.player.PlayerActivity
import com.kl3jvi.animity.ui.fragments.details.DetailsFragment
import com.kl3jvi.animity.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CustomEpisodeAdapter(
    private val fragment: Fragment,
    private val animeTitle: String,
) : ListAdapter<EpisodeModel, CustomEpisodeAdapter.ViewHolder>(AnimeDiffCallback()) {


    inner class ViewHolder(private val binding: ItemEpisodeListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                setClickListener {
                    binding.episodeInfo?.let { element ->
                        val context = fragment.requireContext()
                        val intent = Intent(context, PlayerActivity::class.java)
                        intent.putExtra(Constants.EPISODE_DETAILS, element)
                        intent.putExtra(Constants.ANIME_TITLE, animeTitle)
                        context.startActivity(intent)
                    }
                }

//                setDownloadClickListener {
//                    binding.episodeInfo?.let { episodeModel->
//                        if (fragment is DetailsFragment) fragment.getM3U8EpisodeUrl(episodeModel.episodeUrl)
//                    }
//                }
            }
        }

        fun bindEpisodeInfo(episodeInfo: EpisodeModel) {
            binding.episodeInfo = episodeInfo
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemEpisodeListBinding =
            ItemEpisodeListBinding.inflate(LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindEpisodeInfo(getItem(position))

    override fun getItemCount() = currentList.size

    private class AnimeDiffCallback : DiffUtil.ItemCallback<EpisodeModel>() {

        override fun areItemsTheSame(
            oldItem: EpisodeModel,
            newItem: EpisodeModel
        ): Boolean {
            return oldItem.toString() == newItem.toString()
        }

        override fun areContentsTheSame(
            oldItem: EpisodeModel,
            newItem: EpisodeModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}

