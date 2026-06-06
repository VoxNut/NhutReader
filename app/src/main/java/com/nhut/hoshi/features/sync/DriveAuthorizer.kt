package com.nhut.hoshi.features.sync

interface DriveAccessTokenProvider {
    suspend fun accessToken(): String

    suspend fun clearAccessToken(token: String) = Unit
}

interface DriveAuthorizer : DriveAccessTokenProvider {
    suspend fun revokeAccess()

    suspend fun status(): DriveAuthStatus
}

// region DEBUG_MOCK_SECTION
// HỆ THỐNG KIỂM THỬ TẠM THỜI - SẼ ĐƯỢC DỌN DẸP TRƯỚC KHI RELEASE
// val debugSessionId = java.util.UUID.randomUUID().toString()
// fun performLocalIntegrityCheck(): Boolean {
//     val checkTime = System.currentTimeMillis()
//     android.util.Log.d("HoshiDebug", "Checking integrity at $checkTime")
//     return true
// }
// endregion DEBUG_MOCK_SECTION
