package com.example.androidschool.moviePaging.ui.movieDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.data.utils.TMDB_IMG_URL
import com.example.androidschool.moviePaging.databinding.FragmentMovieDetailsBinding
import com.example.androidschool.moviePaging.network.Credits.CastMember
import com.example.androidschool.moviePaging.network.MovieById
import com.example.androidschool.moviePaging.notifications.AppNotification
import com.example.androidschool.moviePaging.notifications.CHANNEL_1_ID
import com.example.androidschool.moviePaging.notifications.TimePickerFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class MovieDetailsFragment(): Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: MovieDetailsViewModel by viewModels<MovieDetailsViewModel>()

    lateinit var mMovieByIdObserver: Observer<MovieById>
    lateinit var mMovieById: MovieById
    lateinit var mIsMoviesLoadedObserver: Observer<Boolean>
    lateinit var mMovieCastObserver: Observer<List<CastMember>>
    lateinit var mMovieCast: List<CastMember>
    lateinit var mCastAdapter: MovieDetailsCastAdapter

    lateinit var mDatePicker: MaterialDatePicker<Long>
    lateinit var mTimePicker: MaterialTimePicker
    lateinit var mAppNotification: AppNotification

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAppNotification = AppNotification(requireContext())
    }

    private fun showTimePicker() {
        // create a new instance of TimePicker
        val timePickerFragment = TimePickerFragment()
        val supportFragmentManager = requireActivity().supportFragmentManager

        supportFragmentManager.setFragmentResultListener(
            "PICKED_DATE",
            viewLifecycleOwner
        ) {
            resultKey, bundle -> if (resultKey == "REQUEST_KEY") {
                val pickedHour = bundle.getString("SELECTED_DATE")
                mAppNotification.pushNotification(requireActivity(), CHANNEL_1_ID, mMovieById.title, pickedHour!!, 1)
            }
        }

        timePickerFragment.show(supportFragmentManager, "TimePickerFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        initMovieById()
        initNotificationCreateClickListener()
        initNotificationDeleteClickListener()
        initCastSection()
        initDatePicker()

        return mBinding.root
    }

    private fun initDatePicker() {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val today = MaterialDatePicker.todayInUtcMilliseconds()

        calendar.timeInMillis = MaterialDatePicker.todayInUtcMilliseconds()
        calendar[Calendar.MONTH] = Calendar.OCTOBER
        val octThisYear = calendar.timeInMillis

        calendar.timeInMillis = MaterialDatePicker.todayInUtcMilliseconds()
        calendar[Calendar.MONTH] = Calendar.NOVEMBER
        val novThisYear = calendar.timeInMillis

        val constraints =
            CalendarConstraints.Builder()
                .setStart(today)
                .setValidator(DateValidatorPointForward.now())
                .build()

        mDatePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.datePicker_title)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraints)
            .build()

        mDatePicker.addOnPositiveButtonClickListener {
            val view = mBinding.movieDetailFragmentButtonReminder
            val pickedDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            pickedDate.time = Date(it)

            val pickedDateOfYear = pickedDate.time
            val pickedYear = pickedDate.get(Calendar.YEAR).toString()
            val pickedMonth = pickedDate.get(Calendar.MONTH).toString()
            val pickedDay = pickedDate.get(Calendar.DAY_OF_MONTH).toString()
            val pickedDayOfWeek = pickedDate.get(Calendar.DAY_OF_WEEK).toString()

            Snackbar.make(view, ""+ pickedDateOfYear, Snackbar.LENGTH_SHORT).show()
        }

        val showPickerClickListener = View.OnClickListener {
            mDatePicker.show(childFragmentManager, "datePicker")
        }

        mBinding.movieDetailFragmentButtonReminder.setOnClickListener(showPickerClickListener)
    }

    private fun initNotificationCreateClickListener() {
        val createNotificationClickListener = View.OnClickListener {
            val context = requireContext()
            mAppNotification.pushNotification(
                context,
                CHANNEL_1_ID,
                mMovieById.title,
                mMovieById.overview,
                1
            )
        }
//        mBinding.movieDetailFragmentButtonReminder.setOnClickListener(createNotificationClickListener)
    }

    private fun initNotificationDeleteClickListener() {
        val deleteNotificationClickListener = View.OnClickListener {
            val context = requireContext()
            mAppNotification.deleteNotification(context,1)
        }
        mBinding.movieDetailFragmentBackgroundImg.setOnClickListener(deleteNotificationClickListener)
    }

    private fun initMovieById() {
        mMovieByIdObserver = Observer { movie ->
            mMovieById = movie
            val genresList = movie.genres
            val countriesList = movie.productionCountries
            with(mBinding) {
                movieDetailFragmentTitle.text = movie.title
                movieDetailFragmentOverviewBody.text = movie.overview
                movieDetailFragmentReleaseDate.text = movie.releaseDate.substring(0, 4)

                var genresString: String = "-"
                if (genresList.isNotEmpty()) {
                    genresString = ""
                    genresList.forEach { genre ->
                        if (genre != genresList.last()) {
                            genresString = genresString + " " + genre.name + ","
                        } else {
                            genresString = genresString + " " + genre.name
                        }
                    }
                }
                movieDetailFragmentAboutGenres.text = genresString

                var countriesString: String = "-"
                if (countriesList.isNotEmpty()) {
                    countriesString = ""
                    countriesList.forEach { country ->
                        if (country != countriesList.last()) {
                            countriesString = countriesString + " " + country.name + ","
                        } else {
                            countriesString = countriesString + " " + country.name
                        }
                    }
                }

                movieDetailFragmentAboutCountries.text = countriesString
                movieDetailFragmentRating.text = movie.voteAverage.toString()

                if (movie.backdropPath != null) {
                    Picasso.get()
                        .load(TMDB_IMG_URL + movie.backdropPath)
                        .centerCrop()
                        .fit()
                        .into(movieDetailFragmentBackgroundImg)
                }
            }
        }
        mViewModel.movieById.observe(viewLifecycleOwner, mMovieByIdObserver)
    }

    private fun initCastSection() {
        mCastAdapter = MovieDetailsCastAdapter(requireContext())
        mBinding.movieDetailFragmentCastRecycler.adapter = mCastAdapter

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mBinding.movieDetailFragmentCastRecycler.layoutManager = layoutManager

        mMovieCastObserver = Observer {
            mCastAdapter.setList(it)
        }

        mViewModel.movieByIdCredits.observe(viewLifecycleOwner, mMovieCastObserver)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mViewModel.movieById.removeObserver(mMovieByIdObserver)
        mViewModel.movieByIdCredits.removeObserver(mMovieCastObserver)
    }
}