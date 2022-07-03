package com.example.mvvmretrofit.ui.popular_movie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvmretrofit.R
import com.example.mvvmretrofit.api.POSTER_BASE_URL
import com.example.mvvmretrofit.model.ResultsItem
import com.example.mvvmretrofit.repository.NetworkState
import com.example.mvvmretrofit.single_movie_details.SingleMovieActivity

class PopularMovieAdapter(var context: Context) :
    PagingDataAdapter<ResultsItem, RecyclerView.ViewHolder>(MovieDiffCallBack()) {


    private var networkState: NetworkState? = null
    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetWorkItemViewHolder).bind(networkState)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
            return MovieItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetWorkItemViewHolder(view)
        }
    }


    class MovieDiffCallBack : DiffUtil.ItemCallback<ResultsItem>() {

        override fun areItemsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean {
            return oldItem == newItem
        }

    }

    class MovieItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imgMovie: ImageView = itemView.findViewById(R.id.imgMovie)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)

        fun bind(data: ResultsItem?, context: Context) {
            val imageURL = POSTER_BASE_URL + data?.posterPath
            Glide.with(itemView.context)
                .load(imageURL)
                .into(imgMovie)
            tvTitle.text = data?.originalTitle
            tvDescription.text = data?.releaseDate


            itemView.setOnClickListener {
                val intent = Intent(context, SingleMovieActivity::class.java)
                intent.putExtra("id", data?.id)
                context.startActivity(intent)
            }
        }


    }

    class NetWorkItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progressBarItem: ProgressBar = itemView.findViewById(R.id.pb_item)
        val tvError: TextView = itemView.findViewById(R.id.tv_error_msg_item)

        fun bind(networkState: NetworkState?) {

            if (networkState != null && networkState == NetworkState.LOADING) {
                progressBarItem.visibility = View.VISIBLE
            } else {
                progressBarItem.visibility = View.GONE
            }

            if (networkState != null && networkState == NetworkState.ERROR) {
                tvError.visibility = View.VISIBLE
                tvError.text = networkState.msg
            } else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                tvError.visibility = View.VISIBLE
                tvError.text = networkState.msg
            } else {
                tvError.visibility = View.GONE
            }


        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState: NetworkState? = this.networkState
        val hadExtraRow: Boolean = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {//hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount()) //remove progresbar at the end
            } else { //// hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount()) //add progressbar at the end
            }
        } else if (hasExtraRow && previousState != newNetworkState) { //hasExtraRow is true and hadExtraRow false
            notifyItemChanged(itemCount - 1)
        }
    }
}