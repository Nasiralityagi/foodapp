package com.android.tlb.home.ui.notifications

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import com.android.tlb.Tlb
import com.android.tlb.home.data.FragmentHomeCommand
import com.android.tlb.liveobserver.LiveMessageEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class NotificationsViewModel(private val repository: NotificationsRepository) : ViewModel() {

    private var disposable: Disposable? = null
    private var navigationEvent: LiveMessageEvent<FragmentHomeCommand> = LiveMessageEvent()
    var progress: ObservableInt = ObservableInt(View.GONE)
    var noData: ObservableInt = ObservableInt(View.GONE)

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }

    fun getNavigationEvent(): LiveMessageEvent<FragmentHomeCommand> {
        return navigationEvent
    }

    fun getHomeListItems() {
        progress.set(View.VISIBLE)
        noData.set(View.GONE)
        if (Tlb.getInstance().isConnected()) {
            //disposable?.dispose()//dispose pre call
            disposable = repository.fetchHomeData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    navigationEvent.sendEvent { homeFeedList(result) }
                    progress.set(View.GONE)
                    if (result.data!!.isEmpty()) {
                        noData.set(View.VISIBLE)
                    }
                }, { error ->
                    navigationEvent.sendEvent { showToast(error.message!!) }
                })
        } else {
            navigationEvent.sendEvent { showToast("Please connect to Internet!") }
            progress.set(View.GONE)
        }
    }
}