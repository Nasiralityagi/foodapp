package com.android.tlb.home.ui.discover


class DiscoverRepository {

    companion object {
        fun getInstance(): DiscoverRepository {
            val mInstance: DiscoverRepository by lazy { DiscoverRepository() }
            return mInstance
        }
    }
}