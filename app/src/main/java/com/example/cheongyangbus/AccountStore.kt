
package com.example.cheongyangbus

import android.content.Context

object AccountStore {

    private const val ACCOUNT_PREF = "CheongyangBusAccount"
    private const val SESSION_PREF = "cheongyang_bus_session"

    private const val LOGIN_KEY = "is_logged_in"
    private const val ADMIN_KEY = "is_admin"

    // 시연용 관리자 계정
    const val ADMIN_ID = "admin"
    private const val ADMIN_PASSWORD = "admin1234"

    // 로그인 정보 확인
    fun login(
        context: Context,
        id: String,
        password: String
    ): Boolean {

        val cleanId = id.trim()

        // 관리자 계정 확인
        if (isAdminAccount(cleanId, password)) {
            return true
        }

        // 일반 사용자 계정 확인
        val prefs = context.getSharedPreferences(
            ACCOUNT_PREF,
            Context.MODE_PRIVATE
        )

        return prefs.getString("user_$cleanId", null) == password
    }

    // 관리자 계정인지 확인
    fun isAdminAccount(
        id: String,
        password: String
    ): Boolean {

        return id.trim() == ADMIN_ID &&
                password == ADMIN_PASSWORD
    }

    // 로그인 상태 저장
    fun setLoggedIn(
        context: Context,
        loggedIn: Boolean,
        isAdmin: Boolean = false
    ) {

        context.getSharedPreferences(
            SESSION_PREF,
            Context.MODE_PRIVATE
        ).edit()
            .putBoolean(LOGIN_KEY, loggedIn)
            .putBoolean(ADMIN_KEY, loggedIn && isAdmin)
            .apply()
    }

    // 로그인 여부 확인
    fun isLoggedIn(context: Context): Boolean {

        return context.getSharedPreferences(
            SESSION_PREF,
            Context.MODE_PRIVATE
        ).getBoolean(LOGIN_KEY, false)
    }

    // 관리자 여부 확인
    fun isAdmin(context: Context): Boolean {

        return context.getSharedPreferences(
            SESSION_PREF,
            Context.MODE_PRIVATE
        ).getBoolean(ADMIN_KEY, false)
    }

    // 로그아웃
    fun logout(context: Context) {
        setLoggedIn(context, false)
    }
}
