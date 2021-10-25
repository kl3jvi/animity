package com.kl3jvi.animity.view.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.kl3jvi.animity.databinding.ItemEpisodeNumberBinding
import com.kl3jvi.animity.model.entities.EpisodeModel
import com.kl3jvi.animity.view.activities.player.PlayerActivity

class CustomEpisodeAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<CustomEpisodeAdapter.ViewHolder>() {

    private var list = listOf<EpisodeModel>()


    inner class ViewHolder(view: ItemEpisodeNumberBinding) : RecyclerView.ViewHolder(view.root) {
        val num = view.episodeText
        val progress = view.episodeProgress
        val item = view.watchEpisode
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemEpisodeNumberBinding =
            ItemEpisodeNumberBinding.inflate(LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = list[position]

        holder.num.text = element.episodeNumber.replace("EP", "Episode")
        holder.progress.progress = (0..100).random()

        holder.item.setOnClickListener {

            val intent =
                Intent(fragment.requireActivity(), PlayerActivity::class.java)
            intent.putExtra("episodeUrl", element.episodeurl)
            intent.putExtra("episodeNumber", element.episodeNumber)
            fragment.requireContext().startActivity(intent)

        }
    }

    override fun getItemCount() = list.size

    fun getEpisodeInfo(retrieveData: List<EpisodeModel>) {
        list = retrieveData.reversed()
        notifyDataSetChanged()
    }
}