package com.example.mvvmretrofit.api

import com.example.mvvmretrofit.model.ListMovie
import com.example.mvvmretrofit.model.MovieDetail
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //api list movie , movie detail
    //https://api.themoviedb.org/3/movie/popular?api_key=ffdb03735928fcc0efda02a0db29b49e&language=en-US&page=1
    //https://api.themoviedb.org/3/movie/453395?api_key=ffdb03735928fcc0efda02a0db29b49e


    @GET("/3/movie/popular")
    fun getListMovie(
        @Path("api_key") apiKey: String,
        @Path("language") language: String,
        @Path("page") page: Int
    ): Observable<ListMovie>

    @GET("/3/movie/{movie_id}")
    fun getMovieDetail(
        @Path("movie_id") movieID: Int,
        @Query("api_key") apiKey: String

    ): Single<MovieDetail>
}