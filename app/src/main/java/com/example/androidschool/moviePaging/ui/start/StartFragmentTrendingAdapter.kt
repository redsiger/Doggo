package com.example.androidschool.moviePaging.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.data.utils.TMDB_IMG_URL
import com.example.androidschool.moviePaging.databinding.FragmentStartTrendingItemBinding
import com.example.androidschool.moviePaging.model.Movie
import com.example.androidschool.moviePaging.util.MovieAdapter
import com.squareup.picasso.Picasso

class StartFragmentTrendingAdapter(private val picasso: Picasso): RecyclerView.Adapter<StartFragmentTrendingAdapter.TrendingItemHolder>(), MovieAdapter {

    private var trendingList: List<Movie> = emptyList()

    override fun setList(list: List<Movie>) {
        val diffUtilCallBack = MovieAdapterDiffUtil(trendingList, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)

        trendingList = list
        diffResult.dispatchUpdatesTo(this)
    }

    class TrendingItemHolder(view: View, private val picasso: Picasso): RecyclerView.ViewHolder(view) {
        val mBinding = FragmentStartTrendingItemBinding.bind(view)

        fun bind(movie: Movie) {
            with(mBinding) {
                if (movie.posterPath != "null") {
                    picasso
                        .load(TMDB_IMG_URL + movie.backdropPath)
                        .resizeDimen(R.dimen.trending_item_width, R.dimen.trending_item_height)
                        .into(trendingItemBackground)
                } else {
                    trendingItemBackground.setImageResource(R.drawable.ic_movie_card_viewholder)
                }
                trendingItemTitle.text = movie.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_start_trending_item, parent, false)
        return TrendingItemHolder(view, picasso)
    }

    override fun onBindViewHolder(holder: TrendingItemHolder, position: Int) {
        val movie: Movie = trendingList[position]
        holder.bind(movie)

        val bundle = Bundle()
        val id = movie.id.toString()
        bundle.putString("MovieId", id)

        holder.itemView.setOnClickListener {
            it.findNavController().navigate(R.id.action_startFragment_to_movieDetailsFragment, bundle)
        }
    }

    override fun getItemCount(): Int = trendingList.size


}