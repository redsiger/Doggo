package com.example.androidschool.moviePaging.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.databinding.FragmentMovieRecyclerItemBinding
import com.example.androidschool.moviePaging.databinding.FragmentPopularMoviesBinding
import com.example.androidschool.moviePaging.model.Movie
import com.example.androidschool.moviePaging.network.TMBD_IMG_URL
import com.example.androidschool.moviePaging.ui.popularMovies.MovieDetailsFragment
import com.example.androidschool.moviePaging.ui.popularMovies.PopularMoviesFragmentDirections
import com.google.android.material.snackbar.Snackbar
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

            val extras = FragmentNavigatorExtras(
                mBinding.itemMovieImg to "movie_img"
            )

        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(movie = it) }

        val movie = getItem(position)

        holder.itemView.setOnClickListener {
            val bundle: Bundle = Bundle()
            val id = movie?.id.toString()
            bundle.putString("MovieId", id)

            val movieId: String = movie?.id.toString()
            val action = PopularMoviesFragmentDirections.actionPopularMoviesFragmentToMovieDetailsFragment().setMovieId(movieId)

            val extras = FragmentNavigatorExtras(
                holder.mBinding.itemMovieImg to "movie_img"
            )

            val anims = NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_right)
                .setExitAnim(R.anim.slide_out_left)
                .setPopExitAnim(R.anim.slide_out_right)
                .setPopEnterAnim(R.anim.slide_in_left)
                .build()


            Navigation.findNavController(it).navigate(R.id.movieDetailsFragment, bundle, anims)

            Snackbar.make(it, "$position", Snackbar.LENGTH_SHORT).show()
        }
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