package com.mywings.thieffacedetector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mywings.newtwitterapp.process.LoginAsync
import com.mywings.newtwitterapp.process.OnLoginListener
import com.mywings.thieffacedetector.model.User
import com.mywings.thieffacedetector.process.OnLocationProviderListener
import com.mywings.thieffacedetector.process.ProgressDialogUtil
import com.mywings.thieffacedetector.process.ValidateLocation
import kotlinx.android.synthetic.main.activity_login.*


import org.json.JSONObject

class MainActivity : AppCompatActivity(), OnLoginListener, OnLocationProviderListener {


    private lateinit var progressDialogUtil: ProgressDialogUtil
    private var location: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressDialogUtil = ProgressDialogUtil(this@MainActivity)
        btnSignIn.setOnClickListener {
            if (validate()) {
                initLogin()
            } else {
                Toast.makeText(this@MainActivity, "Enter username and password", Toast.LENGTH_LONG).show()
            }
        }

        btnSignUp.setOnClickListener {
            if (validate()) {
                val intent = Intent(this@MainActivity, RegistrationActivity::class.java)
                intent.putExtra("location", location)
                startActivity(intent)
            }
        }
        locationFinder()
    }

    private fun locationFinder() {
        val locationUtil = ValidateLocation()
        locationUtil.setLocationListener(this)
    }

    private fun initLogin() {
        progressDialogUtil.show()
        var loginAsync = LoginAsync()
        var request = JSONObject()
        var param = JSONObject()
        param.put("Username", txtUsername.text)
        param.put("Password", txtPassword.text)
        request.put("request", param)
        loginAsync.setOnLoginListener(this, request)
    }

    private fun validate(): Boolean = location

    override fun onLoginSuccess(user: User?) {

    }

    override fun onLocationSuccess(result: Boolean?) {
        location = result!!
    }

}
