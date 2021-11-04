package com.example.androidschool.moviePaging.ui.alarms

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.data.room.alarms.Alarm
import com.example.androidschool.moviePaging.databinding.FragmentAlarmsItemBinding
import com.example.androidschool.moviePaging.notifications.AlarmReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AlarmsAdapter @Inject constructor(@ApplicationContext private val context: Context): RecyclerView.Adapter<AlarmsAdapter.AlarmHolder>() {

    private var alarmsList: MutableList<Alarm> = mutableListOf()
    lateinit var alarmsListener: AlarmsListener

    interface AlarmsListener {
        fun btnDeleteClicked(movieId: Int, movieTitle: String, time: Long)
        fun showAlarmDialogFragment(
            movieTitle: String,
            movieId: Int,
            time: Long,
            position: Int
        )
    }

    fun setList(list: List<Alarm>) {
        var list1 = list.toMutableList()
        val diffUtilCallBack = AlarmsAdapterDiffUtil(alarmsList, list1)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)

        alarmsList = list1
        diffResult.dispatchUpdatesTo(this)
    }

    class AlarmHolder(val context: Context,
                      view: View)
        : RecyclerView.ViewHolder(view) {
        val mBinding = FragmentAlarmsItemBinding.bind(view)
        val dateFormatter = SimpleDateFormat("dd-MMMM-yyyy HH:mm")

        fun bind(alarm: Alarm) {
            with(mBinding) {
                alarmMovieTitle.text = alarm.movieTitle
                alarmTime.text = dateFormatter.format(Date(alarm.time))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_alarms_item, parent, false)
        return AlarmHolder(context, view)
    }

    override fun onBindViewHolder(holder: AlarmHolder, position: Int) {
        val alarm: Alarm = alarmsList[position]
        holder.bind(alarm)

        holder.mBinding.root.setOnClickListener {
            alarmsListener.showAlarmDialogFragment(alarm.movieTitle, alarm.movieId, alarm.time, position)
        }

        holder.mBinding.alarmBtnDelete.setOnClickListener {
            removeItem(holder.bindingAdapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return alarmsList.size
    }

    private fun removeItem(position: Int) {
        val movie =  alarmsList[position]
        alarmsListener.btnDeleteClicked(
            movie.movieId,
            movie.movieTitle,
            movie.time
        )
    }

    fun updateItem(position: Int, time: Long) {
        val updatedAlarm = alarmsList[position].copy(time = time)
        alarmsList.set(position, updatedAlarm)
        notifyItemChanged(position)
    }
}