package com.example.androidschool.moviePaging.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.moviePaging.databinding.FragmentTestBinding
import com.example.androidschool.moviePaging.ui.popularMovies.PopularMoviesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TestFragment: Fragment() {

    private var _binding: FragmentTestBinding? = null
    private val mBinding get() = _binding!!
    lateinit var mViewModel: PopularMoviesViewModel
    lateinit var mAdapter: MovieSearchResponseAdapter
    lateinit var mRecyclerView: RecyclerView

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
        _binding = FragmentTestBinding.inflate(inflater, container,false)
        mViewModel = ViewModelProvider(this).get(PopularMoviesViewModel::class.java)
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

    private fun searchOnclick(query: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.searchResult(query).collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

    private fun initialize() {
        mRecyclerView = mBinding.searchResultMovies
        mAdapter = MovieSearchResponseAdapter()

        mRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }

        mBinding.testFragmentSearchButton.setOnClickListener {
            if (mBinding.testFragmentSearchInput.text.isNotEmpty()) {
                val query: String = mBinding.testFragmentSearchInput.text.toString()
                searchOnclick(query)
            }
        }
    }
}