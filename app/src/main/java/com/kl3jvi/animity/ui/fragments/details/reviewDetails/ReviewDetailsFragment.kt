package com.kl3jvi.animity.ui.fragments.details.reviewDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import coil.load
import com.kl3jvi.animity.databinding.ReviewDetailsFragmentBinding
import com.kl3jvi.animity.ui.base.BaseFragment
import com.kl3jvi.animity.utils.setMarkdownText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ReviewDetailsFragment : BaseFragment<ReviewDetailsViewModel, ReviewDetailsFragmentBinding>() {
    private val args: ReviewDetailsFragmentArgs by navArgs()
    private val reviewInfo get() = args.reviewDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.actionBar?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun observeViewModel() {}

    override fun initViews() {
        binding.imageView.load(reviewInfo.aniListMedia.coverImage.large)
        binding.postContent.postAuthor.text = reviewInfo.user.name
        binding.postContent.postBody.setMarkdownText(reviewInfo.body)
    }

    override fun getViewBinding(): ReviewDetailsFragmentBinding =
        ReviewDetailsFragmentBinding.inflate(layoutInflater)

    override val viewModel: ReviewDetailsViewModel
        get() = ReviewDetailsViewModel()

    override fun onResume() {
        super.onResume()

    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
}
