package com.kl3jvi.animity.ui.fragments.details.reviewDetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.ReviewDetailsFragmentBinding
import com.kl3jvi.animity.utils.setMarkdownText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ReviewDetailsFragment : Fragment(R.layout.review_details_fragment) {
    private val args: ReviewDetailsFragmentArgs by navArgs()
    private val reviewInfo get() = args.reviewDetails
    private var binding: ReviewDetailsFragmentBinding? = null

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = ReviewDetailsFragmentBinding.bind(view)
        initViews()
    }

    private fun initViews() {
        binding?.imageView?.load(reviewInfo.aniListMedia.coverImage.large)
        binding?.postContent?.postAuthor?.text = reviewInfo.user.name
        binding?.postContent?.postBody?.setMarkdownText(reviewInfo.body)
    }
}
