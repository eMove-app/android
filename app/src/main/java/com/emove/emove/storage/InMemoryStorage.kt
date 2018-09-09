package com.emove.emove.storage

import com.emove.emove.model.SearchResult
import com.google.android.gms.maps.model.LatLng

object InMemoryStorage {

    var lastTripId: Int? = null
    var searchResult: SearchResult? = null
    var lastHomeSaved: LatLng? = null
    var lastWorkSaved: LatLng? = null
}