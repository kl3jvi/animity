package com.kl3jvi.animity.ui.fragments.downloads.episodes

import androidx.lifecycle.ViewModel
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest

@HiltViewModel
class DownloadedEpisodesViewModel @Inject constructor(
    private val persistenceRepository: PersistenceRepository
) : ViewModel() {
    private val animeId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val downloadedEpisodes = animeId.flatMapLatest {
        if (it != -1)
            persistenceRepository.getDownloadedEpisodesForAnime(it)
        else emptyFlow()
    }

    fun setAnimeId(id: Int) {
        animeId.value = id
    }
}
