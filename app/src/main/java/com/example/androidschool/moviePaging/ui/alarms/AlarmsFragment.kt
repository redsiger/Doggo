package com.example.androidschool.moviePaging.ui.alarms

import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidschool.moviePaging.MainActivity
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.data.room.alarms.Alarm
import com.example.androidschool.moviePaging.databinding.FragmentAlarmsBinding
import com.example.androidschool.moviePaging.notifications.AlarmReceiver
import com.example.androidschool.moviePaging.notifications.AppNotification
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmsFragment : Fragment(), AlarmsAdapter.AlarmsListener {

    private var _binding: FragmentAlarmsBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: AlarmsViewModel by viewModels<AlarmsViewModel>()
    @Inject
    lateinit var mAlarmsAdapter: AlarmsAdapter
    lateinit var mAlarmsObserver: Observer<List<Alarm>>
    @Inject
    lateinit var mAppNotification: AppNotification

    lateinit var mNavController: NavController
    lateinit var mBottomNavigationView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAlarmsBinding.inflate(inflater, container, false)

        setupAlarmDialogFragmentListener()
        initAlarmsRecycler()
        return mBinding.root
    }

    private fun initAlarmsRecycler() {
        mBinding.alarmsRecycler.adapter = mAlarmsAdapter

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mBinding.alarmsRecycler.layoutManager = layoutManager
        mAlarmsAdapter.alarmsListener = this

        mAlarmsObserver = Observer {
            mAlarmsAdapter.setList(it)
        }

        mViewModel.alarms.observe(viewLifecycleOwner, mAlarmsObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewModel.alarms.removeObserver(mAlarmsObserver)
    }

    override fun btnDeleteClicked(movieId: Int, movieTitle: String, time: Long) {
        lifecycleScope.launch(Dispatchers.Default) {
            mAppNotification.deleteNotificationAndAlarm(movieId, movieTitle)
            mViewModel.getAlarms()
        }
//        val activity: MainActivity = getActivity() as MainActivity
//        activity.showSnackbar(movieTitle)
        val reminderDelete: String = getString(R.string.reminder_is_delete, movieTitle)
        val snackbar = Snackbar.make(mBinding.root, reminderDelete, Snackbar.LENGTH_SHORT)
        snackbar.setAction(R.string.cancel, View.OnClickListener {
            lifecycleScope.launch(Dispatchers.Default) {
                mAppNotification.createNotificationAndAlarm(
                    movieId,
                    movieTitle,
                    time
                )
                mViewModel.getAlarms()
            }
        })
        snackbar.show()
    }

    override fun showAlarmDialogFragment(movieTitle: String,
                                         movieId: Int,
                                         time: Long,
                                         position: Int) {
        val bundle = Bundle()
        bundle.putString("movieTitle", movieTitle)
        bundle.putInt("movieId", movieId)
        bundle.putLong("time", time)
        bundle.putInt("position", position)

        val alarmDialog = AlarmDialogFragment()
        alarmDialog.arguments = bundle
        alarmDialog.show(parentFragmentManager, AlarmDialogFragment.TAG)
    }

    private fun setupAlarmDialogFragmentListener() {
        parentFragmentManager.setFragmentResultListener(AlarmDialogFragment.REQUEST_KEY, viewLifecycleOwner, FragmentResultListener { _, result ->
            val which = result.getInt(AlarmDialogFragment.KEY_RESPONSE)
            val time = result.getLong(AlarmDialogFragment.KEY_RESPONSE_TIME)
            val movieTitle = result.getString(AlarmDialogFragment.KEY_RESPONSE_MOVIE_TITLE)
            val movieId = result.getInt(AlarmDialogFragment.KEY_RESPONSE_MOVIE_ID)
            val position = result.getInt(AlarmDialogFragment.KEY_RESPONSE_MOVIE_POSITION)

            when (which) {
                DialogInterface.BUTTON_NEGATIVE -> showToast(DialogInterface.BUTTON_NEGATIVE.toString())
                DialogInterface.BUTTON_POSITIVE -> {
                    showToast(DialogInterface.BUTTON_POSITIVE.toString())
                    lifecycleScope.launch(Dispatchers.Default) {
                        mAppNotification.createNotificationAndAlarm(
                            movieId,
                            movieTitle!!,
                            time
                        )
                        mViewModel.getAlarms()
                    }

                }
            }
        })
    }

    fun showToast(string: String) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
    }
}