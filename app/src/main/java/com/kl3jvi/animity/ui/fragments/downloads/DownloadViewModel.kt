package com.kl3jvi.animity.ui.fragments.downloads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import com.kl3jvi.animity.utils.mapToUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    persistenceRepository: PersistenceRepository,
) : ViewModel() {
    val listOfAnimes =
        persistenceRepository.getAllAnimesThatHasDownloadedEpisodes()
            .mapToUiState(viewModelScope)
}
