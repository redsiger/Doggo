package com.example.androidschool.moviePaging.ui.popularMovies

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.databinding.FragmentPopularMoviesBinding
import com.example.androidschool.moviePaging.ui.MovieSearchResponseAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PopularMoviesFragment : Fragment() {

    private var _binding: FragmentPopularMoviesBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: PopularMoviesViewModel by viewModels()
    lateinit var mAdapter: MovieSearchResponseAdapter
    lateinit var mRecyclerView: RecyclerView
    lateinit var mIsMoviesLoadedObserver: Observer<Boolean>
    lateinit var thisView: Fragment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPopularMoviesBinding.inflate(inflater, container,false)
//        mViewModel = ViewModelProvider(this).get(PopularMoviesViewModel::class.java)

        initialize()
        initPopularMoviesList()

        thisView = this

        Log.e("Tag", "onCreateView")
        return (mBinding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("Tag", "onCreate")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val menuItem = menu.findItem(R.menu.main_menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_search -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initialize() {
        mRecyclerView = mBinding.recyclerMovieList
        mAdapter = MovieSearchResponseAdapter()

        setHasOptionsMenu(true)

        mRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
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
                mAdapter.submitData(lifecycle, it)
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