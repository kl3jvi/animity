package com.kl3jvi.animity.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.kl3jvi.animity.databinding.ItemEpisodeNumberBinding
import com.kl3jvi.animity.model.entities.EpisodeModel

class CustomEpisodeAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<CustomEpisodeAdapter.ViewHolder>() {

    private var list = ArrayList<EpisodeModel>()


    inner class ViewHolder(view: ItemEpisodeNumberBinding) : RecyclerView.ViewHolder(view.root) {
        val num = view.episodeNum
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemEpisodeNumberBinding =
            ItemEpisodeNumberBinding.inflate(LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = list[position]
        holder.num.text = element.episodeNumber
    }

    override fun getItemCount() = list.size

    fun getEpisodeInfo(retriveData: ArrayList<EpisodeModel>) {
        list = retriveData
        notifyDataSetChanged()
    }
}