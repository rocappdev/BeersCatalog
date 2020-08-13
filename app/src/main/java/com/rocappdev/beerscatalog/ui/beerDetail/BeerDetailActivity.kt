package com.rocappdev.beerscatalog.ui.beerDetail

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.rocappdev.beerscatalog.R
import com.rocappdev.beerscatalog.databinding.ActivityBeerDetailBinding
import com.rocappdev.beerscatalog.domain.Beer
import com.rocappdev.beerscatalog.ui.beerList.BeerListActivity
import com.rocappdev.beerscatalog.util.loadImage
import kotlinx.android.synthetic.main.activity_beer_detail.*

class BeerDetailActivity : AppCompatActivity() {
    private lateinit var selectedBeer: Beer
    private lateinit var binding: ActivityBeerDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.extras?.let {
            selectedBeer = intent.getSerializableExtra(BeerListActivity.SELECTED_BEER) as Beer
        }
        setViews()
    }

    private fun setViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_beer_detail)
        binding.selectedBeer = selectedBeer

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = selectedBeer.name

        selectedBeer.imageUrl?.let { imageUrl ->
            loadImage(image, imageUrl)
        }

        selectedBeer.foodPairing?.forEach {
            val foodData = TextView(this)
            foodData.text = it
            food_pairing_data.addView(foodData)
        }

        setReplacementState()

        replacement.setOnClickListener {
            selectedBeer.notAvailable = !selectedBeer.notAvailable
            setReplacementState()
        }
    }

    private fun setReplacementState() {
        if (selectedBeer.notAvailable) {
            notAvailable.visibility = View.VISIBLE
            replacement.text = getString(R.string.replacement_done)
        } else {
            notAvailable.visibility = View.GONE
            replacement.text = getString(R.string.needs_replacement)
        }
    }

    override fun onBackPressed() {
        goBackSavingData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                goBackSavingData()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goBackSavingData() {
        val intent = Intent(this, BeerListActivity::class.java)
        intent.putExtra(BeerListActivity.SELECTED_BEER, selectedBeer)
        setResult(RESULT_OK, intent)
        finish()
    }
}