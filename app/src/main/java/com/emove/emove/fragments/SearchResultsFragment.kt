package com.emove.emove.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emove.emove.R
import com.emove.emove.activities.MapsActivity
import com.emove.emove.adapters.SearchResultsAdapter
import com.emove.emove.model.SearchResult
import com.emove.emove.model.SearchResultsResponse
import com.emove.emove.retrofit.EmoveApi
import com.emove.emove.storage.InMemoryStorage
import com.emove.emove.storage.StorageController
import kotlinx.android.synthetic.main.fragment_search_results.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@SuppressLint("ValidFragment")
class SearchResultsFragment @SuppressLint("ValidFragment") constructor
(fragmentCallbacks: FragmentCallbacks) : BaseFragment(fragmentCallbacks) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_results, container, false)
    }

    override fun onResume() {
        super.onResume()
        rv_search_results.layoutManager = LinearLayoutManager(context)
        findRide()
    }

    private fun setupList(searchResults: List<SearchResult>) {
        rv_search_results.adapter = SearchResultsAdapter(searchResults, listener = object: SearchResultsAdapter.SearchResultsListener {
            override fun onItemSelected(searchResult: SearchResult) {
                StorageController.saveLastSearchResult(searchResult)
                startActivity(Intent(context, MapsActivity::class.java))
            }
        })
    }

    private fun findRide() {
        val token = StorageController.readToken(context!!)
        token?.let {
            showLoading()
            var lat = 44.458561
            var lng = 26.110557
            InMemoryStorage.lastDestinationSaved?.let {
                lat = it.latitude
                lng = it.longitude
            }
            EmoveApi.instance.findTrip(it, lat, lng).enqueue(object: Callback<SearchResultsResponse> {

                override fun onResponse(call: Call<SearchResultsResponse>, response: Response<SearchResultsResponse>) {
                    hideLoading()
                    val body = response.body()
                    body?.let { setupList(body.data) }
                }
                override fun onFailure(call: Call<SearchResultsResponse>, t: Throwable) {
                    hideLoading()
                }
            })
        }
    }

}