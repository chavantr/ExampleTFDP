package com.mywings.thieffacedetector.process

import android.os.AsyncTask
import com.mywings.newtwitterapp.process.HttpConstants

class ValidateLocation : AsyncTask<String, Void, Boolean>() {

    private lateinit var onLocationProviderListener: OnLocationProviderListener
    private val httpConnectionUtil = HttpConnectionUtil()

    override fun doInBackground(vararg params: String?): Boolean {
        return httpConnectionUtil.requestGet(HttpConstants.LOCATION_URL.plus(HttpConstants.LOCATION_UTIL).plus(params[0]))
            ?.toBoolean()!!
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        onLocationProviderListener.onLocationSuccess(result)
    }

    fun setLocationListener(onLocationProviderListener: OnLocationProviderListener) {
        this.onLocationProviderListener = onLocationProviderListener
        super.executeOnExecutor(THREAD_POOL_EXECUTOR, "?id=".plus(HttpConstants.LOCATION_NAME))
    }

}