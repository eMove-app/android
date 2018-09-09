package com.emove.emove.activities

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.login.LoginManager
import com.facebook.CallbackManager
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.emove.emove.R
import com.emove.emove.model.LoginResponse
import com.emove.emove.retrofit.EmoveApi
import com.emove.emove.storage.StorageController
import com.facebook.AccessToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private val EMAIL = "email"
    private val REQUEST_LOCATION = 1;
    val callbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermission()
        setupFacebookButton()
    }

    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //do nothing
            } else {
//                Toast.makeText(this@LoginActivity, "The app can't work without location services", Toast.LENGTH_SHORT).show()
                requestPermission()
            }
        }
    }

    fun setupFacebookButton() {

        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        if (!isLoggedIn) {

//        val loginButton = findViewById<View>(R.id.login_button) as LoginButton
//        loginButton.setReadPermissions(Arrays.asList(EMAIL))
//        // If you are using in a fragment, call loginButton.setFragment(this);
//
//        // Callback registration
//        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
//            override fun onSuccess(loginResult: LoginResult) {
//                // App code
//            }
//
//            override fun onCancel() {
//                // App code
//            }
//
//            override fun onError(exception: FacebookException) {
//                // App code
//            }
//        })

            LoginManager.getInstance().registerCallback(callbackManager,
                    object : FacebookCallback<LoginResult> {
                        override fun onSuccess(loginResult: LoginResult) {
                            Toast.makeText(this@LoginActivity, "onSuccess", Toast.LENGTH_SHORT).show()
                            login(loginResult.accessToken.token)
                            startActivity(Intent(this@LoginActivity, MainNavigationActivity::class.java))
                        }

                        override fun onCancel() {
                            Toast.makeText(this@LoginActivity, "onCancel", Toast.LENGTH_SHORT).show()
                        }

                        override fun onError(exception: FacebookException) {
                            Toast.makeText(this@LoginActivity, "onError", Toast.LENGTH_SHORT).show()
                        }
                    })
        } else {
            login(accessToken.token)
            finish()
            startActivity(Intent(this, MainNavigationActivity::class.java))
            this.overridePendingTransition(0, 0);

        }
    }

    fun login (facebookToken: String) {
        val token = StorageController.readToken(this)
        if (token == null) {
            EmoveApi.instance.login(facebookToken).enqueue(object : Callback<LoginResponse> {

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    Toast.makeText(this@LoginActivity, "login succes", Toast.LENGTH_SHORT).show()
                    response.body()?.let { StorageController.saveToken(this@LoginActivity, it.token) }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "login failed", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

}
