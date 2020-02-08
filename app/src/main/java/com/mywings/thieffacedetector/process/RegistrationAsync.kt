package com.mywings.thieffacedetector.process

import android.os.AsyncTask
import com.mywings.newtwitterapp.process.HttpConstants
import com.mywings.newtwitterapp.process.OnRegistrationListener
import org.json.JSONObject

class RegistrationAsync : AsyncTask<JSONObject, Void, Int?>() {

    private val httpConnectionUtil = HttpConnectionUtil()
    private lateinit var onRegistrationListener: OnRegistrationListener

    override fun doInBackground(vararg params: JSONObject?): Int? {
        var response: String? = null
        response = httpConnectionUtil.requestPost(HttpConstants.URL.plus(HttpConstants.REGISTER), params[0])
        response.let {
            return if (it != null && !it.isNullOrEmpty()) {
                it.toInt()
            } else 0
        }
    }

    override fun onPostExecute(result: Int?) {
        super.onPostExecute(result)
        onRegistrationListener.onRegistrationSuccess(result)
    }

    fun setOnRegistrationListener(onRegistrationListener: OnRegistrationListener, request: JSONObject) {
        this.onRegistrationListener = onRegistrationListener
        super.executeOnExecutor(THREAD_POOL_EXECUTOR, request)
    }
}