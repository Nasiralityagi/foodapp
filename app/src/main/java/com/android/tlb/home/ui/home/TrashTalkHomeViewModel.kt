package com.android.tlb.home.ui.home

import android.util.Log
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tirade.android.core.trashtalk.home.data.interfaces.TrashTalkHomeListener
import com.tirade.android.core.trashtalk.home.data.model.temp.PostAction
import com.tirade.android.core.trashtalk.home.data.repo.TrashTalkHomeRepository
import com.tirade.android.utils.ApiException
import com.tirade.android.utils.Coroutines
import com.tirade.android.utils.NoInternetException

class TrashTalkHomeViewModel(private val repository: TrashTalkHomeRepository) : ViewModel() {

    var homeListListener: TrashTalkHomeListener? = null

    private var dataListHome: ArrayList<com.android.tlb.home.ui.home.newsfeed.Data> = ArrayList()
    private val mutableHomeList: MutableLiveData<ArrayList<com.android.tlb.home.ui.home.newsfeed.Data>> = MutableLiveData()
    private var dataListNewsFeed: ArrayList<com.android.tlb.home.ui.home.newsfeed.Data> = ArrayList()
    private val mutableNewsFeedList: MutableLiveData<ArrayList<com.android.tlb.home.ui.home.newsfeed.Data>> = MutableLiveData()
    var progress: ObservableInt = ObservableInt(View.GONE)
    var noData: ObservableInt = ObservableInt(View.GONE)

    fun getHomeListItems(): LiveData<ArrayList<com.android.tlb.home.ui.home.newsfeed.Data>>? {
        progress.set(View.VISIBLE)
        noData.set(View.GONE)
        Coroutines.main {
            try {
                val response = repository.fetchHomeData()
                response.let {
                    dataListHome = response.data as ArrayList<com.android.tlb.home.ui.home.newsfeed.Data>
                    // dataList = response as ArrayList<TrashHomeResponse>
                    mutableHomeList.postValue(dataListHome)//notify observable
                    //repository.saveDataForOffline(dataList)//save data for offline
                    progress.set(View.GONE)
                    if(dataListHome.size == 0){
                        noData.set(View.VISIBLE)
                    }
                    return@main
                }
            } catch (e: Exception) {
                homeListListener?.onFailure(e.message!!)
            }
            progress.set(View.GONE)
        }

        return mutableHomeList
    }

    fun getNewsFeedListItems(): LiveData<ArrayList<com.android.tlb.home.ui.home.newsfeed.Data>>? {
        //progress.set(View.VISIBLE)
        //noData.set(View.GONE)
        Coroutines.main {
            try {
                val response = repository.fetchNewsFeedData()
                response.let {
                    dataListNewsFeed = response.data as ArrayList<com.android.tlb.home.ui.home.newsfeed.Data>
                    mutableNewsFeedList.postValue(dataListNewsFeed)//notify observable
                    //progress.set(View.GONE)
                    return@main
                }
            } catch (e: Exception) {
                homeListListener?.onFailure(e.message!!)
            }
        }

        return mutableNewsFeedList
    }

    fun onShoutOutClick () {
        homeListListener?.onShoutOutClick()
    }

    fun onRepresentClick() {
        homeListListener?.onRepresentClick()
    }

    fun onTrashClick() {
        homeListListener?.onTrashClick()
    }

    fun onDebateClick() {
        homeListListener?.onDebateClick()
    }

    fun onWblowerClick() {
        homeListListener?.onWblowerClick()
    }

    fun onJokesClick() {
        homeListListener?.onJokesClick()
    }

    fun onForumsClick() {
        homeListListener?.onForumsClick()
    }

    fun onShameClick() {
        homeListListener?.onShameClick()
    }

}