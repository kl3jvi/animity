package com.kl3jvi.animity.ui.fragments.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.data.network.VersionInfo
import com.kl3jvi.animity.domain.repositories.DetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    detailsRepository: DetailsRepository
) : ViewModel() {

    val versionInfo = detailsRepository.getUpdateVersionInfo()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            VersionInfo(null)
        )
}
