package com.kl3jvi.animity.ui.adapters.newAdapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class MainDiffUtil<T> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.toString() == newItem.toString()
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}