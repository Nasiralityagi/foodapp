package com.android.tlb.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.tlb.auth.data.repository.AuthRepository
import com.android.tlb.auth.data.viewmodel.LoginViewModel
import com.android.tlb.auth.data.viewmodel.SignUpViewModel
import com.android.tlb.home.ui.cart.CartRepository
import com.android.tlb.home.ui.cart.CartViewModel
import com.android.tlb.home.ui.collections.CollectionsRepository
import com.android.tlb.home.ui.collections.CollectionsViewModel
import com.android.tlb.home.ui.discover.DiscoverRepository
import com.android.tlb.home.ui.discover.DiscoverViewModel
import com.android.tlb.home.ui.home.HomeRepository
import com.android.tlb.home.ui.home.HomeViewModel
import com.android.tlb.home.ui.notifications.NotificationsRepository
import com.android.tlb.home.ui.notifications.NotificationsViewModel
import com.android.tlb.home.ui.profile.ProfileRepository
import com.android.tlb.home.ui.profile.ProfileViewModel

class ViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(AuthRepository.getInstance()) as T
        } else return if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            SignUpViewModel(AuthRepository.getInstance()) as T
        } else return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(HomeRepository.getInstance()) as T
        }else return if (modelClass.isAssignableFrom(DiscoverViewModel::class.java)) {
            DiscoverViewModel(DiscoverRepository.getInstance()) as T
        }else return if (modelClass.isAssignableFrom(CollectionsViewModel::class.java)) {
            CollectionsViewModel(CollectionsRepository.getInstance()) as T
        }else return if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            NotificationsViewModel(NotificationsRepository.getInstance()) as T
        }else return if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            ProfileViewModel(ProfileRepository.getInstance()) as T
        }else return if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            CartViewModel(CartRepository.getInstance()) as T
        } else {
            //TODO for others view model repository
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
