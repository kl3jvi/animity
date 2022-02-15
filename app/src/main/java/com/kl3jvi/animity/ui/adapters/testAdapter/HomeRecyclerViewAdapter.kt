package com.kl3jvi.animity.ui.adapters.testAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.ItemHorizontalRecyclerBinding
import com.kl3jvi.animity.databinding.ItemTitleBinding
import com.kl3jvi.animity.databinding.ItemTodaySelectionBinding
import com.kl3jvi.animity.ui.adapters.MainDiffUtil

class HomeRecyclerViewAdapter :
    ListAdapter<HomeRecyclerViewItem, HomeRecyclerViewHolder>(MainDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecyclerViewHolder {
        return when (viewType) {
            R.layout.item_title -> HomeRecyclerViewHolder.TitleViewHolder(
                ItemTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.item_horizontal_recycler -> HomeRecyclerViewHolder.HorizontalViewHolder(
                ItemHorizontalRecyclerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.item_today_selection -> HomeRecyclerViewHolder.VerticalViewHolder(
                ItemTodaySelectionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Invalid ViewType Provided")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: HomeRecyclerViewHolder, position: Int) {
        when (holder) {
            is HomeRecyclerViewHolder.VerticalViewHolder -> holder.bindAnimeItem(getItem(position) as HomeRecyclerViewItem.AnimeVertical)
            is HomeRecyclerViewHolder.HorizontalViewHolder -> holder.bind(currentList as List<HomeRecyclerViewItem.Anime>)
            is HomeRecyclerViewHolder.TitleViewHolder -> holder.bind(getItem(position) as HomeRecyclerViewItem.Title)
        }
    }

    override fun getItemCount() = currentList.size

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is HomeRecyclerViewItem.Anime -> R.layout.item_horizontal_recycler
            is HomeRecyclerViewItem.AnimeVertical -> R.layout.item_today_selection
            is HomeRecyclerViewItem.Title -> R.layout.item_title
        }
    }
}