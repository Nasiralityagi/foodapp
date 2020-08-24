package com.android.tlb.home.ui.notifications

class NotificationsRepository {

    companion object {
        fun getInstance(): NotificationsRepository {
            val mInstance: NotificationsRepository by lazy { NotificationsRepository() }
            return mInstance
        }
    }
}