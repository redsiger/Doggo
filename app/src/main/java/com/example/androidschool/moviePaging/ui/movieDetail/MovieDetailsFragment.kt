package com.example.androidschool.moviePaging.ui.movieDetail

import android.app.ActionBar
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.data.utils.TMDB_IMG_URL
import com.example.androidschool.moviePaging.databinding.FragmentMovieDetailBinding
import com.example.androidschool.moviePaging.databinding.FragmentMovieDetailsMotionBinding
import com.example.androidschool.moviePaging.network.Credits.CastMember
import com.example.androidschool.moviePaging.network.MovieById
import com.example.androidschool.moviePaging.notifications.AlarmReceiver
import com.example.androidschool.moviePaging.notifications.AppNotification
import com.example.androidschool.moviePaging.util.Result.Status
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment(): Fragment(R.layout.fragment_movie_details_motion) {

    private var _binding: FragmentMovieDetailsMotionBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: MovieDetailsViewModel by viewModels<MovieDetailsViewModel>()
    private val args by navArgs<MovieDetailsFragmentArgs>()
    private val movieId by lazy {
        args.movieId
    }


    lateinit var mMovieById: MovieById
    lateinit var mCastAdapter: MovieDetailsCastAdapter
    lateinit var mActionBar: ActionBar

    lateinit var movieStateObserver: Observer<Status<MovieById>>
    lateinit var castStateObserver: Observer<Status<List<CastMember>>>

    @Inject
    lateinit var mDatePicker: MaterialDatePicker.Builder<Long>
    @Inject
    lateinit var mTimePickerBuilder: MaterialTimePicker.Builder
    lateinit var mTimePicker: MaterialTimePicker
    @Inject
    lateinit var mDateAndTime: Calendar
    @Inject
    lateinit var mAppNotification: AppNotification
    @Inject
    lateinit var mPicasso: Picasso
    var mGenres = String()
    lateinit var genresObserver: Observer<String>
    var mCountries = String()
    lateinit var countriesObserver: Observer<String>

    lateinit var mNavController: NavController
    lateinit var mToolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTimePicker = mTimePickerBuilder.build()
        mViewModel.getMovieById(movieId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieDetailsMotionBinding.bind(view)

        mToolbar = mBinding.movieDetailToolbar
//        mToolbar.setTitleTextAppearance(requireContext(), R.style.style_toolbar_title)
//        mBinding.movieDetailsToolbarCol.setCollapsedTitleTextAppearance(R.style.Theme_ToolBarFontFamily)
//        mBinding.movieDetailsToolbarCol.setExpandedTitleTextAppearance(R.style.Theme_ToolBarFontFamilyExpanded)
//        mBinding.movieDetailTitleExpanded.text = mMovieById.title
//        mBinding.movieDetailTitle.text = mMovieById.title
        mNavController = findNavController()
        NavigationUI.setupWithNavController(mToolbar, mNavController)

//        initMovieById()
        initObservers()
//        initRefresh()
        initNotificationDeleteClickListener()
        initCastSection()
        initTimePicker()
        initDatePicker(mTimePicker)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun initTimePicker() {
        mTimePicker.addOnPositiveButtonClickListener {
            mDateAndTime.set(Calendar.MILLISECOND, 0)
            mDateAndTime.set(Calendar.SECOND, 0)
            mDateAndTime.set(Calendar.MINUTE, mTimePicker.minute)
            mDateAndTime.set(Calendar.HOUR_OF_DAY, mTimePicker.hour)
            Log.e("DATE PICKER", mDateAndTime.toString())

            lifecycleScope.launch(Dispatchers.Default) {
                mAppNotification.createNotificationAndAlarm(
                    mMovieById.id,
                    mMovieById.title,
                    mDateAndTime.timeInMillis
                )
            }

            val dateFormatter = SimpleDateFormat("HH:mm dd-MMMM-yyyy")
            val reminderTime: String = getString(R.string.reminder_is_set, dateFormatter.format(mDateAndTime.time))
            val snackbar = Snackbar.make(mBinding.root, reminderTime, Snackbar.LENGTH_SHORT)

            snackbar.setAction(R.string.cancel, View.OnClickListener {
                lifecycleScope.launch(Dispatchers.Default) {
                    mAppNotification.deleteNotificationAndAlarm(
                        mMovieById.id,
                        mMovieById.title
                    )
                }
            })
            snackbar.show()
        }
    }

    private fun initDatePicker(timePicker: MaterialTimePicker) {
        val picker = mDatePicker.build()
        picker.addOnPositiveButtonClickListener {
            val pickedDate = Calendar.getInstance(TimeZone.getTimeZone("Moscow"))
            pickedDate.time = Date(it)
            mDateAndTime.timeInMillis = it
            timePicker.show(childFragmentManager, "timePicker")
            Log.e("DATE PICKER", mDateAndTime.toString())
        }

        val showPickerClickListener = View.OnClickListener {
            picker.show(childFragmentManager, "datePicker")
        }

        mBinding.movieDetailBtnRemind.setOnClickListener(showPickerClickListener)
    }

    private fun initShowTrailerClickListener(id: String) {

        val showTrailerClickListener = View.OnClickListener {
            val appSrc: String = "vnd.youtube:" + id
            val webSrc: String = "http://www.youtube.com/watch?v=" + id
            val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse(appSrc))
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(webSrc))

            try {
                requireContext().startActivity(appIntent)
            } catch (e: ActivityNotFoundException) {
                requireContext().startActivity(webIntent)
            }
        }

        mBinding.fragmentMovieDetailScrolling.movieDetailFragmentTrailerPlayback.setOnClickListener(showTrailerClickListener)
    }

    private fun initNotificationDeleteClickListener() {
        val deleteNotificationClickListener = View.OnClickListener {
            val context = requireContext()
            mAppNotification.deleteNotification(mMovieById.id)
        }
        mBinding.movieDetailFragmentBackgroundImg.setOnClickListener(deleteNotificationClickListener)
    }

    private fun initObservers() {
        movieStateObserver = Observer { state ->
            when (state) {
                is Status.Success -> {
//                    hideLoading()
//                    showContent()
                    state.data.let {
                        mMovieById = it
                        renderPage(it, mGenres, mCountries)
                    }
                }
                is Status.InProgress -> {
//                    showLoading()
//                    hideContent()
                }
                is Status.Error -> {
                    val error = state.exception.toString()
                    Snackbar.make(mBinding.root, error, Snackbar.LENGTH_SHORT).show()
//                    hideLoading()
//                    hideContent()
                }
            }
        }
        mViewModel.movieState.observe(viewLifecycleOwner, movieStateObserver)

        castStateObserver = Observer { state ->
            when (state) {
                is Status.Success -> {
//                    hideLoading()
//                    showContent()
                    mCastAdapter.setList(state.data)
                }
                is Status.InProgress -> {
//                    showLoading()
//                    hideContent()
                }
                is Status.Error -> {
                    val error = state.exception.toString()
                    Snackbar.make(mBinding.root, error, Snackbar.LENGTH_SHORT).show()
//                    hideLoading()
//                    hideContent()
                }
            }
        }
        mViewModel.castState.observe(viewLifecycleOwner, castStateObserver)

        genresObserver = Observer { genres ->
            mGenres = genres
        }
        mViewModel.genres.observe(viewLifecycleOwner, genresObserver)

        countriesObserver = Observer { countries ->
            mCountries = countries
        }
        mViewModel.countries.observe(viewLifecycleOwner, countriesObserver)
    }

    private fun renderPage(movie: MovieById, genres: String, counties: String) {
        with(mBinding) {
            if (movie.backdropPath != "null") {
                mPicasso
                    .load(TMDB_IMG_URL + movie.backdropPath)
                    .centerCrop()
                    .fit()
                    .into(movieDetailFragmentBackgroundImg)
            }
//            movieDetailFragmentReleaseDate.text =
//                if (movie.releaseDate.length > 4) movie.releaseDate.substring(0, 4)
//                else movie.releaseDate
            mBinding.movieDetailTitleExpanded.text = movie.title
            mBinding.movieDetailTitle.text = movie.title
        }

        with(mBinding.fragmentMovieDetailScrolling) {
            movieDetailFragmentOverviewBody.text = movie.overview
            movieDetailFragmentAboutGenres.text = genres
            movieDetailFragmentAboutCountries.text = counties
            movieDetailFragmentRating.text = movie.voteAverage.toString()
            movieDetailFragmentRatingCount.text = getString(R.string.movie_detail_rating, movie.voteCount.toString())

            if (!movie.videos.results.isEmpty()) {
                initShowTrailerClickListener(movie.videos.results[0].key)
                if (movie.backdropPath != "null") {
                    mPicasso
                        .load(TMDB_IMG_URL + movie.backdropPath)
                        .centerCrop()
                        .fit()
                        .into(mBinding.fragmentMovieDetailScrolling.movieDetailFragmentTrailerPlayback)
                }
            } else {
                movieDetailFragmentTrailerContainer.visibility = View.GONE
            }
        }
    }

