package com.example.mvvmretrofit.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.mvvmretrofit.api.ApiService
import com.example.mvvmretrofit.api.FIRST_PAGE
import com.example.mvvmretrofit.api.api_key
import com.example.mvvmretrofit.model.ListMovie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource(
    private val apiService: ApiService,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, ListMovie>() {

    private var page = FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()


    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ListMovie>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getListMovie(api_key, "en-US", params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if(it.totalPages!! >= params.key){
                            callback.onResult(it.results as List<ListMovie>,params.key+1)
                            networkState.postValue(NetworkState.LOADED)
                        }else{

                        }
                    }, {
                        networkState.postValue(NetworkState.ERROR)
                        Log.d("get list movie", it.localizedMessage)
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ListMovie>) {

    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, ListMovie>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getListMovie(api_key, "en-US", page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.results as List<ListMovie>, null, page + 1)
                        networkState.postValue(NetworkState.LOADED)
                    }, {
                        networkState.postValue(NetworkState.ENDOFLIST)
                        Log.d("get list movie", it.localizedMessage)
                    }
                )
        )
    }
}