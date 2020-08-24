package com.android.tlb.home.ui.home

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.android.tlb.databinding.MainCategoryListViewBinding
import com.android.tlb.databinding.ProductListViewBinding
import com.android.tlb.databinding.TrendingPagerListViewBinding
import com.android.tlb.home.data.FragmentHomeCommand
import com.android.tlb.home.data.MainCategoriesAdapter
import com.android.tlb.home.data.ProductListAdapter
import com.android.tlb.home.data.TrendingListPagerAdapter
import com.android.tlb.home.data.model.Data
import com.android.tlb.utils.toast
import java.util.*
import kotlin.collections.ArrayList


class HomeListAdapter(
    private var command: FragmentHomeCommand,
    private var requireActivity: FragmentActivity
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_HOR = 0
        const val VIEW_TYPE_PAGER = 1
        const val VIEW_TYPE_GRID = 2
    }

    private val mCategoryList = ArrayList<Data>()

    fun setAppList(categoryModel: List<Data>) {
        mCategoryList.clear()
        mCategoryList.addAll(categoryModel)

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mCategoryList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            1 -> {
                VIEW_TYPE_PAGER
            }
            3 -> {
                VIEW_TYPE_GRID
            }
            5 -> {
                VIEW_TYPE_GRID
            }
            8 -> {
                VIEW_TYPE_GRID
            }
            11 -> {
                VIEW_TYPE_GRID
            }
            12 -> {
                VIEW_TYPE_PAGER
            }
            else -> {
                VIEW_TYPE_HOR
            }
        }
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_HOR -> {
                val holder = vh as ViewHolderHor
                holder.bind(mCategoryList[position])
            }
            VIEW_TYPE_PAGER -> {
                val holder = vh as ViewHolderPager
                holder.bind(mCategoryList[position])
            }
            VIEW_TYPE_GRID -> {
                val holder = vh as ViewHolderGrid
                holder.bind(mCategoryList[position])
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        if (viewType == VIEW_TYPE_HOR) {
            return ViewHolderHor(
                MainCategoryListViewBinding.inflate(layoutInflater, parent, false)
            )
        }
        if (viewType == VIEW_TYPE_PAGER) {
            return ViewHolderPager(
                TrendingPagerListViewBinding.inflate(layoutInflater, parent, false)
            )
        }
        return ViewHolderGrid(
            ProductListViewBinding.inflate(layoutInflater, parent, false)
        )
    }

    private inner class ViewHolderHor(private var applicationBinding: MainCategoryListViewBinding) :
        RecyclerView.ViewHolder(applicationBinding.root) {

        fun bind(feed: Data) {
            if (feed.label.isEmpty()) {
                applicationBinding.text.visibility = View.GONE
            } else {
                applicationBinding.text.text = feed.label
            }
            val homeListAdapter =
                MainCategoriesAdapter(
                    requireActivity,
                    feed.viewList
                )
            val categoryLinearLayoutManager = LinearLayoutManager(requireActivity)
            categoryLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            applicationBinding.recyclerView.layoutManager = categoryLinearLayoutManager
            applicationBinding.recyclerView.adapter = homeListAdapter
            applicationBinding.recyclerView.setHasFixedSize(true)

            applicationBinding.viewAll.setOnClickListener{
                requireActivity.toast("Future Development")
            }
        }
    }

    private inner class ViewHolderPager(private var applicationBinding: TrendingPagerListViewBinding) :
        RecyclerView.ViewHolder(applicationBinding.root) {

        var currentPage = 0
        val DELAY_MS: Long = 2000 //delay in milliseconds before task is to be executed
        val PERIOD_MS: Long = 12000 // time in milliseconds between successive task executions.
        var handler: Handler? = Handler()
        var timer: Timer? = Timer()

        fun bind(feed: Data) {
            if (feed.label.isEmpty()) {
                applicationBinding.text.visibility = View.GONE
            } else {
                applicationBinding.text.text = feed.label
            }
            val mainPagerAdapter =
                TrendingListPagerAdapter(
                    requireActivity,
                    feed.viewList
                )
            applicationBinding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            applicationBinding.viewPager.adapter = mainPagerAdapter
            timer?.schedule(object : TimerTask() { // task to be scheduled
                override fun run() {
                    handler?.post(Runnable {
                        if (feed.viewList.isEmpty())
                            return@Runnable
                        if (currentPage == feed.viewList.size - 1) {
                            currentPage = 0
                        }
                        applicationBinding.viewPager.setCurrentItem(currentPage++, true)
                    })
                }
            }, DELAY_MS, PERIOD_MS)

            applicationBinding.viewAll.setOnClickListener{
                requireActivity.toast("Future Development")
            }
        }
    }

    private inner class ViewHolderGrid(private var applicationBinding: ProductListViewBinding) :
        RecyclerView.ViewHolder(applicationBinding.root) {

        fun bind(feed: Data) {
            if (feed.label.isEmpty()) {
                applicationBinding.text.visibility = View.GONE
            } else {
                applicationBinding.text.text = feed.label
            }
            val homeListAdapter = ProductListAdapter(
                requireActivity,
                feed.viewList
            )
            val categoryLinearLayoutManager = GridLayoutManager(requireActivity, 3)
            categoryLinearLayoutManager.orientation = GridLayoutManager.VERTICAL
            applicationBinding.recyclerView.layoutManager = categoryLinearLayoutManager
            applicationBinding.recyclerView.adapter = homeListAdapter
            applicationBinding.recyclerView.setHasFixedSize(true)

            applicationBinding.viewAll.setOnClickListener{
                requireActivity.toast("Future Development")
            }
        }
    }
}


