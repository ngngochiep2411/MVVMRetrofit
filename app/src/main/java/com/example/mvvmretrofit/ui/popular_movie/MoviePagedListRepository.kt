package com.example.mvvmretrofit.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.mvvmretrofit.api.ApiService
import com.example.mvvmretrofit.api.POST_PER_PAGE
import com.example.mvvmretrofit.model.ListMovie
import com.example.mvvmretrofit.model.ResultsItem
import com.example.mvvmretrofit.repository.MovieDataSource
import com.example.mvvmretrofit.repository.MovieDataSourceFactory
import com.example.mvvmretrofit.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository(private val apiService: ApiService) {

    lateinit var moviePagedList: LiveData<PagedList<ResultsItem>>
    lateinit var moviesDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePageList(compositeDisposable: CompositeDisposable): LiveData<PagedList<ResultsItem>> {
        moviesDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()
        return moviePagedList
    }

    fun getNetworkState() : LiveData<NetworkState>{
        return Transformations.switchMap<MovieDataSource,NetworkState>(
            moviesDataSourceFactory.moviesLiveDataSource,MovieDataSource::networkState
        )
    }

}