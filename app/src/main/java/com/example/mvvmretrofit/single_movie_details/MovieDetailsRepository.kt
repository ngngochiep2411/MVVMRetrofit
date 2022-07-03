package com.example.mvvmretrofit.single_movie_details

import androidx.lifecycle.LiveData
import com.example.mvvmretrofit.api.ApiService
import com.example.mvvmretrofit.model.MovieDetail
import com.example.mvvmretrofit.repository.MovieDetailsNetworkDataSource
import com.example.mvvmretrofit.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService: ApiService) {

    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails(
        compositeDisposable: CompositeDisposable,
        movieID: Int,
        apiKey:String
    ): LiveData<MovieDetail> {

        movieDetailsNetworkDataSource =
            MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieID,apiKey)

        return movieDetailsNetworkDataSource.downloadedMovieDetailsResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }
}