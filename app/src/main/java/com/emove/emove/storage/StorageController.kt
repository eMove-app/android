package com.emove.emove.storage

import android.content.Context
import com.emove.emove.model.SearchResult

object StorageController {

    val sharedPrefsStorage: SharedPrefsStorage = SharedPrefsStorage()

    fun saveToken(context: Context, token: String) {
        sharedPrefsStorage.saveToken(context, token)
    }

    fun readToken(context: Context): String? {
        return sharedPrefsStorage.readToken(context)
    }

    fun saveLastSearchResult(searchResult: SearchResult) {
        InMemoryStorage.searchResult =  searchResult
    }

    fun getLastSearchResult(): SearchResult? {
        return InMemoryStorage.searchResult
    }
}