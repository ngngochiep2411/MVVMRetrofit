package com.example.mvvmretrofit.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.mvvmretrofit.api.ApiService
import com.example.mvvmretrofit.model.ListMovie
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(private val apiService: ApiService,private val compositeDisposable: CompositeDisposable) : DataSource.Factory<Int, ListMovie>() {

    val moviesLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, ListMovie> {
        val movieDatasource = MovieDataSource(apiService,compositeDisposable)
        moviesLiveDataSource.postValue(movieDatasource)
        return movieDatasource
    }
}