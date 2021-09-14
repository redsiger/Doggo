package com.example.androidschool.moviePaging.ui.popularMovies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.moviePaging.databinding.FragmentPopularMoviesBinding
import com.example.androidschool.moviePaging.ui.MovieSearchResponseAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PopularMoviesFragment : Fragment() {

    private var _binding: FragmentPopularMoviesBinding? = null
    private val mBinding get() = _binding!!
    lateinit var mViewModel: PopularMoviesViewModel
    lateinit var mAdapter: MovieSearchResponseAdapter
    lateinit var mRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPopularMoviesBinding.inflate(inflater, container,false)
        mViewModel = ViewModelProvider(this).get(PopularMoviesViewModel::class.java)


        initialize()

        return (mBinding.root)
    }


    private fun initialize() {
        mRecyclerView = mBinding.recyclerMovieList
        mAdapter = MovieSearchResponseAdapter()

        mRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }

        initPopularMoviesList()

        mBinding.startFragmentMoviesRefresh.setOnRefreshListener {
            makeToast()
        }
//        viewLifecycleOwner.lifecycleScope.launch {
//            mViewModel.popularMovies().collectLatest {
//                mAdapter.submitData(it)
//            }
//        }
    }

    private fun makeToast() {
        Snackbar.make(mBinding.startFragmentMoviesRefresh, "Update", Snackbar.LENGTH_SHORT).show()
        mBinding.startFragmentMoviesRefresh.isRefreshing = false
    }

    private fun initPopularMoviesList() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.getPopularMovies().observe(viewLifecycleOwner, {
                mAdapter.submitData(lifecycle, it)
            })
        }
    }


}