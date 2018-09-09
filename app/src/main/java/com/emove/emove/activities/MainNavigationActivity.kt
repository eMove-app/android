package com.emove.emove.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.emove.emove.R
import com.emove.emove.fragments.*
import kotlinx.android.synthetic.main.activity_main_navigation.*

class MainNavigationActivity : AppCompatActivity(), FragmentCallbacks {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                switchFragment(HomeFragment(this))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                switchFragment(UserFragment(this))
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun switchFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)

// Commit the transaction
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_navigation)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        switchFragment(HomeFragment(this))
    }

    override fun onSearchForRideSelected() {
        switchFragment(SearchFragment(this))
    }

    override fun onStartATripSelected() {
        switchFragment(StartTripFragment(this))
    }

    override fun onTripStarted() {
        startActivity(Intent(this, MapsActivity::class.java))
    }

    override fun onSearch() {
        switchFragment(SearchResultsFragment(this))
    }
}
