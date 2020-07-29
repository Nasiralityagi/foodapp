package com.tirade.android.store

import android.content.Context
import android.content.SharedPreferences
import com.android.tlb.Tlb
import com.google.gson.GsonBuilder

private const val PREFS_FILENAME = "tlbPref"

object PrefStoreManager {
    public const val SIGN_IN = "signIn"
    //Shared Preference field used to save and retrieve JSON string
    lateinit var prefs: SharedPreferences

    /**
     * Call this first before retrieving or saving object.
     *
     * @param application Instance of application class
     */
    fun with(application: Tlb) {
        prefs = application.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
    }

    /**
     * Saves object into the Preferences.
     *
     * @param `object` Object of model class (of type [T]) to save
     * @param key Key with which Shared preferences to
     **/
    fun <T> put(`object`: T, key: String) {
        //Convert object to JSON String.
        val jsonString = GsonBuilder().create().toJson(`object`)
        //Save that String in SharedPreferences
        prefs.edit().putString(key, jsonString).apply()
    }

    fun clearIncognito(){
        put({}, SIGN_IN)
    }

    /**
     * Used to retrieve object from the Preferences.
     *
     * @param key Shared Preference key with which object was saved.
     **/
    inline fun <reified T> get(key: String): T? {
        //We read JSON String which was saved.
        val value = prefs.getString(key, null)
        //JSON String was found which means object can be read.
        //We convert this JSON String to model object. Parameter "c" (of
        //type “T” is used to cast.
        return GsonBuilder().create().fromJson(value, T::class.java)
    }

}