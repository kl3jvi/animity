package com.kl3jvi.animity.ui.activities.main

import androidx.lifecycle.ViewModel
import com.kl3jvi.animity.data.repository.persistence_repository.LocalStorageImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: LocalStorageImpl
) : ViewModel() {

}