package com.android.tlb.auth.data.listener

interface AuthListener {
    fun pgVisibility(visibility: Int)
    fun showToast(message: String)
    fun onSuccess(message: String)
    fun onFailure(message: String)
    fun onImageSelection()
    fun openDatePicker()
}