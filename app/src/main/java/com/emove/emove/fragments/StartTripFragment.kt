package com.emove.emove.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emove.emove.R
import com.emove.emove.model.GetUserResponse
import com.emove.emove.model.Position
import com.emove.emove.model.StartTripBody
import com.emove.emove.retrofit.EmoveApi
import com.emove.emove.storage.StorageController
import kotlinx.android.synthetic.main.fragment_start_trip.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import com.emove.emove.activities.ChooseLocationActivity
import com.emove.emove.activities.MapsActivity
import com.emove.emove.model.SearchResultsResponse
import com.emove.emove.storage.InMemoryStorage
import com.google.android.gms.maps.model.LatLng


@SuppressLint("ValidFragment")
class StartTripFragment @SuppressLint("ValidFragment") constructor
(fragmentCallbacks: FragmentCallbacks) : BaseFragment(fragmentCallbacks) {

    val GET_LOCATION_HOME = 1
    val GET_LOCATION_WORK = 2

    companion object {
        val LAT = "lat"
        val LNG = "lng"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_trip, container, false)
    }

    override fun onResume() {
        super.onResume()
        setupActions()
    }

    private fun setupActions() {
        bt_start.setOnClickListener { view -> startTrip() }
        et_starting_position.setOnClickListener { _ -> startActivityForResult(Intent(context, ChooseLocationActivity::class.java), GET_LOCATION_HOME) }
        et_destination.setOnClickListener { _ -> startActivityForResult(Intent(context, ChooseLocationActivity::class.java), GET_LOCATION_WORK) }
    }

    private fun getStartTripBody(): StartTripBody {
        return StartTripBody(Position(InMemoryStorage.lastWorkSaved!!.latitude, InMemoryStorage.lastWorkSaved!!.longitude), Position(InMemoryStorage.lastWorkSaved!!.latitude, InMemoryStorage.lastWorkSaved!!.longitude), "9:00:00")
    }

    private fun startTrip() {
        val token = StorageController.readToken(context!!)
        token?.let {
            showLoading()
            EmoveApi.instance.startTrip(it, getStartTripBody()).enqueue(object: Callback<SearchResultsResponse> {

                override fun onResponse(call: Call<SearchResultsResponse>, response: Response<SearchResultsResponse>) {
                    response.body()?.let {
                        StorageController.saveLastSearchResult(it.data[0])
                        startActivity(Intent(context, MapsActivity::class.java))
                    }
                    hideLoading()

                }
                override fun onFailure(call: Call<SearchResultsResponse>, t: Throwable) {
                    hideLoading()
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Check which request we're responding to
        if (requestCode == GET_LOCATION_HOME) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    val lat = data.getDoubleExtra(LAT, 0.0)
                    val lng = data.getDoubleExtra(LNG, 0.0)
                    if (lat != null && lng != null) {
                        InMemoryStorage.lastHomeSaved = LatLng(lat, lng)
                    }
                }
            }
        } else if (requestCode == GET_LOCATION_WORK) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    val lat = data.getDoubleExtra(LAT, 0.0)
                    val lng = data.getDoubleExtra(LNG, 0.0)
                    if (lat != null && lng != null) {
                        InMemoryStorage.lastWorkSaved = LatLng(lat, lng)
                    }
                }
            }
        }
    }

}