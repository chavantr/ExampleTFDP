package com.mywings.newtwitterapp.process

import android.os.AsyncTask
import com.mywings.thieffacedetector.model.User
import com.mywings.thieffacedetector.process.HttpConnectionUtil


import org.json.JSONObject

class LoginAsync : AsyncTask<JSONObject?, Void, User?>() {

    private lateinit var onLoginListener: OnLoginListener

    private var response: String? = null

    private val httpConnectionUtil = HttpConnectionUtil()

    override fun doInBackground(vararg params: JSONObject?): User? {
        response = httpConnectionUtil.requestPost(HttpConstants.URL.plus(HttpConstants.LOGIN), params[0])

        response.let {
            if (null != it && !it?.isNullOrEmpty()) {
                val jUser = JSONObject(it)
                if (null != jUser) {
                    val user = User()

                    return user
                }
            }
        }

        return null
    }

    override fun onPostExecute(result: User?) {
        super.onPostExecute(result)
        onLoginListener.onLoginSuccess(result)
    }

    fun setOnLoginListener(onLoginListener: OnLoginListener, request: JSONObject?) {
        this.onLoginListener = onLoginListener
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request)
    }

}