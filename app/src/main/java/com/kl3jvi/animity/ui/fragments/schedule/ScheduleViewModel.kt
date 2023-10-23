package com.kl3jvi.animity.ui.fragments.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.data.enums.WeekName
import com.kl3jvi.animity.data.mapper.AiringInfo
import com.kl3jvi.animity.domain.repositories.NotificationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel
    @Inject
    constructor(
        private val notificationsRepository: NotificationsRepository,
    ) : ViewModel() {
        private val _airingAnimeSchedule = MutableStateFlow<Map<WeekName, List<AiringInfo>>>(emptyMap())
        val airingAnimeSchedule: StateFlow<Map<WeekName, List<AiringInfo>>> = _airingAnimeSchedule

        init {
            viewModelScope.launch(Dispatchers.IO) {
                fetchAiringAnimeSchedule()
            }
        }

        operator fun invoke() {}

        private suspend fun fetchAiringAnimeSchedule() {
            val results = mutableMapOf<WeekName, List<AiringInfo>>()
            // Using coroutine's coroutineScope to ensure all children coroutines complete before proceeding
            coroutineScope {
                WeekName.entries.map { day ->
                    async(Dispatchers.IO) {
                        day to (
                            notificationsRepository.getScheduled(day)
                                .firstOrNull() ?: emptyList()
                        )
                        // Assuming you want the first emission from the Flow
                    }
                }.forEach { deferred ->
                    val pair = deferred.await()
                    results[pair.first] = pair.second
                }
            }
            _airingAnimeSchedule.compareAndSet(_airingAnimeSchedule.value, results)
        }
    }
