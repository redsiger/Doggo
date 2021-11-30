package com.example.androidschool.moviePaging.ui.start

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.androidschool.moviePaging.MainActivity
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.databinding.FragmentStartBinding
import com.example.androidschool.moviePaging.model.Movie
import com.example.androidschool.moviePaging.util.MovieAdapter
import com.example.androidschool.moviePaging.util.Result.Status
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartFragment : Fragment(R.layout.fragment_start) {

    private var _binding: FragmentStartBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel by viewModels<StartFragmentViewModel>()

    lateinit var mTrendingAdapter: StartFragmentTrendingAdapter
    lateinit var mPopularsAdapter: StartFragmentPopularsAdapter
    lateinit var mTopRatedAdapter: StartFragmentPopularsAdapter

    lateinit var mTrendingStateObserver: Observer<Status<List<Movie>>>
    lateinit var mPopularStateObserver: Observer<Status<List<Movie>>>
    lateinit var mTopRatedStateObserver: Observer<Status<List<Movie>>>

    lateinit var mNavController: NavController
    lateinit var mToolbar: Toolbar

    @Inject lateinit var mPicasso: Picasso

    // Search
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(searchQuery: String?): Boolean {
                val bundle = Bundle()
                bundle.putString("searchQuery", searchQuery)
                findNavController().navigate(R.id.searchFragment, bundle)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.app_bar_search -> {
//
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStartBinding.bind(view)

        initToolbar()

        initialization()
    }

    private fun initToolbar() {
        mToolbar = mBinding.mainToolbar
        mToolbar.setTitleTextAppearance(requireContext(), R.style.style_toolbar_title)
        mToolbar.inflateMenu(R.menu.search_menu)
        val searchItem = mToolbar.menu.findItem(R.id.app_bar_search)
        val searchView = searchItem.actionView as SearchView
        initSearchInToolbar(searchView)
        initNavigationInToolbar()
    }

    private fun initNavigationInToolbar() {
        mNavController = findNavController()
        NavigationUI.setupWithNavController(mToolbar, mNavController)
    }

    private fun initSearchInToolbar(searchView: SearchView) {
        with (searchView) {
            queryHint = getString(R.string.search_hint)
            setBackgroundResource(R.color.transparent)
            setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(searchQuery: String?): Boolean {
                    val bundle = Bundle()
                    bundle.putString("searchQuery", searchQuery)
                    findNavController().navigate(R.id.searchFragment, bundle)
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return true
                }
            })
        }
    }

    private fun initialization() {
        mBinding.startFragmentContentContainer.isFillViewport = true
        initAdapters()
        initObservers()
        initRefreshing()
        initSectionsMore()
    }

    private fun initSectionsMore() {
        val popularMoreOnclickListener = View.OnClickListener {
            Navigation.findNavController(it).navigate(R.id.popularMoviesFragment)
        }
        mBinding.sectionPopular.setOnClickListener(popularMoreOnclickListener)
        mBinding.startFragmentPopularRecycler.isNestedScrollingEnabled = false

        val topRatedMoreOnclickListener = View.OnClickListener {
            Navigation.findNavController(it).navigate(R.id.topRatedFragment)
        }
        mBinding.sectionTopRated.setOnClickListener(topRatedMoreOnclickListener)
        mBinding.startFragmentTopRatedRecycler.isNestedScrollingEnabled = false
    }

    private fun initAdapters() {
        mTrendingAdapter = StartFragmentTrendingAdapter(mPicasso)
        mBinding.startFragmentTrendingRecycler.adapter = mTrendingAdapter
        val trendingLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mBinding.startFragmentTrendingRecycler.layoutManager = trendingLayoutManager
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(mBinding.startFragmentTrendingRecycler)

        mPopularsAdapter = StartFragmentPopularsAdapter(mPicasso)
        mBinding.startFragmentPopularRecycler.adapter = mPopularsAdapter
        val popularLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mBinding.startFragmentPopularRecycler.layoutManager = popularLayoutManager

        mTopRatedAdapter = StartFragmentPopularsAdapter(mPicasso)
        mBinding.startFragmentTopRatedRecycler.adapter = mTopRatedAdapter
        val topRatedLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mBinding.startFragmentTopRatedRecycler.layoutManager = topRatedLayoutManager
    }


    private fun initObservers() {
        mTrendingStateObserver = Observer { result ->
            when (result) {
                is Status.Success -> {
                    renderList(result.data, mTrendingAdapter)
                    hideLoading()
                    showData()
                }
                is Status.InProgress -> {
                    showLoading()
                    hideData()
                }
                is Status.Error -> {
                    val error = result.exception.toString()
                    Snackbar.make(mBinding.startFragmentRefreshContainer, error, Snackbar.LENGTH_SHORT).show()
                    hideLoading()
                    hideData()
                }
            }
        }
        mViewModel.trendingState.observe(viewLifecycleOwner, mTrendingStateObserver)

        mPopularStateObserver = Observer { result ->
            when (result) {
                is Status.Success -> {
                    renderList(result.data, mPopularsAdapter)
                    hideLoading()
                    showData()
                }
                is Status.InProgress -> {
                    showLoading()
                    hideData()
                }
                is Status.Error -> {
                    val error = result.exception.toString()
                    Snackbar.make(mBinding.startFragmentRefreshContainer, error, Snackbar.LENGTH_SHORT).show()
                    hideLoading()
                    hideData()
                }
            }
        }
        mViewModel.popularState.observe(viewLifecycleOwner, mPopularStateObserver)

        mTopRatedStateObserver = Observer { result ->
            when (result) {
                is Status.Success -> {
                    renderList(result.data, mTopRatedAdapter)
                    hideLoading()
                    showData()
                }
                is Status.InProgress -> {
                    showLoading()
                    hideData()
                }
                is Status.Error -> {
                    val error = result.exception.toString()
                    Snackbar.make(mBinding.startFragmentRefreshContainer, error, Snackbar.LENGTH_SHORT).show()
                    hideLoading()
                    hideData()
                }
            }
        }

        mViewModel.topRatedState.observe(viewLifecycleOwner, mTopRatedStateObserver)
    }

    private fun showTrending() {
        mBinding.sectionTrendingContainer.visibility = View.VISIBLE
    }
    private fun showPopular() {
        mBinding.sectionPopularContainer.visibility = View.VISIBLE
    }
    private fun showTopRated() {
        mBinding.sectionTopRatedContainer.visibility = View.VISIBLE
    }
    private fun hideTrending() {
        mBinding.sectionTrendingContainer.visibility = View.GONE
    }
    private fun hidePopular() {
        mBinding.sectionPopularContainer.visibility = View.GONE
    }
    private fun hideTopRated() {
        mBinding.sectionTopRatedContainer.visibility = View.GONE
    }

    private fun showData() {
        mBinding.startFragmentContentContainer.visibility = View.VISIBLE
    }
    private fun showLoading() {
        mBinding.startFragmentRefreshContainer.isRefreshing = true
//        mBinding.startFragmentShimmerContainer.visibility = View.VISIBLE
//        mBinding.startFragmentShimmerContainer.startShimmer()
    }

    private fun hideData() {
        mBinding.startFragmentContentContainer.visibility = View.GONE
    }

    private fun hideLoading() {
        mBinding.startFragmentRefreshContainer.isRefreshing = false
//        mBinding.startFragmentShimmerContainer.stopShimmer()
//        mBinding.startFragmentShimmerContainer.visibility = View.GONE
    }



    private fun renderList(movies: List<Movie>, adapter: MovieAdapter) {
        adapter.setList(movies)
    }

    private fun initRefreshing() {
        mBinding.startFragmentRefreshContainer.setOnRefreshListener {
            refreshFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewModel.trendingState.removeObserver(mTrendingStateObserver)
        mViewModel.topRatedState.removeObserver(mTopRatedStateObserver)
        mViewModel.popularState.removeObserver(mPopularStateObserver)
    }

    private fun refreshFragment() {
        mViewModel.loadState()
        Snackbar.make(mBinding.startFragmentRefreshContainer, R.string.fragment_updated, Snackbar.LENGTH_SHORT).show()
    }
}