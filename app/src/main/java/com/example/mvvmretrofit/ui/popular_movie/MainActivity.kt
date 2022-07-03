package com.example.mvvmretrofit.ui.popular_movie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mvvmretrofit.R
import com.example.mvvmretrofit.api.RetrofitClient
import com.example.mvvmretrofit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainActivityViewModel
    lateinit var movieRepository: MoviePagedListRepository
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val apiService = RetrofitClient.getClient()
        movieRepository = MoviePagedListRepository(apiService)
        viewModel = getMainViewModel()

        val movieAdapter = PopularMovieAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 3)



        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                if (viewType == movieAdapter.MOVIE_VIEW_TYPE) return 1
                else return 3

            }

        }

        binding.rvListPopular.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            adapter = movieAdapter
        }

        viewModel.moviePagedList.observe(this , Observer{
            movieAdapter.submitData(it)
        })

    }


    private fun getMainViewModel(): MainActivityViewModel {
        return ViewModelProviders.of(this, viewModelFactory {
            return MainActivityViewModel(movieRepository)
        })[MainActivityViewModel::class.java]
    }
}