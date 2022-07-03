package com.example.mvvmretrofit.single_movie_details

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bumptech.glide.Glide
import com.example.mvvmretrofit.R
import com.example.mvvmretrofit.api.ApiService
import com.example.mvvmretrofit.api.POSTER_BASE_URL
import com.example.mvvmretrofit.api.RetrofitClient
import com.example.mvvmretrofit.api.api_key
import com.example.mvvmretrofit.databinding.ActivitySingleMovieBinding
import com.example.mvvmretrofit.model.MovieDetail
import com.example.mvvmretrofit.repository.NetworkState

class SingleMovieActivity : AppCompatActivity() {

    lateinit var viewModel: SingleMovieViewModel
    lateinit var movieRepository: MovieDetailsRepository
    val TAG = "SingleMovieActivity"
    lateinit var binding: ActivitySingleMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_single_movie)

        val movieID = 453395
        val apiService: ApiService = RetrofitClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)




        viewModel = getViewModel(movieID)
        viewModel.movieDetails.observe(this, {
            val imageURL = POSTER_BASE_URL + it.posterPath
            Glide.with(this).load(imageURL)
                .into(binding.imgMovie)
        })

        viewModel.networkState.observe(this, {
            binding.pbLoading.visibility =
                if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.tvError.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })

    }

    private fun bindUI(item: MovieDetail) {
        TODO("Not yet implemented")

    }

    private fun getViewModel(movieID: Int): SingleMovieViewModel {
        return ViewModelProviders.of(this, viewModelFactory {
            return SingleMovieViewModel(movieRepository, movieID, api_key)
        })[SingleMovieViewModel::class.java]
    }


}