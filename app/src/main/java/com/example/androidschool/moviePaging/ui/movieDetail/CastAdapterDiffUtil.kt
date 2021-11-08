package com.example.androidschool.moviePaging.ui.start

import androidx.recyclerview.widget.DiffUtil
import com.example.androidschool.moviePaging.model.Movie
import com.example.androidschool.moviePaging.network.Credits.CastMember

class CastAdapterDiffUtil(
    private val oldList: List<CastMember>,
    private val newList: List<CastMember>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = oldList[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = oldList[newItemPosition]
        return oldItem.originalName == newItem.originalName
    }
}