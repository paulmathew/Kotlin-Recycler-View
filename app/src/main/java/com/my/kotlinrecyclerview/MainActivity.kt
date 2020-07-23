package com.my.kotlinrecyclerview

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: RecyclerAdapter
    var movieList: List<Movie> = emptyList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        itemsswipetorefresh.setColorSchemeColors(Color.WHITE)
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(
                this,
                R.color.colorPrimary
            )
        )
        itemsswipetorefresh.isRefreshing = true
        setItemData()
        itemsswipetorefresh.setOnRefreshListener {
            movieList = emptyList()
            setItemData()

        }


    }

    private fun setItemData() {
        val apiInterface = ApiInterface.create().getMovies()
        apiInterface.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>?, response: Response<List<Movie>>?) {
                if (response?.body() != null) {
                    movieList = response.body()!!
                    recyclerAdapter = RecyclerAdapter(clickListener = { itemPos ->
                        Toast.makeText(
                            applicationContext,
                            movieList[itemPos].title,
                            Toast.LENGTH_LONG
                        ).show()
                    }, movieListItems = movieList)
                    recyclerView.adapter = recyclerAdapter
                    itemsswipetorefresh.isRefreshing = false
                }
            }

            override fun onFailure(call: Call<List<Movie>>?, t: Throwable?) {
                Log.e("Failed ", t?.localizedMessage.toString())
            }
        })

    }
}