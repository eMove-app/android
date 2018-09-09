package com.emove.emove.fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.support.v4.app.Fragment

@SuppressLint("ValidFragment")
open class BaseFragment(val fragmentCallbacks: FragmentCallbacks) : Fragment() {

    private var progress: ProgressDialog? = null

    private fun setupLoading() {
        progress = ProgressDialog(context)
        progress?.let {
            it.setTitle("Loading")
            it.setMessage("Please wait...")
            it.setCancelable(false) // disable dismiss by tapping outside of the dialog
        }
    }

    protected fun showLoading() {
        progress?.let {
            if (!it.isShowing()) {
                it.show()
            }
        }
    }

    protected fun hideLoading() {
        progress?.let {
            if (it.isShowing()) {
                it.dismiss()
            }
        }
    }
}