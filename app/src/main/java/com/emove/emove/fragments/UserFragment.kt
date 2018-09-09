package com.emove.emove.fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide

import com.emove.emove.R
import com.emove.emove.model.GetUserResponse
import com.emove.emove.model.User
import com.emove.emove.retrofit.EmoveApi
import com.emove.emove.storage.StorageController
import kotlinx.android.synthetic.main.fragment_user.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@SuppressLint("ValidFragment")
class UserFragment @SuppressLint("ValidFragment") constructor
(fragmentCallbacks: FragmentCallbacks) : BaseFragment(fragmentCallbacks) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onResume() {
        super.onResume()
        setupActions()
        retrieveUserDetails();
    }

    private fun setupActions() {
        sw_driver.setOnCheckedChangeListener { compoundButton, checked -> updateCarExtraLayoutVisibility(checked) }
    }

    private fun updateUI(user: User) {
        Glide.with(this).load(user.profile_picture_url).into(iv_picture);
        tv_name.text = user.name
        user.addresses?.let {
            val homeAddresses = it.filter { it.type.equals("home") }
            if (homeAddresses.isNotEmpty()) {
                et_home_address.setText(homeAddresses.get(0).name)
            }
        }
        user.addresses?.let {
            val homeAddresses = it.filter { it.type.equals("work") }
            if (homeAddresses.isNotEmpty()) {
                et_work_address.setText(homeAddresses.get(0).name)
            }
        }
        et_phone.setText(user.phone)
        sw_driver.isChecked = user.isDriver
        updateCarExtraLayoutVisibility(user.isDriver)
        user.car?.let {
            et_make.setText(it.make)
            et_model.setText(it.model)
            et_color.setText(it.color)
            et_registration.setText(it.registration_number)
            et_seats.setText(it.seats)
            et_extra.setText(it.extra_info)
        }
    }

    private fun updateCarExtraLayoutVisibility(show: Boolean) {
        rl_car_extra.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun retrieveUserDetails() {
        val token = StorageController.readToken(context!!)
        token?.let {
            showLoading()
            EmoveApi.instance.getUser(it).enqueue(object: Callback<GetUserResponse> {

                override fun onResponse(call: Call<GetUserResponse>, response: Response<GetUserResponse>) {
                    hideLoading()
                    val body = response.body()
                    body?.let {
                        updateUI(body.data) }
                }
                override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                    hideLoading()
                }
            })
        }
    }

    private fun saveUserDetails(user: User) {
        val token = StorageController.readToken(context!!)
        token?.let {
            showLoading()
            EmoveApi.instance.updateUser(it, user).enqueue(object: Callback<ResponseBody> {

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    hideLoading()
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    hideLoading()
                }
            })
        }
    }
}
