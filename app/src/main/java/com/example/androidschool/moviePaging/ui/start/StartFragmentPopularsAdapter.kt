package com.example.androidschool.moviePaging.ui.start

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.data.utils.TMDB_IMG_URL
import com.example.androidschool.moviePaging.databinding.FragmentStartRecyclerItemBinding
import com.example.androidschool.moviePaging.model.Movie
import com.squareup.picasso.Picasso

class StartFragmentPopularsAdapter(private val context: Context): RecyclerView.Adapter<StartFragmentPopularsAdapter.MovieHolder>() {

    private var movieList: List<Movie> = emptyList()

    fun setList(list: List<Movie>) {
        movieList = list
        notifyDataSetChanged()
    }

    class MovieHolder(view: View): RecyclerView.ViewHolder(view) {
        val mBinding = FragmentStartRecyclerItemBinding.bind(view)

        fun bind(movie: Movie) {
            with(mBinding) {
                if (movie.posterPath != "null") {
                    Picasso.get().load(TMDB_IMG_URL + movie.posterPath).resizeDimen(R.dimen.start_fragment_section_movie_width, R.dimen.start_fragment_section_movie_height).into(startFragmentPopularsMovieImg)
                } else {
                    startFragmentPopularsMovieImg.setImageResource(R.drawable.ic_movie_card_viewholder)
                }
                startFragmentMovieRating.text = movie.voteAverage.toString()
                startFragmentMovieTitle.text = movie.title
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_start_recycler_item, parent, false)
        return MovieHolder(view)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val movie: Movie = movieList[position]
        holder.bind(movie)

        val bundle: Bundle = Bundle()
        val id = movie.id.toString()
        val movieTitle = movie.title
        bundle.putString("MovieId", id)

        val anims = NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_down)
            .setExitAnim(R.anim.fade_out)
            .setPopExitAnim(R.anim.slide_out_down)
            .setPopEnterAnim(R.anim.fade_in)
            .build()

        holder.itemView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.movieDetailsFragment, bundle, anims)
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}