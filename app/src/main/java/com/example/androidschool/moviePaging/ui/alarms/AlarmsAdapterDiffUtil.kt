package com.example.androidschool.moviePaging.ui.alarms

import androidx.recyclerview.widget.DiffUtil
import com.example.androidschool.moviePaging.data.room.alarms.Alarm

class AlarmsAdapterDiffUtil(
    private val oldList: List<Alarm>,
    private val newList: List<Alarm>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        return if (newItemPosition >= oldList.size) {
//            false
//        } else {
//            val oldItem = oldList[oldItemPosition]
//            val newItem = oldList[newItemPosition]
//            return oldItem.movieId == newItem.movieId
//        }
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.movieId == newItem.movieId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        return if (newItemPosition >= oldList.size) {
//            false
//        } else {
//            val oldItem = oldList[oldItemPosition]
//            val newItem = oldList[newItemPosition]
//            return oldItem.time == newItem.time
//        }
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.time == newItem.time
    }

}