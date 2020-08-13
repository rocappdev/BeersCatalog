package com.rocappdev.beerscatalog.ui.beerList.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocappdev.beerscatalog.domain.Beer
import com.rocappdev.beerscatalog.repository.BeerList
import com.rocappdev.beerscatalog.repository.BeerRepository
import com.rocappdev.beerscatalog.repository.ErrorResponse
import com.rocappdev.beerscatalog.util.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BeerListViewModel : ViewModel() {

    private val beerRepository = BeerRepository()

    private val _beerList = MutableLiveData<BeerList>()
    val beerList: LiveData<BeerList> get() = _beerList

    private val _error = MutableLiveData<ErrorResponse>()
    val error: LiveData<ErrorResponse> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private var beersPage = FIRST_PAGE
    private var previousLastVisibleBeer = 0

    init {
        viewModelScope.launch {
            getBeers(beersPage)
        }
    }

    private fun getBeers(page: Int) {
        _loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            val beerResponse = beerRepository.getBeers(page)
            Log.d(TAG, beerResponse.toString())
            when (beerResponse) {
                is BeerList -> {
                    setBeerList(beerResponse)
                }
                is ErrorResponse -> {
                    _error.postValue(beerResponse)
                }
            }
            _loading.postValue(false)
        }
    }

    private fun setBeerList(beerResponse: BeerList) {
        if (beerResponse.list.size < BL_MIN_PAGE_SIZE)
            noMoreBeers()

        val updatedList = beerList.value?.list?.toMutableList() ?: mutableListOf()
        updatedList.addAll(beerResponse.list)

        _beerList.postValue(BeerList(updatedList))
    }

    fun setBeerAvailability(beer: Beer) {
        beerRepository.setBeerAvailability(beer)

        _beerList.postValue(beerList.value?.list?.let {
            beerRepository.updateBeerAvailability(
                beer,
                it
            )
        })
    }

    private fun noMoreBeers() {
        beersPage = Int.MIN_VALUE
    }

    private fun requestMoreBeers(): Boolean {
        return beersPage != Int.MIN_VALUE
    }

    fun getNextPage(lastVisibleBeer: Int, lvbPosition: Int) {
        if (loading.value != true && requestMoreBeers()) {
            val scrollingDown = previousLastVisibleBeer < lastVisibleBeer
            val nearTheBottom = lastVisibleBeer + BL_VISIBLE_LIMIT >= lvbPosition

            if (scrollingDown && nearTheBottom) {
                getBeers(++beersPage)
                previousLastVisibleBeer = lastVisibleBeer
            }
        }
    }

    companion object {
        private const val FIRST_PAGE = 1
        private const val BL_MIN_PAGE_SIZE = 25
        private const val BL_VISIBLE_LIMIT = 16
    }
}