package com.my.kotlinrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.squareup.picasso.Picasso

class RecyclerAdapter(
    val clickListener: (Int) -> Unit,
    val movieListItems: List<Movie>

) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    private var movieList: List<Movie> = movieListItems

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvMovieName.text = movieList[position].title
        val imgUrl: String = movieList[position].image?.replace("http", "https").toString()

        Picasso.get().load(imgUrl)
            .fit()
            .placeholder(R.drawable.progress_animation)
            .into(holder.image)
// click listener
        holder.itemView.setOnClickListener { clickListener(position) }
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvMovieName: TextView = itemView.findViewById(R.id.title)
        val image: ImageView = itemView.findViewById(R.id.image)

    }
}