package com.example.androidschool.moviePaging.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.data.utils.TMDB_IMG_URL
import com.example.androidschool.moviePaging.databinding.FragmentPopularRecyclerItemBinding
import com.example.androidschool.moviePaging.model.Movie
import com.squareup.picasso.Picasso

class MovieSearchResponsePagingAdapter(private val picasso: Picasso) : PagingDataAdapter<Movie, MovieSearchResponsePagingAdapter.MovieViewHolder>(
    MOVIE_COMPARATOR
) {

    class MovieViewHolder(view: View, private val picasso: Picasso) : RecyclerView.ViewHolder(view) {
        val mBinding = FragmentPopularRecyclerItemBinding.bind(view)
//        val mBinding = FragmentStartRecyclerItemBinding.bind(view)

        fun bind(movie: Movie) {
            with(mBinding.fragmentPopularRecyclerItem1) {
                if (movie.posterPath != "null") {
                    picasso.load(TMDB_IMG_URL + movie.posterPath).resizeDimen(R.dimen.start_fragment_section_movie_width, R.dimen.start_fragment_section_movie_height).into(startFragmentPopularsMovieImg)
                } else {
                    startFragmentPopularsMovieImg.setImageResource(R.drawable.ic_movie_card_viewholder)
                }
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
                startFragmentMovieTitle.text = movie.title
            }
        }

//        fun bind(movie: Movie) {
//            with(mBinding) {
//                itemMovieTitle.text = movie.title
//                if (movie.releaseDate.length > 4) {
//                    itemMovieYear.text = movie.releaseDate.substring(0, 4)
//                }
//                itemMovieDesc.text = movie.overview
//
//                if (movie.posterPath != null) {
//                    picasso
//                        .load(TMDB_IMG_URL + movie.posterPath)
////                    .placeholder(R.drawable.movie_img_loading_anim)
//                        .resizeDimen(R.dimen.item_movie_img_width, R.dimen.item_movie_img_height)
//                        .into(itemMovieImg)
//                } else {
//                    itemMovieImg.setImageResource(R.drawable.ic_loading_failed)
//                }
//            }
//        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(movie = it) }

        val movie = getItem(position)
        val bundle = Bundle()
        val id = movie?.id.toString()
        val movieTitle = movie?.title
        bundle.putString("MovieId", id)
        bundle.putString("MovieTitle", movieTitle)

        holder.itemView.setOnClickListener {
            it.findNavController().navigate(R.id.movie_detail_graph, bundle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_movie_recycler_item, parent, false)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_popular_recycler_item, parent, false)
        return MovieViewHolder(view, picasso)
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