package com.kl3jvi.animity.view.activities.player

import android.media.AudioManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.Player
import com.kl3jvi.animity.databinding.FragmentPlayerBinding

class PlayerFragment : Fragment(), View.OnClickListener, Player.EventListener,
    AudioManager.OnAudioFocusChangeListener  {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    override fun onAudioFocusChange(p0: Int) {
        TODO("Not yet implemented")
    }
}