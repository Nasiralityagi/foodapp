package com.android.tlb.home.data

import com.android.tlb.home.ui.home.newsfeed.FeedResponse

interface FragmentHomeCommand {
    fun onFailure(message: String)
    fun onCategoryClick(position: Int)
    fun showToast(message: String)
    fun newsFeedList(response: FeedResponse)
    fun homeFeedList(response: FeedResponse)
}