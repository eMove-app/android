package com.emove.emove.storage

import android.content.Context

class SharedPrefsStorage {

    private val SHARED_PREFS_FILE = "emove_shared_prefs"
    private val TOKEN = "emove_token"

    private fun saveString(context: Context, key: String, value: String?) {
        context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .commit()
    }

    private fun readString(context: Context, key: String): String? {
        return context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
                .getString(key, null)
    }

    fun saveToken(context: Context, token: String) {
        saveString(context, TOKEN, token)
    }

    fun readToken(context: Context): String? {
        return readString(context, TOKEN)
    }

}