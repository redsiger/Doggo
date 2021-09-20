package com.example.androidschool.moviePaging.ui.popularMovies

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.data.utils.TMBD_IMG_URL
import com.example.androidschool.moviePaging.databinding.FragmentMovieDetailsBinding
import com.example.androidschool.moviePaging.model.MovieById
import com.squareup.picasso.Picasso

class MovieDetailsFragment: Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val mBinding get() = _binding!!
//    private val args : MovieDetailsFragmentArgs by navArgs()
//    private val mViewModel: MovieDetailsViewModel by viewModels{ MovieDetailsViewModelFactory(args.movieId!!)
//    }
        private val mViewModel: MovieDetailsViewModel by viewModels{ MovieDetailsViewModelFactory(
        requireArguments()
            .getString("MovieId")!!)
    }
    lateinit var mMovieByIdObserver: Observer<MovieById>
    lateinit var mIsMoviesLoadedObserver: Observer<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        mMovieByIdObserver = Observer { movie ->
            with(mBinding) {
                movieDetailFragmentTitle.text = movie.originalTitle
                movieDetailFragmentReleaseDate.text = movie.releaseDate
                movieDetailFragmentDesc.text = movie.overview

                if (movie.posterPath != null) {
                    Picasso.get()
                        .load(TMBD_IMG_URL + movie.posterPath)
//                        .placeholder()
                        .resizeDimen(R.dimen.item_movie_img_width, R.dimen.item_movie_img_height)
                        .into(movieDetailFragmentMovieImg)
                } else {
                    movieDetailFragmentMovieImg.setImageResource(R.drawable.ic_loading_failed)
                }

                if (movie.backdropPath != null) {
                    Picasso.get()
                        .load(TMBD_IMG_URL + movie.backdropPath)
                        .centerCrop()
                        .fit()
                        .into(movieDetailFragmentBackgroundImg)
                }
            }
        }

        mIsMoviesLoadedObserver = Observer { isLoaded ->
            when (isLoaded) {
                true -> mBinding.progressBarFragmentMovieDetails.visibility = View.GONE
                false -> mBinding.progressBarFragmentMovieDetails.visibility = View.VISIBLE
            }
        }

        mViewModel.isMoviesLoaded.observe(viewLifecycleOwner, mIsMoviesLoadedObserver)

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