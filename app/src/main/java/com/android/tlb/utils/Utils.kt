package com.android.tlb.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.view.View
import androidx.annotation.IdRes
import com.android.tlb.R
import com.android.tlb.home.data.model.Data
import com.android.tlb.home.data.model.Response
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.util.regex.Pattern

class Utils {
    companion object {

        /** To make activity full screen*/
        @SuppressLint("InlinedApi")
        fun makeImmersive(decorView: View) {
            try {
                val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        // or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        // or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hidePlayerController nav bar
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_FULLSCREEN // hidePlayerController status bar
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
                decorView.systemUiVisibility = flags
            } catch (e: Exception) {
            }
        }


        /**The consequence of doing this is that we don’t need to call the function from the onCreate callback now*/
        fun <T : View> Activity.bind(@IdRes idRes: Int): Lazy<T> {
            @Suppress("UNCHECKED_CAST")
            return unsafeLazy { findViewById<T>(idRes) as T }
        }

        fun <T : View> View.bind(@IdRes idRes: Int): Lazy<T> {
            @Suppress("UNCHECKED_CAST")
            return unsafeLazy { findViewById<T>(idRes) }
        }

        private fun <T> unsafeLazy(initializer: () -> T) = lazy(
            LazyThreadSafetyMode.NONE,
            initializer
        )//Lazy<T>, which means it won’t be initialised right away but the first time the value is actually needed

        /**Handler for making task delay*/
        fun handlerDelay(delay: Long, process: () -> Unit) {
            Handler().postDelayed({
                process()
            }, delay)
        }

        /**Handler delay task If you need something to run on UI thread*/
        fun handlerDelayOnMainThread(delay: Long, activity: Activity, process: () -> Unit) {
            Handler().postDelayed({
                activity.runOnUiThread {
                    Runnable {
                        process()
                    }
                }
            }, delay)
        }

        /**Check email validation*/
        fun isEmailValid(email: CharSequence): Boolean {
            val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(email)
            return matcher.matches()
        }

        fun getHomeList(context: Context): List<Data> {
            val jsonString: String = getJSONStringFromRaw(context, R.raw.home)
            val listType: Type = object : TypeToken<Response?>() {}.type
            val apiResponseMessage: Response = Gson().fromJson(jsonString, listType)
            return apiResponseMessage.data
        }

        fun getCollectionsList(context: Context): List<Data> {
            val jsonString: String = getJSONStringFromRaw(context, R.raw.collection)
            val listType: Type = object : TypeToken<Response?>() {}.type
            val apiResponseMessage: Response = Gson().fromJson(jsonString, listType)
            return apiResponseMessage.data
        }

        private fun getJSONStringFromRaw(context: Context, rawId: Int): String {
            val content: InputStream = context.resources.openRawResource(rawId)
            val buffer = BufferedReader(InputStreamReader(content))
            val respString = StringBuilder()
            try {
                var s: String? = ""
                while (buffer.readLine().also { s = it } != null) {
                    respString.append(s)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return respString.toString()
        }

    }
}