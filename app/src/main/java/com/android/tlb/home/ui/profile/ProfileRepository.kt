package com.android.tlb.home.ui.profile


class ProfileRepository {

    companion object {
        fun getInstance(): ProfileRepository {
            val mInstance: ProfileRepository by lazy { ProfileRepository() }
            return mInstance
        }
    }
}