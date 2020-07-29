package com.android.tlb.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.tlb.auth.data.repository.AuthRepository
import com.android.tlb.auth.data.viewmodel.LoginViewModel
import com.android.tlb.auth.data.viewmodel.SignUpViewModel

class ViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(AuthRepository.getInstance()) as T
        } else return if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            SignUpViewModel(AuthRepository.getInstance()) as T
        } else {
            //TODO for others view model repository
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
