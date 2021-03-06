package com.android.tlb.home.ui.home

import com.android.tlb.home.ui.home.newsfeed.FeedResponse
import com.android.tlb.network.WebCall
import io.reactivex.Observable


class HomeRepository {

    /**The singleton BackEndApi object that is created lazily when the first time it is used
     * After that it will be reused without creation
     */
    private val apiServices by lazy { WebCall.create() }

     fun fetchHomeData(): Observable<FeedResponse> {
        return apiServices.fetchHomeDataList()
    }

     fun fetchNewsFeedData(): Observable<FeedResponse> {
        return apiServices.fetchPagerDataList()
    }

    companion object {
        fun getInstance(): HomeRepository {
            val mInstance: HomeRepository by lazy { HomeRepository() }
            return mInstance
        }
    }
}