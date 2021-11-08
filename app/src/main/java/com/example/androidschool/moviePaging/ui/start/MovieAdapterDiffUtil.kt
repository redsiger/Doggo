package com.example.androidschool.moviePaging.ui.start

import androidx.recyclerview.widget.DiffUtil
import com.example.androidschool.moviePaging.model.Movie

class MovieAdapterDiffUtil(
    private val oldList: List<Movie>,
    private val newList: List<Movie>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return if (newItemPosition >= oldList.size) {
            false
        } else {
            val oldItem = oldList[oldItemPosition]
            val newItem = oldList[newItemPosition]
            oldItem.id == newItem.id
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = oldList[newItemPosition]
        return oldItem.originalTitle == newItem.originalTitle
    }
}