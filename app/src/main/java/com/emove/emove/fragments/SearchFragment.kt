package com.emove.emove.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emove.emove.R
import com.emove.emove.activities.ChooseLocationActivity
import com.emove.emove.fragments.StartTripFragment.Companion.GET_LOCATION_HOME
import com.emove.emove.fragments.StartTripFragment.Companion.GET_LOCATION_WORK
import com.emove.emove.fragments.StartTripFragment.Companion.LAT
import com.emove.emove.fragments.StartTripFragment.Companion.LNG
import com.emove.emove.storage.InMemoryStorage
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_search.*


@SuppressLint("ValidFragment")
class SearchFragment @SuppressLint("ValidFragment") constructor
(fragmentCallbacks: FragmentCallbacks) : BaseFragment(fragmentCallbacks) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onResume() {
        super.onResume()
        setupActions()
    }

    private fun setupActions() {
        bt_search.setOnClickListener { view -> fragmentCallbacks.onSearch() }
        et_starting_position.setOnClickListener { _ -> startActivityForResult(Intent(context, ChooseLocationActivity::class.java), GET_LOCATION_HOME) }
        et_destination.setOnClickListener { _ -> startActivityForResult(Intent(context, ChooseLocationActivity::class.java), GET_LOCATION_WORK) }
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
                        InMemoryStorage.lastDestinationSaved = LatLng(lat, lng)
                    }
                }
            }
        }
    }
}