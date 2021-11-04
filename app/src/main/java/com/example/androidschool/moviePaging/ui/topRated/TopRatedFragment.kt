package com.example.androidschool.moviePaging.ui.topRated

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.moviePaging.databinding.FragmentTopRatedBinding
import com.example.androidschool.moviePaging.model.Movie
import com.example.androidschool.moviePaging.ui.start.StartFragmentPopularsAdapter
import com.example.androidschool.moviePaging.util.MovieAdapter
import com.example.androidschool.moviePaging.util.Result.Status
import com.example.androidschool.moviePaging.util.ScollListener.InfiniteScrollListener
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TopRatedFragment: Fragment() {

    private var _binding: FragmentTopRatedBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: TopRatedViewModel by viewModels()
    lateinit var mTopRatedAdapter: StartFragmentPopularsAdapter
    lateinit var mTopRatedObserver: Observer<Status<List<Movie>?>>
    lateinit var mGenresChipsAdapter: StartFragmentPopularsAdapter
    lateinit var mRecyclerView: RecyclerView
    @Inject lateinit var mPicasso: Picasso

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTopRatedBinding.inflate(inflater, container, false)

        initialization()
        return (mBinding.root)
    }

    private fun initialization() {
//        hideLoadingBtn()
        initTopRatedRecycler()
        initObservers()
    }

    private fun initTopRatedRecycler() {
        initTopRatedAdapter()
    }

    private fun fetchMore() {
        mViewModel.loadNextPage()
    }

    private fun initTopRatedAdapter() {
        val topRatedLayoutManager = GridLayoutManager(requireContext(), 3)
//        val topRatedLayoutManager = LinearLayoutManager(requireContext())
        mBinding.fragmentTopRatedMoviesRecycler.apply {
            isNestedScrollingEnabled = false
            mTopRatedAdapter = StartFragmentPopularsAdapter(mPicasso)
            adapter = mTopRatedAdapter
            layoutManager = topRatedLayoutManager
        }
        val scroll = mBinding
            .fragmentTopRatedScrollContainer
        scroll
            .viewTreeObserver.addOnScrollChangedListener {
                val view = scroll.getChildAt(scroll.childCount - 1)
                Log.e("Scroll", scroll.childCount.toString())

                val diff = view.bottom - (scroll.height + scroll.scrollY)
                Log.e("Scroll", diff.toString())

                if (diff == 0) {
                    //your api call to fetch data
                    Log.e("Scroll", "It Works!")
                    fetchMore()
                }
            }
    }

    private fun initObservers() {
        mTopRatedObserver = Observer { result ->
            when (result) {
                is Status.Success -> {
                    hideLoading()
                    renderList(result.data, mTopRatedAdapter)
                }
                is Status.InProgress -> {
                    showLoading()
                }
                is Status.Error -> {
                    val error = result.exception.toString()
                    Snackbar.make(mBinding.root, error, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        mViewModel.topRatedState.observe(viewLifecycleOwner, mTopRatedObserver)
//        mBinding.fragmentTopRatedBtnLoadMore.setOnClickListener {
//            mViewModel.loadNextPage()
//        }
    }

    private fun hideLoading() {
        mBinding.fragmentTopRatedLoading.visibility = View.GONE
    }

    private fun showLoading() {
        mBinding.fragmentTopRatedLoading.visibility = View.VISIBLE
    }

//    private fun hideLoadingBtn() {
//        mBinding.fragmentTopRatedBtnLoadMore.visibility = View.GONE
//    }
//
//    private fun showLoadingBtn() {
//        mBinding.fragmentTopRatedBtnLoadMore.visibility = View.VISIBLE
//    }

    private fun renderList(movies: List<Movie>?, adapter: MovieAdapter) {
        adapter.setList(movies as List<Movie>)
    }

}