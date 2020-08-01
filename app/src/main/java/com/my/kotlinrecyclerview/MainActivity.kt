package com.my.kotlinrecyclerview


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
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
        setContentView(R.layout.drawer_layout)

//for nav drawer
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val t = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.Open, R.string.Close
        )
        drawerLayout.addDrawerListener(t)
        t.syncState()
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(
            NavigationView.OnNavigationItemSelectedListener { menuItem ->
                onNavigationItemSelected(menuItem)
                true
            })

//for nav drawer

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
    //for nav drawer
    fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val i = Intent()
        when (item.itemId) {
            R.id.nav_home -> {
//                i.setClass(this, CurrentWorkoutActivity::class.java)
//                startActivity(i)

                Toast.makeText(
                    applicationContext,
                    "Home Pressed",
                    Toast.LENGTH_LONG
                ).show()
            }
            R.id.nav_gallery -> {
                //similarly start activity with Intent
            }
            R.id.nav_slideshow -> {}

        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
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
                                applicationContext,
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

    override fun onBackPressed() {

    }
}