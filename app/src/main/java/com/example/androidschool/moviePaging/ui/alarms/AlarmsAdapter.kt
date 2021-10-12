package com.example.androidschool.moviePaging.ui.alarms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.data.room.Alarm
import com.example.androidschool.moviePaging.databinding.FragmentAlarmsItemBinding
import java.text.SimpleDateFormat
import java.util.*

class AlarmsAdapter: RecyclerView.Adapter<AlarmsAdapter.AlarmHolder>() {

    private var alarmsList: List<Alarm> = emptyList()

    fun setList(list: List<Alarm>) {
        alarmsList = list
        notifyDataSetChanged()
    }

    class AlarmHolder(view: View):RecyclerView.ViewHolder(view) {
        val mBinding = FragmentAlarmsItemBinding.bind(view)
        val dateFormatter = SimpleDateFormat("HH:mm dd-MMMM-yyyy")

        fun bind(alarm: Alarm) {
            with(mBinding) {
                alarmMovieTitle.text = alarm.movieTitle
                alarmTime.text = dateFormatter.format(Date(alarm.time))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_alarms_item, parent, false)
        return AlarmHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmHolder, position: Int) {
        val alarm: Alarm = alarmsList[position]
        holder.bind(alarm)
    }

    override fun getItemCount(): Int {
        return alarmsList.size
    }
}