package com.rocappdev.beerscatalog.ui.beerList

import android.content.Context
import android.os.Build
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rocappdev.beerscatalog.R
import com.rocappdev.beerscatalog.databinding.RowBeerItemBinding
import com.rocappdev.beerscatalog.domain.Beer

class BeerListAdapter(
    private val onClickListener: (Beer) -> Unit
) : RecyclerView.Adapter<BeerListAdapter.ViewHolder>() {

    private var items: List<Beer> = listOf()
    private lateinit var context: Context
    private var mLastClickTime: Long = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val binding = RowBeerItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val beer = items[position]
        holder.appBinding.beer = beer
        if (beer.notAvailable)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.appBinding.cardviewItem.backgroundTintList =
                    context.resources.getColorStateList(R.color.lightGray, null)
            } else {
                @Suppress("DEPRECATION")
                holder.appBinding.cardviewItem.backgroundTintList =
                    context.resources.getColorStateList(R.color.lightGray)
            }
        else
            holder.appBinding.cardviewItem.backgroundTintList = null

        holder.itemView.setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            onClickListener(items[position])
        }
    }

    fun setData(list: List<Beer>) {
        items = list
        notifyDataSetChanged()
    }

    class ViewHolder(val appBinding: RowBeerItemBinding) : RecyclerView.ViewHolder(appBinding.root)
}