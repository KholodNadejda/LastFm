package com.example.lastfm

import android.content.Context

class CustomPreference (private val context: Context) {
    private val preference =
        context.getSharedPreferences("loginMy", Context.MODE_PRIVATE)
    var login: String = ""
        get() {
            return preference.getString(LOGIN, "").orEmpty()
        }
        set(value) {
            field = value
            preference.edit().putString(LOGIN, value).apply()
        }
    var password: String = ""
        get() {
            return preference.getString(PASSWORD, "").orEmpty()
        }
        set(value) {
            field = value
            preference.edit().putString(PASSWORD, value).apply()
        }

    companion object {
        private const val LOGIN = "login"
        private const val PASSWORD = "password"
    }
}