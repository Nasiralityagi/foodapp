package com.android.tlb.home.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.tlb.R
import com.android.tlb.databinding.FragmentCartBinding
import com.android.tlb.databinding.FragmentCollectionsBinding
import com.android.tlb.factory.ViewModelFactory
import com.android.tlb.home.data.FragmentHomeCommand
import com.android.tlb.home.data.model.Data
import com.android.tlb.home.data.model.ViewList
import com.android.tlb.home.ui.home.newsfeed.FeedResponse
import com.android.tlb.utils.Utils
import com.android.tlb.utils.snackBar
import com.android.tlb.utils.toast
import kotlinx.android.synthetic.main.fragment_collections.*

class CartFragment: Fragment(), FragmentHomeCommand {

    private lateinit var viewModel: CartViewModel
    private lateinit var binding: FragmentCartBinding

    private lateinit var cartListAdapter: CartListAdapter

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        // With ViewModelFactory
        viewModel = ViewModelProvider(this@CartFragment, ViewModelFactory()).get(
            CartViewModel::class.java
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

        swipeRefresh.setOnRefreshListener {
            refresh()
        }

        setRecyclerViewHome(getHomeListData()[0].viewList)
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
        setRecyclerViewHome(getHomeListData()[0].viewList)
    }

    private fun getHomeListData(): List<Data> {
        return Utils.getCollectionsList(requireContext())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            refresh()
        }
    }

    private fun setRecyclerViewHome(dataList: List<ViewList>) {
        recycler_view_home.setHasFixedSize(true)
        recycler_view_home.recycledViewPool.setMaxRecycledViews(0, 10)
        recycler_view_home.setItemViewCacheSize(10)
        cartListAdapter = CartListAdapter(requireActivity(), dataList)
        val categoryLinearLayoutManager = LinearLayoutManager(context)
        categoryLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_view_home.layoutManager = categoryLinearLayoutManager
        recycler_view_home.adapter = cartListAdapter
        swipeRefresh.isRefreshing = false
        // recycler_view_home.setHasFixedSize(true)
    }

    companion object {
        private const val categoryName = "param1"
        fun newInstance(param1: String): CartFragment {
            val fragment = CartFragment()
            val args = Bundle()
            args.putString(categoryName, param1)
            fragment.arguments = args
            return fragment
        }
    }

}

