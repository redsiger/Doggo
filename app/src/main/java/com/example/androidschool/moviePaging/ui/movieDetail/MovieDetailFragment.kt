package com.example.androidschool.moviePaging.ui.movieDetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.databinding.FragmentMovieDetailsMotionBinding

class MovieDetailFragment(): Fragment(R.layout.fragment_movie_details_motion) {

    private var _binding: FragmentMovieDetailsMotionBinding? = null
    private val mBinding get() = _binding!!

    lateinit var mNavController: NavController
    lateinit var mToolbar: androidx.appcompat.widget.Toolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieDetailsMotionBinding.bind(view)

        mToolbar = mBinding.movieDetailToolbar
        mNavController = findNavController()
        NavigationUI.setupWithNavController(mToolbar, mNavController)

        mToolbar.title = ""

        mBinding.movieDetailTitleExpanded.text = "Some movie title"
        mBinding.movieDetailTitle.text = "Some movie title"
    }
}