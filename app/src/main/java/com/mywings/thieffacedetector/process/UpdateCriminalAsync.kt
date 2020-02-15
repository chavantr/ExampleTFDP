package com.mywings.thieffacedetector.process

import android.os.AsyncTask
import com.mywings.newtwitterapp.process.HttpConstants
import com.mywings.newtwitterapp.process.OnUpdateCriminalListener
import org.json.JSONObject

class UpdateCriminalAsync : AsyncTask<JSONObject, Void, Int?>() {

    private val httpConnectionUtil = HttpConnectionUtil()
    private lateinit var onRegistrationListener: OnUpdateCriminalListener

    override fun doInBackground(vararg params: JSONObject?): Int? {
        var response: String? = null
        response = httpConnectionUtil.requestPost(HttpConstants.LOCATION_URL.plus(HttpConstants.FACE_DETECT), params[0])
        response.let {
            return if (it != null && !it.isNullOrEmpty()) {
                it.toInt()
            } else 0
        }
    }



    override fun onPostExecute(result: Int?) {
        super.onPostExecute(result)
        onRegistrationListener.onUpdateSuccess(result)
    }

    fun setOnUpdateListener(onRegistrationListener: OnUpdateCriminalListener, request: JSONObject) {
        this.onRegistrationListener = onRegistrationListener
        super.executeOnExecutor(THREAD_POOL_EXECUTOR, request)
    }
}