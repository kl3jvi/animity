package com.kl3jvi.animity.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kl3jvi.animity.data.model.ui_models.HomeRecycleViewItemData
import com.kl3jvi.animity.databinding.ItemListWithHeaderBinding

open class ParentAdapter(var isHomeFragment: Boolean = true) :
    ListAdapter<HomeRecycleViewItemData,
            ParentAdapter.ParentViewHolder>(MainDiffUtil<HomeRecycleViewItemData>()) {
    private var sharedPool = RecyclerView.RecycledViewPool()

    inner class ParentViewHolder(private val binding: ItemListWithHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val horizontalChildAdapter by lazy { AnimeCardAdapter(isHomeFragment = isHomeFragment) }

        fun bind(data: HomeRecycleViewItemData) {
            binding.contentTitle.text = data.headerTitle
            binding.childRecyclerView.adapter = horizontalChildAdapter
            horizontalChildAdapter.submitList(data.listOfAnimeMetaModel)
//            verticalChildAdapter.submitList(if (!data.isHorizontalScrollView) data.listOfAnimeMetaModel else emptyList())
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val binding: ItemListWithHeaderBinding =
            ItemListWithHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.childRecyclerView.setRecycledViewPool(sharedPool)
        binding.childRecyclerView.layoutManager =
            LinearLayoutManager(parent.context, RecyclerView.HORIZONTAL, false)
        return ParentViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) =
        holder.bind(getItem(position))

    override fun getItemCount(): Int = currentList.size


}

