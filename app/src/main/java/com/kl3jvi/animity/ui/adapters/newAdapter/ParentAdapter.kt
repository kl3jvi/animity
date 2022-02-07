package com.kl3jvi.animity.ui.adapters.newAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kl3jvi.animity.data.model.ui_models.HomeRecycleViewItemData
import com.kl3jvi.animity.databinding.ItemListWithHeaderBinding

open class ParentAdapter(val playButtonFlag: Boolean = false) :
    ListAdapter<HomeRecycleViewItemData, ParentAdapter.ParentViewHolder>(MainDiffUtil()) {

    override fun getItemCount(): Int = currentList.size

    class ParentViewHolder(private val binding: ItemListWithHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val childAdapter by lazy { AnimeCardAdapter() }
        fun bind(data: HomeRecycleViewItemData) {
            binding.contentTitle.text = data.headerTitle
            childAdapter.submitList(data.listOfAnimeMetaModel)
            binding.childRecyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            binding.childRecyclerView.adapter = childAdapter
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val binding: ItemListWithHeaderBinding =
            ItemListWithHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParentViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) =
        holder.bind(getItem(position))


}