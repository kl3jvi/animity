package com.kl3jvi.animity.view.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kl3jvi.animity.databinding.FragmentDetailsBinding
import com.kl3jvi.animity.model.network.ApiHelper
import com.kl3jvi.animity.model.network.RetrofitBuilder
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Status


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailsViewModel by viewModels {
        DetailsViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
    }

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
        val randomNum = (0..4).random()
        args.animeDetails.let { animeInfo ->
            Glide.with(this)
                .load(Constants.DETAILS_BACKGROUND[randomNum])
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.appBarImage)

            Glide.with(this)
                .load(animeInfo.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.icon)

            binding.textView6.text = animeInfo.title

            animeInfo.categoryUrl?.let { url ->
                viewModel.fetchAnimeInfo(url).observe(viewLifecycleOwner, { res ->
                    res?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                resource.data?.let { info ->
                                    binding.expandTextView.text = info.plotSummary
                                }
                            }
                            Status.ERROR -> {
                                Toast.makeText(requireActivity(), res.message, Toast.LENGTH_LONG)
                                    .show()
                            }
                            Status.LOADING -> {

                            }
                        }
                    }
                })
            }
        }
    }


}