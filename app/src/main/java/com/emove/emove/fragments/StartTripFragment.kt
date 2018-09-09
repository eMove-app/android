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


@SuppressLint("ValidFragment")
class StartTripFragment @SuppressLint("ValidFragment") constructor
(fragmentCallbacks: FragmentCallbacks) : BaseFragment(fragmentCallbacks) {

    val GET_LOCATION = 1

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
    }

    private fun getStartTripBody(): StartTripBody {
        return StartTripBody(Position(44.489163, 26.125570), Position(44.428907, 26.104237), "9:00:00")
    }

    private fun startTrip() {
        val token = StorageController.readToken(context!!)
        token?.let {
            showLoading()
            EmoveApi.instance.startTrip(it, getStartTripBody()).enqueue(object: Callback<ResponseBody> {

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    hideLoading()
                    startActivityForResult(Intent(context, ChooseLocationActivity::class.java), GET_LOCATION)
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    hideLoading()
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Check which request we're responding to
        if (requestCode == GET_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {

            }
        }
    }

}