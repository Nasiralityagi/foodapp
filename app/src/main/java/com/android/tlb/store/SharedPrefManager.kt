package com.android.tlb.store

import android.content.Context

/**
 * Created by LENOVO on 1/30/2018.
 */
object SharedPrefManager {

    operator fun set(context: Context, key: String, `object`: Any) {
        val sp = context.getSharedPreferences(context.packageName,
                Context.MODE_PRIVATE)
        val edit = sp.edit()
        when (`object`.javaClass.simpleName) {
            "String" -> edit.putString(key, `object` as String)
            "Integer" -> edit.putInt(key, `object` as Int)
            "Boolean" -> edit.putBoolean(key, `object` as Boolean)
            "Float" -> edit.putFloat(key, `object` as Float)
            "Long" -> edit.putLong(key, `object` as Long)
        }
        edit.apply()
    }

    operator fun get(context: Context, key: String, defaultObject: Any): Any? {
        val sp = context.getSharedPreferences(context.packageName,
                Context.MODE_PRIVATE)
        when (defaultObject.javaClass.simpleName) {
            "String" -> return sp.getString(key, defaultObject as String)
            "Integer" -> return sp.getInt(key, defaultObject as Int)
            "Boolean" -> return sp.getBoolean(key, defaultObject as Boolean)
            "Float" -> return sp.getFloat(key, defaultObject as Float)
            "Long" -> return sp.getLong(key, defaultObject as Long)
        }
        return null
    }

    fun cleanAll(context: Context) {
        val sp = context.getSharedPreferences(context.packageName,
                Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.clear()
        editor.apply()
    }
}