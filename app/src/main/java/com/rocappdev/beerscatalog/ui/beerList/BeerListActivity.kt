package com.rocappdev.beerscatalog.ui.beerList

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rocappdev.beerscatalog.R
import com.rocappdev.beerscatalog.domain.Beer
import com.rocappdev.beerscatalog.ui.beerDetail.BeerDetailActivity
import com.rocappdev.beerscatalog.ui.beerList.viewmodel.BeerListViewModel
import kotlinx.android.synthetic.main.activity_beer_list.*


class BeerListActivity : AppCompatActivity() {
    private val beerListViewModel: BeerListViewModel by viewModels()
    private lateinit var beerAdapter: BeerListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer_list)

        setViews()
        setObservers()
    }

    private fun setViews() {
        beerAdapter = BeerListAdapter { beer ->
            openBeerDetail(beer)
        }
        beerList.adapter = beerAdapter

        val mLayoutManager = LinearLayoutManager(applicationContext)
        beerList.layoutManager = mLayoutManager

        beerList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleBeer = mLayoutManager.findLastVisibleItemPosition()
                val lvbPosition = mLayoutManager.itemCount

                beerListViewModel.getNextPage(lastVisibleBeer, lvbPosition)
            }
        })
    }

    private fun openBeerDetail(beer: Beer) {
        val intent = Intent(this, BeerDetailActivity::class.java)
        intent.putExtra(SELECTED_BEER, beer)
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                data?.extras?.let {
                    val selectedBeer =
                        data.getSerializableExtra(SELECTED_BEER) as Beer
                    beerListViewModel.setBeerAvailability(selectedBeer)
                }
            }
        }
    }

    private fun setObservers() {
        beerListViewModel.beerList.observe(this, Observer {
            errorList.visibility = View.GONE
            beerList.visibility = View.VISIBLE
            beerAdapter.setData(it.list)
        })
        beerListViewModel.error.observe(this, Observer {
            errorList.visibility = View.VISIBLE
            beerList.visibility = View.GONE
            errorList.text = it.message
        })
        beerListViewModel.loading.observe(this, Observer {
            if (it) {
                loadingList.visibility = View.VISIBLE
                errorList.visibility = View.GONE
            } else
                loadingList.visibility = View.GONE
        })
    }

    companion object {
        const val REQUEST_CODE = 100
        const val SELECTED_BEER = "selectedBeer"
    }
}