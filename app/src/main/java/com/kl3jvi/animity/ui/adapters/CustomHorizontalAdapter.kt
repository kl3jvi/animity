package com.kl3jvi.animity.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.databinding.ItemCardAnimeBinding
import com.kl3jvi.animity.utils.Constants.Companion.TYPE_POPULAR_ANIME
import com.kl3jvi.animity.utils.Constants.Companion.TYPE_RECENT_SUB
//
//class CustomHorizontalAdapter(val playButtonFlag: Boolean = true) :
//    ListAdapter<AnimeMetaModel, HomeRecyclerViewHolder.AnimeViewHolder>(AnimeDiffCallback()) {
//
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): HomeRecyclerViewHolder.AnimeViewHolder {
//        val binding: ItemCardAnimeBinding =
//            ItemCardAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return HomeRecyclerViewHolder.AnimeViewHolder(binding, playButtonFlag = playButtonFlag)
//    }
//
//    override fun onBindViewHolder(holder: HomeRecyclerViewHolder.AnimeViewHolder, position: Int) {
//        when (getItem(position).typeValue) {
//            TYPE_RECENT_SUB -> {
//                holder.bindAnimeInfo(getItem(position) as HomeRecyclerViewItem.Anime)
//            }
//            TYPE_POPULAR_ANIME -> {
//                holder.bindAnimeInfo(getItem(position))
//
//            }
//        }
//    }
//
//
//    override fun getItemCount(): Int = currentList.size
//
//    private class AnimeDiffCallback : DiffUtil.ItemCallback<AnimeMetaModel>() {
//
//        override fun areItemsTheSame(
//            oldItem: AnimeMetaModel,
//            newItem: AnimeMetaModel
//        ): Boolean {
//            return oldItem.toString() == newItem.toString()
//        }
//
//        override fun areContentsTheSame(
//            oldItem: AnimeMetaModel,
//            newItem: AnimeMetaModel
//        ): Boolean {
//            return oldItem == newItem
//        }
//    }
//}
