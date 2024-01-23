package com.netplus.coremechanism.utils

import android.content.Context

class AppPreferences private constructor(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    companion object {
        val PREFS_NAME = "TallyPref"
        val TOKEN = "token"

        @Volatile
        private var instance: AppPreferences? = null

        fun getInstance(context: Context): AppPreferences {
            return instance ?: synchronized(this) {
                instance ?: AppPreferences(context).also { instance = it }
            }
        }
    }

    fun setStringValue(key:String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    fun getStringValue(key:String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
}