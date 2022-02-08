package com.kl3jvi.animity.ui.adapters.newAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kl3jvi.animity.data.model.ui_models.HomeRecycleViewItemData
import com.kl3jvi.animity.databinding.ItemListWithHeaderBinding
import com.kl3jvi.animity.ui.adapters.CustomVerticalAdapter

open class ParentAdapter(var playButtonFlag: Boolean = false) : ListAdapter<HomeRecycleViewItemData,
        ParentAdapter.ParentViewHolder>(MainDiffUtil<HomeRecycleViewItemData>()) {
    override fun getItemCount(): Int = currentList.size

    class ParentViewHolder(private val binding: ItemListWithHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var sharedPool = RecyclerView.RecycledViewPool()

        private val horizontalChildAdapter by lazy { AnimeCardAdapter(playButtonFlag = false) }
        private val verticalChildAdapter by lazy { CustomVerticalAdapter() }
        fun bind(data: HomeRecycleViewItemData) {
            binding.contentTitle.text = data.headerTitle
            if (data.isHorizontalScrollView) {
                binding.childRecyclerView.layoutManager =
                    LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                binding.childRecyclerView.setRecycledViewPool(sharedPool)
                binding.childRecyclerView.adapter = horizontalChildAdapter
            } else {
                binding.childRecyclerView.layoutManager =
                    LinearLayoutManager(itemView.context)
                binding.childRecyclerView.setRecycledViewPool(sharedPool)
                binding.childRecyclerView.adapter = verticalChildAdapter
            }
            horizontalChildAdapter.submitList(data.listOfAnimeMetaModel)
            verticalChildAdapter.submitList(if (!data.isHorizontalScrollView) data.listOfAnimeMetaModel else emptyList())
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

