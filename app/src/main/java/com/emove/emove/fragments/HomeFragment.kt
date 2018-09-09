package com.emove.emove.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emove.emove.R
import kotlinx.android.synthetic.main.fragment_home.*

@SuppressLint("ValidFragment")
class HomeFragment @SuppressLint("ValidFragment") constructor
(fragmentCallbacks: FragmentCallbacks): BaseFragment(fragmentCallbacks) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onResume() {
        super.onResume()
        setupActions()
    }

    private fun setupActions() {
        bt_start.setOnClickListener { view -> fragmentCallbacks.onStartATripSelected() }
        bt_search.setOnClickListener { view -> fragmentCallbacks.onSearchForRideSelected() }
    }
}