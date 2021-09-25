package com.kl3jvi.animity.view.activities.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kl3jvi.animity.databinding.FragmentPlayerBinding

class PlayerFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.andExoPlayerView.apply {
            setSource("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
            seekBackward(10000)
            seekForward(10000)
        }
    }


    override fun onClick(p0: View?) {

    }



    override fun onDestroy() {
        super.onDestroy()
        binding.andExoPlayerView.stopPlayer()
    }

}