package com.android.tlb.home.ui.discover

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DiscoverViewModel(private val repository: DiscoverRepository) : ViewModel() {
    var progress: ObservableInt = ObservableInt(View.GONE)
    var noData: ObservableInt = ObservableInt(View.GONE)

}