package com.example.androidschool.moviePaging.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.databinding.FragmentSearchBinding
import com.example.androidschool.moviePaging.ui.popularMovies.PopularMoviesViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment: Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: PopularMoviesViewModel by viewModels()
    lateinit var mPagingAdapter: MovieSearchResponsePagingAdapter
    lateinit var mRecyclerView: RecyclerView

    lateinit var mNavController: NavController
    lateinit var mToolbar: Toolbar

    @Inject
    lateinit var mPicasso: Picasso

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.e("logging", "Fragment saveInstanceState")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("logging", "Fragment attached")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("logging", "Fragment detached")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("logging", "Fragment created")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("logging", "Fragment destroyed")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container,false)

        mToolbar = mBinding.toolbarSearchFragment
        mToolbar.setTitleTextAppearance(requireContext(), R.style.style_toolbar_title)
        mNavController = findNavController()
        NavigationUI.setupWithNavController(mToolbar, mNavController)

        val searchView = mBinding.fragmentSearchSearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(searchQuery: String?): Boolean {
//                mViewModel.searchResult()
                val bundle = Bundle()
                bundle.putString("searchQuery", searchQuery)
                findNavController().navigate(R.id.searchFragment2, bundle)
                Log.e("search", "Enter clicked")
                return true
            }

            override fun onQueryTextChange(searchQuery: String?): Boolean {
                mViewModel.setQuery(searchQuery!!)
                return true
            }
        })

        val searchQueryObserver: Observer<String> = Observer {
            mViewModel.searchResult()
        }

        initialize()

        Log.e("logging", "Fragment view created")
        return (mBinding.root)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("logging", "Fragment view destroyed")
    }

    override fun onStart() {
        super.onStart()
        Log.e("logging", "Fragment started")
    }

    override fun onStop() {
        super.onStop()
        Log.e("logging", "Fragment stopped")
    }

    override fun onResume() {
        super.onResume()
        Log.e("logging", "Fragment resumed")
    }

    override fun onPause() {
        super.onPause()
        Log.e("logging", "Fragment paused")
    }

    private fun searchOnclick() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.searchResult().observe(viewLifecycleOwner, {
                mPagingAdapter.submitData(lifecycle, it)
            })
        }
    }

    private fun initSearchResultsList() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.searchResult().observe(viewLifecycleOwner, {
                mPagingAdapter.submitData(lifecycle, it)
            })
        }
    }

    private fun initialize() {
        mRecyclerView = mBinding.searchResultMovies
        mPagingAdapter = MovieSearchResponsePagingAdapter(mPicasso)

        mRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(),3)
            adapter = mPagingAdapter
        }

        mBinding.testFragmentMoviesRefresh.setOnRefreshListener {
            mBinding.testFragmentMoviesRefresh.isRefreshing = false
        }

//        mBinding.testFragmentSearchButton.setOnClickListener {
//            if (mBinding.testFragmentSearchInput.text.isNotEmpty()) {
//                val query: String = mBinding.testFragmentSearchInput.text.toString()
//                mViewModel.searchQuery = query
//                searchOnclick()
//            }
//        }

        initSearchResultsList()
    }
}