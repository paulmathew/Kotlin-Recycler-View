package com.my.kotlinrecyclerview


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RecyclerViewFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: RecyclerAdapter
    lateinit var itemsswipetorefresh:SwipeRefreshLayout
    var movieList: List<Movie> = emptyList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.activity_main, container, false)
        recyclerView = view.findViewById(R.id.recyclerview)
        itemsswipetorefresh=view.findViewById(R.id.itemsswipetorefresh)

        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )

        itemsswipetorefresh.setColorSchemeColors(Color.WHITE)
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.colorPrimary
            )
        )
        itemsswipetorefresh.isRefreshing = true
        setItemData()
        itemsswipetorefresh.setOnRefreshListener {
            movieList = emptyList()
            setItemData()

        }

        return view
    }


    private fun setItemData() {
        val apiInterface = ApiInterface.create().getMovies()
        apiInterface.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>?, response: Response<List<Movie>>?) {
                if (response?.body() != null) {
                    movieList = response.body()!!

                    recyclerAdapter = RecyclerAdapter(
                        //calling the listener method in the init section of the Adapter class
                        clickListener = { itemPos ->
                            Toast.makeText(
                                activity,
                                movieList[itemPos].title,
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        // click listener ends
                        movieListItems = movieList
                    )
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