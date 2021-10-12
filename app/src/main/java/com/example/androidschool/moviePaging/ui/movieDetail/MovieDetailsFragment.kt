package com.example.androidschool.moviePaging.ui.movieDetail

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.data.room.Alarm
import com.example.androidschool.moviePaging.data.room.AlarmsDao
import com.example.androidschool.moviePaging.data.room.AlarmsDatabase
import com.example.androidschool.moviePaging.data.utils.TMDB_IMG_URL
import com.example.androidschool.moviePaging.databinding.FragmentMovieDetailsBinding
import com.example.androidschool.moviePaging.network.Credits.CastMember
import com.example.androidschool.moviePaging.network.MovieById
import com.example.androidschool.moviePaging.notifications.AlarmReceiver
import com.example.androidschool.moviePaging.notifications.AppNotification
import com.example.androidschool.moviePaging.notifications.CHANNEL_1_ID
import com.example.androidschool.moviePaging.notifications.TimePickerFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

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

    @Inject
    lateinit var mDatePicker: MaterialDatePicker<Long>
    @Inject
    lateinit var mTimePicker: MaterialTimePicker
    @Inject
    lateinit var mDateAndTime: Calendar
    lateinit var mAppNotification: AppNotification

    @Inject
    lateinit var mAlarmsRoomDao: AlarmsDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAppNotification = AppNotification(requireContext())
//        mAlarmsRoomDao = AlarmsDatabase.getInstance(requireContext())!!.getAlarmsDao()
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
        initTimePicker()
        initDatePicker(mTimePicker)

        return mBinding.root
    }

    private fun initTimePicker() {
        mTimePicker.addOnPositiveButtonClickListener {
            mDateAndTime.set(Calendar.MILLISECOND, 0)
            mDateAndTime.set(Calendar.SECOND, 0)
            mDateAndTime.set(Calendar.MINUTE, mTimePicker.minute)
            mDateAndTime.set(Calendar.HOUR_OF_DAY, mTimePicker.hour)
            Log.e("DATE PICKER", mDateAndTime.toString())

            var pickedDateAndTime: String = mDateAndTime.get(Calendar.YEAR).toString()
            pickedDateAndTime += "-"
            val pickedMonth = (mDateAndTime.get(Calendar.MONTH) + 1) % 12
            pickedDateAndTime += pickedMonth.toString()
            pickedDateAndTime += "-"
            pickedDateAndTime += mDateAndTime.get(Calendar.DAY_OF_MONTH).toString()
            pickedDateAndTime += " "
            pickedDateAndTime += mDateAndTime.get(Calendar.HOUR_OF_DAY).toString()
            pickedDateAndTime += ":"
            pickedDateAndTime += mDateAndTime.get(Calendar.MINUTE).toString()

            val intent = Intent(requireContext(), AlarmReceiver::class.java)
//            intent.action = mMovieById.title
            intent.action = "PUSH_NOTIFICATION " + mMovieById.title

            intent.putExtra("MovieId", mMovieById.id.toString())
            intent.putExtra("MovieTitle", mMovieById.title)
            intent.putExtra("MovieOverview", mMovieById.overview)

            val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, 0)
            mAppNotification.createAlarm(pendingIntent, mDateAndTime.timeInMillis)

            lifecycleScope.launch(Dispatchers.IO) {
                val alarm: Alarm = Alarm(
                    mMovieById.id,
                    mMovieById.title,
                    mDateAndTime.timeInMillis
                )
                mAlarmsRoomDao.addAlarm(alarm)
            }

            val dateFormatter = SimpleDateFormat("HH:mm dd-MMMM-yyyy")
            val reminderTime: String = getString(R.string.reminder_is_set, dateFormatter.format(mDateAndTime.time))
            Snackbar.make(mBinding.movieDetailFragmentCastRecycler, reminderTime, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun initDatePicker(timePicker: MaterialTimePicker) {
        mDatePicker.addOnPositiveButtonClickListener {
            val pickedDate = Calendar.getInstance(TimeZone.getTimeZone("Moscow"))
            pickedDate.time = Date(it)
            mDateAndTime.timeInMillis = it
            timePicker.show(childFragmentManager, "timePicker")
            Log.e("DATE PICKER", mDateAndTime.toString())
        }

        val showPickerClickListener = View.OnClickListener {
            mDatePicker.show(childFragmentManager, "datePicker")
        }

        mBinding.movieDetailFragmentButtonReminder.setOnClickListener(showPickerClickListener)
    }

    private fun initNotificationCreateClickListener() {
        val createNotificationClickListener = View.OnClickListener {
            val context = requireContext()
            val bundle = Bundle()
            bundle.putInt("MovieId", mMovieById.id)
            mAppNotification.pushNotification(
                context,
                CHANNEL_1_ID,
                mMovieById.title,
                mMovieById.overview,
                bundle,
                mMovieById.id
            )
        }
        mBinding.movieDetailFragmentTitle.setOnClickListener(createNotificationClickListener)
    }

    private fun initNotificationDeleteClickListener() {
        val deleteNotificationClickListener = View.OnClickListener {
            val context = requireContext()
            mAppNotification.deleteNotification(context,mMovieById.id)
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
                            genresString = genresString + genre.name + ", "
                        } else {
                            genresString += genre.name
                        }
                    }
                }
                movieDetailFragmentAboutGenres.text = genresString

                var countriesString: String = "-"
                if (countriesList.isNotEmpty()) {
                    countriesString = ""
                    countriesList.forEach { country ->
                        if (country != countriesList.last()) {
                            countriesString = countriesString + country.name + ", "
                        } else {
                            countriesString += country.name
                        }
                    }
                }

                movieDetailFragmentAboutCountries.text = countriesString
//                movieDetailFragmentRating.text = getString(R.string.movie_detail_rating, movie.voteAverage.toString())
                movieDetailFragmentRating.text = movie.voteAverage.toString()
                movieDetailFragmentRatingCount.text = getString(R.string.movie_detail_rating, movie.voteCount.toString())

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