package com.kl3jvi.animity.ui.adapters.testAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.ItemHorizontalRecyclerBinding
import com.kl3jvi.animity.databinding.ItemTitleBinding
import com.kl3jvi.animity.databinding.ItemTodaySelectionBinding
import com.kl3jvi.animity.ui.adapters.MainDiffUtil

class HomeRecyclerViewAdapter :
    ListAdapter<HomeRecyclerViewItem, HomeRecyclerViewHolder>(MainDiffUtil()) {

    private val sharedViewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecyclerViewHolder {
        return when (viewType) {
            R.layout.item_title -> HomeRecyclerViewHolder.TitleViewHolder(
                binding = ItemTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.item_horizontal_recycler -> HomeRecyclerViewHolder.HorizontalViewHolder(
                binding = ItemHorizontalRecyclerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                sharedViewPool = sharedViewPool
            )
            R.layout.item_today_selection -> HomeRecyclerViewHolder.VerticalViewHolder(
                binding = ItemTodaySelectionBinding.inflate(
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
            is HomeRecyclerViewHolder.TitleViewHolder -> holder.bindTitle(getItem(position) as HomeRecyclerViewItem.Title)
            is HomeRecyclerViewHolder.HorizontalViewHolder -> holder.bindList(getItem(position) as HomeRecyclerViewItem.HorizontalAnimeWrapper)
            is HomeRecyclerViewHolder.VerticalViewHolder -> holder.bindVerticalAnime(getItem(position) as HomeRecyclerViewItem.Anime)
        }
    }

    override fun getItemCount() = currentList.size

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is HomeRecyclerViewItem.HorizontalAnimeWrapper -> R.layout.item_horizontal_recycler
            is HomeRecyclerViewItem.Anime -> R.layout.item_today_selection
            is HomeRecyclerViewItem.Title -> R.layout.item_title
            else -> throw java.lang.IllegalArgumentException("Couldn't find a view that matches this type of data.")
        }
    }
}