package com.android.tlb.home.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.tlb.R
import com.android.tlb.databinding.FragmentHomeBinding
import com.android.tlb.factory.ViewModelFactory
import com.android.tlb.home.HomeActivity
import com.android.tlb.home.data.FragmentHomeCommand
import com.android.tlb.home.data.model.Data
import com.android.tlb.home.ui.home.newsfeed.FeedResponse
import com.android.tlb.utils.Utils
import com.android.tlb.utils.snackBar
import com.android.tlb.utils.toast
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), FragmentHomeCommand, HomeActivity.SearchListener {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    private lateinit var homeListAdapter: HomeListAdapter

    override fun onFailure(message: String) {
        progress_bar.hide()
        root_layout.snackBar(message)
    }

    override fun onCategoryClick(position: Int) {
        //context?.toast("Position $position")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        // With ViewModelFactory
        viewModel = ViewModelProvider(this@HomeFragment, ViewModelFactory()).get(
            HomeViewModel::class.java
        )

        binding.viewModel = viewModel
        registerObservables()

        return binding.root
    }

    /**With this No observer needed in activity*/
    private fun registerObservables() {
        viewModel.getNavigationEvent().setEventReceiver(this, this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        cross.setOnClickListener {
            etSearch.setText("")
            searchView.visibility = View.GONE
        }
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // homeListAdapter.filter.filter(etSearch.text.toString())
            }
        })

        (activity as HomeActivity?)!!.registerSearchListener(this)

        swipeRefresh.setOnRefreshListener {
            refresh()
        }

        setRecyclerViewHome(getHomeListData())
    }

    override fun showToast(message: String) {
        context?.toast(message, false)
    }

    override fun newsFeedList(response: FeedResponse) {
        response.apply {
            // setRecyclerViewBannerFeed(this.data as ArrayList<Data>)
        }
    }

    override fun homeFeedList(response: FeedResponse) {
        response.apply {
            //setRecyclerViewHome(this.data as ArrayList<com.android.tlb.home.ui.home.newsfeed.Data>)
        }
    }

    private fun refresh() {
        swipeRefresh.isRefreshing = true
        setRecyclerViewHome(getHomeListData())
    }

    private fun getHomeListData(): List<Data> {
        return Utils.getHomeList(requireContext())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            refresh()
        }
    }

    private fun setRecyclerViewHome(dataList: List<Data>) {
        recycler_view_home.setHasFixedSize(true)
        recycler_view_home.recycledViewPool.setMaxRecycledViews(0, 10)
        recycler_view_home.setItemViewCacheSize(10)
        homeListAdapter =
            HomeListAdapter(this, requireActivity())
        val categoryLinearLayoutManager = LinearLayoutManager(context)
        categoryLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_view_home.layoutManager = categoryLinearLayoutManager
        homeListAdapter.setAppList(dataList)
        recycler_view_home.adapter = homeListAdapter
        swipeRefresh.isRefreshing = false
        // recycler_view_home.setHasFixedSize(true)
    }

    companion object {
        private const val categoryName = "param1"
        fun newInstance(param1: String): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString(categoryName, param1)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onSearchClick() {
        searchView.visibility = View.VISIBLE
    }
}
