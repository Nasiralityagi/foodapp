package com.android.tlb.home.ui.home

import com.android.tlb.network.WebCall
import com.tirade.android.core.trashtalk.home.data.model.home.PostActionResponse
import com.tirade.android.core.trashtalk.home.data.model.home.TrashHomeResponse
import com.tirade.android.core.trashtalk.home.data.model.newsfeed.NewsFeedResponse
import com.tirade.android.core.trashtalk.home.data.model.temp.PostAction
import com.tirade.android.network.TiradeApi
import com.tirade.android.network.TiradeClient


class TrashTalkHomeRepository {

    /**The singleton BackEndApi object that is created lazily when the first time it is used
     * After that it will be reused without creation
     */
    private val apiServices by lazy { WebCall.create() }

     fun fetchHomeData(): TrashHomeResponse {
        return apiServices.getTrashTalkHome()
    }

     fun fetchNewsFeedData(): NewsFeedResponse {
        val response = apiServices.getTrashTalkNewsFeed()
        return response
    }

    companion object {
        fun getInstance(): TrashTalkHomeRepository {
            val mInstance: TrashTalkHomeRepository by lazy { TrashTalkHomeRepository() }
            return mInstance
        }
    }
}