package com.android.tlb.auth.data.repository

import com.android.tlb.auth.data.model.AuthResponse
import com.android.tlb.network.WebCall
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
class AuthRepository {

    /**The singleton BackEndApi object that is created lazily when the first time it is used
     * After that it will be reused without creation
     */
    private val apiService by lazy { WebCall.create() }

    // in-memory cache of the loggedInUser object
    private var user: AuthResponse? = null

    val isLoggedIn: Boolean get() = user != null

    fun logout() {
        user = null
        //TODO clear user data when logged out
    }

    fun setLoggedInUser(user: AuthResponse) {
        this.user = user
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        //TODO save user data either in pref or in db
    }

    fun signUp(params: HashMap<String, RequestBody>, fileBody: MultipartBody.Part): Observable<AuthResponse> {
        return apiService.signUpAuth("token", params, fileBody)
    }

    fun login(email: String, password: String): Observable<AuthResponse> {
        return apiService.loginAuth(email, password)
    }

    companion object {
        fun getInstance(): AuthRepository {
            val mInstance: AuthRepository by lazy { AuthRepository() }//by lazy single object created for this class
            return mInstance
        }
    }

}
