package com.mywings.newtwitterapp.process

import com.mywings.thieffacedetector.model.User


interface OnLoginListener {
    fun onLoginSuccess(user: User?)
}