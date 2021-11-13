package com.kl3jvi.animity.view.fragments.downloads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.offline.DownloadCursor
import com.google.android.exoplayer2.offline.DownloadManager
import com.kl3jvi.animity.databinding.FragmentDownloadsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DownloadsFragment : Fragment() {
    private var _binding: FragmentDownloadsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var downloadManager: DownloadManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDownloadsBinding.inflate(inflater, container, false)
        getDownloadedItems().forEach {
//            binding.numd.append(it)
        }
        return binding.root
    }

    private fun getDownloadedItems(): ArrayList<String> {
        /*Asumming this is your Media item data class
        data class Media(var url: String, var artist:
        String, var title: String)*/
        val downloadedTracks = ArrayList<String>()
        val downloadCursor: DownloadCursor =
            downloadManager.downloadIndex.getDownloads()
        if (downloadCursor.moveToFirst()) {
            do {
                downloadCursor.download.bytesDownloaded
                val uri = downloadCursor.download.request.uri
                downloadedTracks.add(uri.toString())
            } while (downloadCursor.moveToNext())
        }
        return downloadedTracks
    }
}
