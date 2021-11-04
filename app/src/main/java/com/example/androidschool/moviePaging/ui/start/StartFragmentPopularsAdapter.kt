package com.example.androidschool.moviePaging.ui.start

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.data.utils.TMDB_IMG_URL
import com.example.androidschool.moviePaging.databinding.FragmentPopularRecyclerItemBinding
import com.example.androidschool.moviePaging.databinding.FragmentStartRecyclerItemBinding
import com.example.androidschool.moviePaging.model.Movie
import com.example.androidschool.moviePaging.ui.movieDetail.MovieDetailsFragmentArgs
import com.example.androidschool.moviePaging.util.MovieAdapter
import com.squareup.picasso.Picasso

class StartFragmentPopularsAdapter(private val picasso: Picasso
): RecyclerView.Adapter<StartFragmentPopularsAdapter.MovieHolder>(), MovieAdapter {

    private var movieList: MutableList<Movie> = mutableListOf()

    override fun setList(list: List<Movie>) {
        val diffUtilCallBack = MovieAdapterDiffUtil(movieList, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)

        movieList = list.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

    class MovieHolder(view: View,
                      private val picasso: Picasso
                      ): RecyclerView.ViewHolder(view) {
        val mBinding = FragmentStartRecyclerItemBinding.bind(view)

        fun bind(movie: Movie) {
            with(mBinding.fragmentStartRecyclerItem) {
                startFragmentMovieTitle.text = movie.title
                startFragmentMovieRating.text = movie.voteAverage.toString()
                when {
                    movie.voteAverage >= 7.5 -> {
                        startFragmentMovieRatingCard.setBackgroundResource(R.color.colorRating10_0)
                    }
                    movie.voteAverage > 5.0 -> {
                        startFragmentMovieRatingCard.setBackgroundResource(R.color.colorRating7_5)
                    }
                    else -> {
                        startFragmentMovieRatingCard.setBackgroundResource(R.color.colorRating5_0)
                    }
                }
                if (movie.posterPath != "null") {
                    picasso
                        .load(TMDB_IMG_URL + movie.posterPath)
                        .resizeDimen(R.dimen.start_fragment_section_movie_width, R.dimen.start_fragment_section_movie_height)
                        .into(startFragmentPopularsMovieImg)
                } else {
                    startFragmentPopularsMovieImg.setImageResource(R.drawable.ic_movie_card_viewholder)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_start_recycler_item, parent, false)
        return MovieHolder(view, picasso)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val movie: Movie = movieList[position]
        holder.bind(movie)

        val bundle = Bundle()
        val id = movie.id.toString()
        bundle.putString("MovieId", id)

        val args = MovieDetailsFragmentArgs(movie.id)
        val action = StartFragmentDirections.actionStartFragmentToMovieDetailsFragment(movie.id)

        holder.itemView.setOnClickListener {
//            it.findNavController().navigate(R.id.action_startFragment_to_movieDetailsFragment, bundle)
            it.findNavController().navigate(action)
//            it.findNavController().navigate(R.id.movie_detail_graph, bundle)
        }
    }

    override fun getItemCount(): Int = movieList.size
}