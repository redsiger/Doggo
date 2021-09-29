package com.example.androidschool.moviePaging.ui.popularMovies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.data.utils.TMDB_IMG_URL
import com.example.androidschool.moviePaging.databinding.FragmentMovieDetailsBinding
import com.example.androidschool.moviePaging.model.MovieById
import com.example.androidschool.moviePaging.notifications.AppNotification
import com.example.androidschool.moviePaging.notifications.CHANNEL_1_ID
import com.example.androidschool.moviePaging.notifications.TimePickerFragment
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment: Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val mBinding get() = _binding!!
//    private val mViewModel: MovieDetailsViewModel by viewModels{ MovieDetailsViewModelFactory(
//        requireArguments()
//            .getString("MovieId")!!)
//    }
    private val mViewModel: MovieDetailsViewModel by viewModels<MovieDetailsViewModel>()

    lateinit var mMovieByIdObserver: Observer<MovieById>
    lateinit var mIsMoviesLoadedObserver: Observer<Boolean>
    lateinit var mMovieById: MovieById
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

        val showTimePickerDialogListener = View.OnClickListener {
            showTimePicker()
        }

        val createNotificationClickListener = View.OnClickListener {
            val context = requireContext()
            mAppNotification.pushNotification(
                context,
                CHANNEL_1_ID,
                mMovieById.title,
                mMovieById.overview,
                1
            )
//            lifecycleScope.launch {
//                delay(3000)
//                notificator.deleteNotification(context, 1)
//            }
        }

        val deleteNotificationClickListener = View.OnClickListener {
            val context = requireContext()
            mAppNotification.deleteNotification(context,1)
        }

        mBinding.apply {
            movieDetailFragmentBackgroundImg.setOnClickListener(createNotificationClickListener)
            movieDetailFragmentMovieImg.setOnClickListener(deleteNotificationClickListener)
        }
//        mBinding.movieDetailFragmentBackgroundImg.setOnClickListener(createNotificationClickListener)
//        mBinding.movieDetailFragmentBackgroundImg.setOnClickListener(showTimePickerDialogListener)

        mMovieByIdObserver = Observer { movie ->
            mMovieById = movie
            with(mBinding) {
                movieDetailFragmentTitle.text = movie.originalTitle
                movieDetailFragmentReleaseDate.text = movie.releaseDate
                movieDetailFragmentDesc.text = movie.overview

                if (movie.posterPath != null) {
                    Picasso.get()
                        .load(TMDB_IMG_URL + movie.posterPath)
//                        .placeholder()
                        .resizeDimen(R.dimen.item_movie_img_width, R.dimen.item_movie_img_height)
                        .into(movieDetailFragmentMovieImg)
                } else {
                    movieDetailFragmentMovieImg.setImageResource(R.drawable.ic_loading_failed)
                }

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

        return mBinding.root
    }


//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val text = arguments?.getString("MovieId")
//        mBinding.movieDetailFragmentTitle.text = text
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewModel.movieById.removeObserver(mMovieByIdObserver)
    }
}