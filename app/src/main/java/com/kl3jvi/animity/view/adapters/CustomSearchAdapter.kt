package com.kl3jvi.animity.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.kl3jvi.animity.databinding.SearchLayoutBinding
import com.kl3jvi.animity.model.entities.AnimeMetaModel


class CustomSearchAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<CustomSearchAdapter.ViewHolder>() {

    private var list: List<AnimeMetaModel> = listOf()


    inner class ViewHolder(view: SearchLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val image = view.imageView
        val title = view.imageText
        val extra = view.imageTextExtra
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: SearchLayoutBinding =
            SearchLayoutBinding.inflate(LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.image.load(item.imageUrl)
        holder.title.text = item.title
        holder.extra.text = item.releasedDate
    }

    override fun getItemCount() = list.size

    fun passSearchData(entry: List<AnimeMetaModel>) {
        list = entry
        notifyDataSetChanged()
    }
}