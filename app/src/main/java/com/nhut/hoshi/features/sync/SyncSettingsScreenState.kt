package com.nhut.hoshi.features.sync

import com.nhut.hoshi.features.settings.settingsContentReady

internal data class SyncSettingsScreenState(
    val settings: SyncSettings?,
    val authStatus: DriveAuthStatus?,
) {
    val isContentReady: Boolean
        get() = settingsContentReady(settings, authStatus)

    val showClearCacheAction: Boolean
        get() = isContentReady && settings?.enabled == true && authStatus == DriveAuthStatus.Connected
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