//    private fun hideLoading() {
//        mBinding.movieDetailFragmentProgressBar.visibility = View.GONE
//    }
//    private fun hideContent() {
//        mBinding.movieDetailsContentContainer.visibility = View.GONE
//    }
//    private fun showLoading() {
//        mBinding.movieDetailFragmentProgressBar.visibility = View.VISIBLE
//    }
//    private fun showContent() {
//        mBinding.movieDetailsContentContainer.visibility = View.VISIBLE
//    }
//    private fun showRefresh() {
//        mBinding.movieDetailFragmentRefreshContainer.isRefreshing = true
//    }
//    private fun hideRefresh() {
//        mBinding.movieDetailFragmentRefreshContainer.isRefreshing = false
//    }


    private fun initCastSection() {
        mCastAdapter = MovieDetailsCastAdapter(requireContext(), mPicasso)
        mBinding.fragmentMovieDetailScrolling.movieDetailFragmentCastRecycler.adapter = mCastAdapter

//        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val layoutManager = GridLayoutManager(requireContext(), 3)
        mBinding.fragmentMovieDetailScrolling.movieDetailFragmentCastRecycler.layoutManager = layoutManager

        mBinding.fragmentMovieDetailScrolling.movieDetailFragmentCastRecycler.isNestedScrollingEnabled = false
    }

//    private fun initRefresh() {
//        mBinding.movieDetailFragmentRefreshContainer.setOnRefreshListener {
//            refreshFragment()
//        }
//    }

    private fun refreshFragment() {
        mViewModel.getMovieById(movieId)
        Snackbar.make(mBinding.movieDetailFragmentRefreshContainer, R.string.fragment_updated, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewModel.movieState.removeObserver(movieStateObserver)
        mViewModel.castState.removeObserver(castStateObserver)
    }
}


