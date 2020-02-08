package com.mywings.thieffacedetector

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.mywings.newtwitterapp.process.OnRegistrationListener
import com.mywings.thieffacedetector.process.ProgressDialogUtil
import com.mywings.thieffacedetector.process.RegistrationAsync
import kotlinx.android.synthetic.main.activity_registration.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class RegistrationActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener, OnRegistrationListener {

    private lateinit var progressDialogUtil: ProgressDialogUtil

    private lateinit var latitude: String
    private lateinit var longitude: String

    private var locationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        if (!intent?.extras?.getBoolean("location")!!) {
            return
        }
        progressDialogUtil = ProgressDialogUtil(this)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {

            } else {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    // Marshmallow+
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ),
                        PERMISSION
                    )
                } else {
                    //below Marshmallow
                }

            }
        }

        latitude = "18.538811"
        longitude = "73.831981"

        imgPhoto.setOnClickListener {
            showMenu(it)
        }

        btnRegister.setOnClickListener {
            if (validate()) {
                init()
            } else {
                Toast.makeText(this, "Please fill mandatory fields", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validate(): Boolean = !txtName.text.isNullOrEmpty()
            && !txtPhone.text.isNullOrEmpty()
            && !txtPanCard.text.isNullOrEmpty()
            && !txtUID.text.isNullOrEmpty()
            && !txtDob.text.isNullOrEmpty()
            && imgPhoto.drawable != null

    private fun showMenu(v: View) {
        PopupMenu(this, v).apply {
            setOnMenuItemClickListener(this@RegistrationActivity)
            inflate(R.menu.camera_menu)
            show()
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_camera -> {
                startCamera()
                true
            }
            R.id.menu_gallery -> {
                startGallery()
                true
            }
            else -> false
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE)
    }

    private fun startCamera() {
        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST)
    }

    private fun init() {
        progressDialogUtil.show()
        val registrationAsync = RegistrationAsync()
        val request = JSONObject()
        val params = JSONObject()
        params.put("name", txtName.text)
        params.put("phone", txtPhone.text)
        params.put("pancard", txtPanCard.text)
        params.put("uid", txtUID.text)
        params.put("dob", txtDob.text)
        params.put("latitude", latitude)
        params.put("longitude", longitude)
        val image = (imgPhoto.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val imgFinal = stream.toByteArray()
        params.put("image", Base64.encodeToString(imgFinal, Base64.DEFAULT))
        request.put("request", params)
        registrationAsync.setOnRegistrationListener(this, params)
    }

    companion object {
        const val PERMISSION = 1001
        const val CAMERA_PIC_REQUEST = 1002
        const val SELECT_IMAGE = 1003

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    CAMERA_PIC_REQUEST -> {
                        val image = data!!.extras?.get("data") as Bitmap
                        imgPhoto.setImageBitmap(image)
                    }
                    SELECT_IMAGE -> {
                        val image =
                            MediaStore.Images.Media.getBitmap(contentResolver, data?.data)
                        imgPhoto.setImageBitmap(image)
                    }
                }
            }
        }
    }

    override fun onRegistrationSuccess(success: Int?) {
        progressDialogUtil.hide()
        if (success != null && success <= 0) {
            val intent = Intent(this@RegistrationActivity, SuccessActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Registration successful", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "YOR ARE NOT LEGAL PERSON", Toast.LENGTH_LONG).show()
        }
    }
}
