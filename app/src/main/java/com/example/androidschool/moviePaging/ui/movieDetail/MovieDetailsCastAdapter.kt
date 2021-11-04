package com.example.androidschool.moviePaging.ui.movieDetail

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.moviePaging.R
import com.example.androidschool.moviePaging.data.utils.TMDB_IMG_URL
import com.example.androidschool.moviePaging.databinding.FragmentMovieDetailsCastItemBinding
import com.example.androidschool.moviePaging.network.Credits.CastMember
import com.example.androidschool.moviePaging.ui.start.CastAdapterDiffUtil
import com.example.androidschool.moviePaging.ui.start.StartFragmentPopularsAdapter
import com.squareup.picasso.Picasso

class MovieDetailsCastAdapter(private val context: Context, private val picasso: Picasso): RecyclerView.Adapter<MovieDetailsCastAdapter.CastHolder>() {

    private var movieCastList: List<CastMember> = emptyList()

    fun setList(list: List<CastMember>) {
        val diffUtilCallBack = CastAdapterDiffUtil(movieCastList, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)

        movieCastList = list
        diffResult.dispatchUpdatesTo(this)
    }

    class CastHolder(view: View, private val picasso: Picasso): RecyclerView.ViewHolder(view) {
        val mBinding = FragmentMovieDetailsCastItemBinding.bind(view)

        fun bind(castMember: CastMember) {
            with(mBinding) {
                if (castMember.profilePath != "null") {
                    picasso.load(TMDB_IMG_URL + castMember.profilePath).resizeDimen(R.dimen.item_movie_img_width, R.dimen.item_movie_img_height).into(castMemberPhoto)
                } else {
                    castMemberPhoto.setImageResource(R.drawable.ic_cast_person_viewholder)
                }
                castMemberName.text = castMember.name
                castMemberCharacter.text = castMember.character
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_movie_details_cast_item, parent, false)
        return CastHolder(view, picasso)
    }

    override fun onBindViewHolder(holder: CastHolder, position: Int) {
        val castMember: CastMember = movieCastList[position]
        holder.bind(castMember)
    }

    override fun getItemCount(): Int {
        return movieCastList.size
    }


}