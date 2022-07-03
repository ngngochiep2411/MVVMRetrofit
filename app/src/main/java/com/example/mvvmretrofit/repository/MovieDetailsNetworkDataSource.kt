package com.example.mvvmretrofit.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmretrofit.api.ApiService
import com.example.mvvmretrofit.model.MovieDetail
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailsNetworkDataSource(
    private val apiService: ApiService,
    private val compositeDisposable: CompositeDisposable
) {

    private val _netWorkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _netWorkState

    private val _downloadedMovieDetailsResponse = MutableLiveData<MovieDetail>()
    val downloadedMovieDetailsResponse: LiveData<MovieDetail>
        get() = _downloadedMovieDetailsResponse

    fun fetchMovieDetails(movieID: Int,apiKey:String) {
        _netWorkState.postValue(NetworkState.LOADING)

        try {

            compositeDisposable.add(
                apiService.getMovieDetail(movieID,apiKey)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedMovieDetailsResponse.postValue(it)
                            _netWorkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _netWorkState.postValue(NetworkState.ERROR)
                            Log.e("getMovieDetails", it.localizedMessage)
                        }
                    )
            )

        } catch (ex: Exception) {
            Log.e("getMovieDetails","exception"+ ex.message.toString())
        }
    }
}