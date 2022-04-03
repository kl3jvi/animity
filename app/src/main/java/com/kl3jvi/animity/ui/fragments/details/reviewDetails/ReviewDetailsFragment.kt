package com.kl3jvi.animity.ui.fragments.details.reviewDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import coil.load
import com.kl3jvi.animity.databinding.ReviewDetailsFragmentBinding
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.base.BaseFragment
import com.kl3jvi.animity.utils.setHtmlText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewDetailsFragment : BaseFragment<ReviewDetailsViewModel, ReviewDetailsFragmentBinding>() {

    private val args: ReviewDetailsFragmentArgs by navArgs()
    private val reviewInfo get() = args.reviewDetails

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun observeViewModel() {}

    override fun initViews() {
        binding.imageView.load(reviewInfo.aniListMedia.coverImage.large)
        binding.postContent.postAuthor.text = reviewInfo.user.name
        binding.postContent.postBody.setHtmlText(reviewInfo.body)
    }

    override fun getViewBinding(): ReviewDetailsFragmentBinding =
        ReviewDetailsFragmentBinding.inflate(layoutInflater)

    override val viewModel: ReviewDetailsViewModel
        get() = ReviewDetailsViewModel()

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.hideBottomNavBar()
        }
    }

}