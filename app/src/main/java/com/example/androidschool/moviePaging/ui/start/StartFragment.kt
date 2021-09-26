package com.example.androidschool.moviePaging.ui.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.databinding.FragmentStartBinding
import com.example.androidschool.moviePaging.model.Movie


class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: StartFragmentViewModel by viewModels()
    lateinit var mAdapter: StartFragmentPopularsAdapter
    lateinit var mPopularsObserver: Observer<List<Movie>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStartBinding.inflate(inflater, container,false)

        initialization()

        return mBinding.root
    }

    private fun initialization() {
        mAdapter = StartFragmentPopularsAdapter(requireContext())
        mBinding.startFragmentPopularRecycler.adapter = mAdapter

//        val layoutManager = LinearLayoutManager(requireContext())
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mBinding.startFragmentPopularRecycler.layoutManager = layoutManager

        mPopularsObserver = Observer {
            mAdapter.setList(it)
        }

        mViewModel.recyclerListData.observe(viewLifecycleOwner, mPopularsObserver)

        val moreOnclickListener = View.OnClickListener {
            Navigation.findNavController(it).navigate(R.id.popularMoviesFragment)
        }
        mBinding.sectionPopularMore.setOnClickListener(moreOnclickListener)
    }
}