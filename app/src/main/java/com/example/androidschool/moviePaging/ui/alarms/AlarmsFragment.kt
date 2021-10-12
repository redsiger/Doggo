package com.example.androidschool.moviePaging.ui.alarms

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.data.room.Alarm
import com.example.androidschool.moviePaging.databinding.FragmentAlarmsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmsFragment : Fragment() {

    private var _binding: FragmentAlarmsBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: AlarmsViewModel by viewModels<AlarmsViewModel>()
    var mAlarmsAdapter = AlarmsAdapter()
    lateinit var mAlarmsObserver: Observer<List<Alarm>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAlarmsBinding.inflate(inflater, container, false)

        initAlarmsRecycler()
        return mBinding.root
    }

    private fun initAlarmsRecycler() {
        mBinding.alarmsRecycler.adapter = mAlarmsAdapter

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mBinding.alarmsRecycler.layoutManager = layoutManager

        mAlarmsObserver = Observer {
            mAlarmsAdapter.setList(it)
        }

        mViewModel.alarms.observe(viewLifecycleOwner, mAlarmsObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewModel.alarms.removeObserver(mAlarmsObserver)
    }
}