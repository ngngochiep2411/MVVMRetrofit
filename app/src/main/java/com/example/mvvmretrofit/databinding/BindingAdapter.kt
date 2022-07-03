package com.example.mvvmretrofit.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.mvvmretrofit.R
import com.example.mvvmretrofit.api.POSTER_BASE_URL


@BindingAdapter("setImageUrl")
fun setImageViewResource(imageView: ImageView, path: String) {
    Glide.with(imageView.context)
        .load(POSTER_BASE_URL+path)
        .placeholder(R.mipmap.ic_launcher)
        .into(imageView)
}

