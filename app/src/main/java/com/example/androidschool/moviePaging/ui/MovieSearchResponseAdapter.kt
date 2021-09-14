package com.example.androidschool.moviePaging.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.databinding.FragmentMovieRecyclerItemBinding
import com.example.androidschool.moviePaging.databinding.FragmentPopularMoviesBinding
import com.example.androidschool.moviePaging.model.Movie
import com.example.androidschool.moviePaging.network.TMBD_IMG_URL
import com.squareup.picasso.Picasso

class MovieSearchResponseAdapter : PagingDataAdapter<Movie, MovieSearchResponseAdapter.MovieViewHolder>(
    MOVIE_COMPARATOR) {

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mBinding = FragmentMovieRecyclerItemBinding.bind(view)

        fun bind(movie: Movie) = with(mBinding) {
            itemMovieTitle.text = movie.title
            itemMovieYear.text = movie.releaseDate.substring(0, 4)
            itemMovieDesc.text = movie.overview

            if (movie.posterPath != null) {
                Picasso.get()
                    .load(TMBD_IMG_URL + movie.posterPath)
//                    .placeholder(R.drawable.movie_img_loading_anim)
                    .resizeDimen(R.dimen.item_movie_img_width, R.dimen.item_movie_img_height)
                    .into(itemMovieImg)
            } else {
                itemMovieImg.setImageResource(R.drawable.ic_loading_failed)
            }

        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(movie = it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_movie_recycler_item, parent, false)
        return MovieViewHolder(view)
    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }
}