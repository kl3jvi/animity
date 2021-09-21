package com.kl3jvi.animity.view.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.kl3jvi.animity.databinding.FragmentDetailsBinding


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DetailsFragmentArgs by navArgs()

        args.animeDetails.let { animeInfo ->
            Glide.with(this)
                .load(animeInfo.imageUrl)
                .into(binding.appBarImage)
            binding.textView6.text = animeInfo.title

            animeInfo.genreList?.let {
                it.forEach { genre ->
                    println(genre)
                }
            }
        }
    }


}