package com.example.androidschool.moviePaging.ui.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.databinding.FragmentStartBinding
import com.example.androidschool.moviePaging.model.Movie
import com.example.androidschool.moviePaging.network.MovieSearchResponse
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel by viewModels<StartFragmentViewModel>()
    lateinit var mAdapter: StartFragmentPopularsAdapter
    lateinit var mPopularsObserver: Observer<List<Movie>>
    lateinit var mIsDataLoadedObserver: Observer<Boolean>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStartBinding.inflate(inflater, container,false)
        initialization()

        val movie = Movie(
        adult = false,
        backdropPath = "some",
        genreIds = listOf(0, 1, 3),
        id = 0,
        originalLanguage = "lan",
        originalTitle = "tit",
        overview = "rew",
        popularity = 2.5,
        posterPath = "path",
        releaseDate = "date",
        title = "tit",
        video = false,
        voteAverage = 2.0,
        voteCount = 2.0
        )

        val movieResponse: MovieSearchResponse = mViewModel.movieResponse.value ?: MovieSearchResponse(
            page = 0,
            results = listOf(movie),
            totalPages = 0,
            totalResults = 0
        )


        lifecycleScope.launch(Dispatchers.IO) {
//            dao.insertMovieSearchResponse()
        }

        return mBinding.root
    }

    private fun initialization() {
        mAdapter = StartFragmentPopularsAdapter(requireContext())
        mBinding.startFragmentPopularRecycler.adapter = mAdapter

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mBinding.startFragmentPopularRecycler.layoutManager = layoutManager

////        SNAPHELPER
//        val snapHelper = LinearSnapHelper()
//        snapHelper.attachToRecyclerView(mBinding.startFragmentPopularRecycler)

        mPopularsObserver = Observer {
            mAdapter.setList(it)
        }

        mViewModel.recyclerListData.observe(viewLifecycleOwner, mPopularsObserver)

        val moreOnclickListener = View.OnClickListener {
            Navigation.findNavController(it).navigate(R.id.popularMoviesFragment)
        }
        mBinding.sectionPopularMore.setOnClickListener(moreOnclickListener)
        initRefreshing()
    }

    private fun initRefreshing() {
        mIsDataLoadedObserver = Observer { isDataLoaded ->
            when (isDataLoaded) {
                false -> mBinding.refreshStartFragment.isRefreshing = true
                true -> mBinding.refreshStartFragment.isRefreshing = false
            }
        }
        mBinding.refreshStartFragment.setOnRefreshListener {
            refreshFragment()
        }
        mViewModel.isDataLoaded.observe(viewLifecycleOwner, mIsDataLoadedObserver)
    }

    private fun refreshFragment() {
        Snackbar.make(mBinding.refreshStartFragment, R.string.fragment_updated, Snackbar.LENGTH_SHORT).show()
        lifecycleScope.launch(Dispatchers.IO) {
            mViewModel.getPopularMovies()
        }
    }
}