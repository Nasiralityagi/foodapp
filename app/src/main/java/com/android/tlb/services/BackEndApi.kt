package com.android.tlb.services

import com.android.tlb.auth.data.model.AuthResponse
import com.android.tlb.home.ui.home.newsfeed.FeedResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface BackEndApi {
    /*@POST("login")
    @Headers("Content-Type: application/json")
    fun loginAuth(@Body login: String): Response<AuthResponse>*///retro generic response

    @FormUrlEncoded
    @POST("login")
     fun loginAuth(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Observable<AuthResponse>//rx generic response

   /* @FormUrlEncoded
    @POST("signup")
     fun signUpAuth(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : Observable<AuthResponse>*/

    @Multipart
    @POST("signup")
    fun signUpAuth(
        @Header("Authorization") authorization: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part fileBody: MultipartBody.Part
    ): Observable<AuthResponse>

    @GET("incognito/postdata/getdata")
    fun fetchHomeDataList(): Observable<FeedResponse>

    @GET("incognito/postdata/get_news_feed")
    fun fetchPagerDataList(): Observable<FeedResponse>
}