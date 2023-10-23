package com.kl3jvi.animity.utils.epoxy

import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyRecyclerView
import com.kl3jvi.animity.utils.BottomNavScrollListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun EpoxyRecyclerView.setupBottomNavScrollListener(
    bottomNavListener: BottomNavScrollListener,
    delayMillis: Long = 60L,
) = apply {
    var debounceJob: Job? = null
    var lastDy: Int

    this.addOnScrollListener(
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int,
            ) {
                super.onScrolled(recyclerView, dx, dy)
                debounceJob?.cancel()
                lastDy = dy
                debounceJob =
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(delayMillis)
                        if (lastDy > 0) {
                            bottomNavListener.onScrollDown()
                        } else if (lastDy < 0) {
                            bottomNavListener.onScrollUp()
                        }
                    }
            }
        },
    )
}
