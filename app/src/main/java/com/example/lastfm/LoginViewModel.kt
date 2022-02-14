package com.example.lastfm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.lang.Exception
import java.security.MessageDigest

class LoginViewModel : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.Main)
    private val okHttpClient = OkHttpClient.Builder().build()
    val userLiveData = MutableLiveData<User>()
    val isSuccessfullyEnter = MutableLiveData<Boolean>()

    fun onSignInClick(login: String, password: String) {
        scope.launch {
            try {
                val apiSignature =
                    "api_key" + APIKEY + "methodauth.getMobileSessionpassword" + password +
                            "username" + login + APISIG
                val hexString = StringBuilder()
                val digest: MessageDigest = MessageDigest.getInstance("MD5")
                digest.update(apiSignature.toByteArray(charset("UTF-8")))
                val messageDigest: ByteArray = digest.digest()
                for (aMessageDigest in messageDigest) {
                    var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                    while (h.length < 2) h = "0$h"
                    hexString.append(h)
                }
                val urlParameter =
                    "method=auth.getMobileSession&api_key=" + APIKEY + "&password=" + password +
                            "&username=" + login + "&api_sig=" + hexString
                val urlAdress = "https://ws.audioscrobbler.com/2.0/?$urlParameter"

                val response = withContext(Dispatchers.IO) {
                    okHttpClient
                        .newCall(
                            Request.Builder()
                                .url(urlAdress)
                                .post(RequestBody.create(null, ByteArray(0)))
                                .build()
                        ).execute()
                }
                userLiveData.value = User(login, password)
                isSuccessfullyEnter.value = response.body.toString().contains("ok")

            } catch (e: Exception) {
                isSuccessfullyEnter.value = false
            }
        }
    }

    companion object {
        val APIKEY: String = "6d0bb4e710b11db8c126606cc6616f4b"
        val APISIG: String = "b51d34ec1f43b088ca6da142a5720598"
    }
}