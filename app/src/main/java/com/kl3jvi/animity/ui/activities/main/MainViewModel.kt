package com.kl3jvi.animity.ui.activities.main

import androidx.lifecycle.ViewModel
import com.kl3jvi.animity.data.repository.DataStoreManagerImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: DataStoreManagerImpl
) : ViewModel() {
    fun getLoginType() = dataStore.getLoginTypeFromPreferencesStore()

}