package com.example.androidschool.moviePaging.ui.popularMovies

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.moviePaging.MainActivity
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.databinding.FragmentPopularMoviesBinding
import com.example.androidschool.moviePaging.ui.search.MovieSearchResponsePagingAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PopularMoviesFragment : Fragment(R.layout.fragment_popular_movies) {

    private var _binding: FragmentPopularMoviesBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: PopularMoviesViewModel by viewModels()
    lateinit var mPagingAdapter: MovieSearchResponsePagingAdapter
    lateinit var mRecyclerView: RecyclerView
    lateinit var mIsMoviesLoadedObserver: Observer<Boolean>
    lateinit var thisView: Fragment
    @Inject lateinit var mPicasso: Picasso

    lateinit var mNavController: NavController
    lateinit var mToolbar: MaterialToolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPopularMoviesBinding.bind(view)
        mToolbar = mBinding.mainToolbar
        mToolbar.setTitleTextAppearance(requireContext(), R.style.style_toolbar_title)
        mNavController = findNavController()
        NavigationUI.setupWithNavController(mToolbar, mNavController)

        initialize()
        initPopularMoviesList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("Tag", "onCreate")
    }

    private fun initialize() {
        mRecyclerView = mBinding.recyclerMovieList
//        mAdapter = StartFragmentPopularsAdapter(requireContext(), mPicasso)
        mPagingAdapter = MovieSearchResponsePagingAdapter(mPicasso)

        mRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = mPagingAdapter
        }

        mBinding.startFragmentMoviesRefresh.setOnRefreshListener {
            makeToast()
        }

        mIsMoviesLoadedObserver = Observer { isLoaded ->
            when (isLoaded) {
                true -> mBinding.progressBarFragmentStart.visibility = View.GONE
                false -> mBinding.progressBarFragmentStart.visibility = View.VISIBLE
            }
        }
        mViewModel.isMoviesLoaded.observe(viewLifecycleOwner, mIsMoviesLoadedObserver)


    }

    private fun makeToast() {
        Snackbar.make(mBinding.startFragmentMoviesRefresh, "Update", Snackbar.LENGTH_SHORT).show()
        mBinding.startFragmentMoviesRefresh.isRefreshing = false
    }

    private fun initPopularMoviesList() {
        viewLifecycleOwner.lifecycleScope.launch {
//            delay(2000)
            mViewModel.getPopularMovies().observe(viewLifecycleOwner, {
                mPagingAdapter.submitData(lifecycle, it)
            })
        }

//        viewLifecycleOwner.lifecycleScope.launch {
//            mViewModel.popularMovies().collectLatest {
//                mAdapter.submitData(it)
//            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewModel.isMoviesLoaded.removeObserver(mIsMoviesLoadedObserver)
        Log.e("Tag", "onDestroyView")
    }

    override fun onPause() {
        super.onPause()
        Log.e("Tag", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e("Tag", "onStop")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("Tag", "onDetach")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("Tag", "onDestroy")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("Tag", "onAttach")
    }
}