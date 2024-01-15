package com.netplus.coremechanism.utils

import android.content.Context

class AppPreferences private constructor(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    companion object {
        private const val PREFS_NAME = "TallyPref"
        private const val TOKEN = "token"

        @Volatile
        private var instance: AppPreferences? = null

        fun getInstance(context: Context): AppPreferences {
            return instance ?: synchronized(this) {
                instance ?: AppPreferences(context).also { instance = it }
            }
        }
    }

    fun setStringValue(value: String) {
        editor.putString(TOKEN, value)
        editor.apply()
    }

    fun getStringValue(defaultValue: String): String {
        return sharedPreferences.getString(TOKEN, defaultValue) ?: defaultValue
    }
}