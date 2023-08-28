package com.kl3jvi.animity.ui.fragments

import androidx.lifecycle.ViewModel
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    localStorage: PersistenceRepository,
) : ViewModel() {
}
