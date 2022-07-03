package com.example.mvvmretrofit.repository

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}


data class NetworkState(val status: Status, val msg: String) {

    companion object {
        val LOADED: NetworkState
        val LOADING: NetworkState
        val ERROR: NetworkState
        val ENDOFLIST: NetworkState

        init {
            LOADED = NetworkState(Status.SUCCESS, "Success")
            LOADING = NetworkState(Status.RUNNING, "Loading...")
            ERROR = NetworkState(Status.FAILED, "Error")
            ENDOFLIST = NetworkState(Status.FAILED,"you have reached the end")
        }
    }


}