package com.android.tlb.auth.data.listener

import android.app.Activity
import com.android.tlb.auth.data.model.AuthResponse

interface LoginActivityCommand {
    fun showToast(message: String)
    fun loginData(data: AuthResponse)
    fun pgVisibility(visibility: Int)
    fun setRememberMe(isRemember: Boolean)
    fun setError(isError: Boolean,inputType: String)
    fun loginFB()
    fun loginGoogle()
    fun <T> openActivity(activity: Class<T>)
}