package com.example.androidschool.moviePaging.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidschool.moviePaging.model.Movie
import com.example.androidschool.moviePaging.network.DEFAULT_PAGE_INDEX
import com.example.androidschool.moviePaging.network.DEFAULT_PAGE_SIZE
import com.example.androidschool.moviePaging.network.MovieService
import retrofit2.HttpException

class SearchResultPagingSource (
    private val movieService: MovieService,
    private val query: String
) : PagingSource<Int, Movie>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        if (query.isEmpty()) {
            return LoadResult.Page(emptyList(), null, null)
        }

        val page: Int = params.key ?: DEFAULT_PAGE_INDEX
        val pageSize: Int = params.loadSize ?: DEFAULT_PAGE_SIZE

        val response = movieService.getSearchResult(query, page)
        return if (response.isSuccessful) {
            val movies : List<Movie> = checkNotNull(response.body()).results
            val nextKey = if (params.key == response.body()?.totalPages) null else page+1
            val prevKey = if (page == 1) null else page-1
            LoadResult.Page(movies, prevKey, nextKey)
        } else {
            LoadResult.Error(HttpException(response))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.plus(1)
    }
}