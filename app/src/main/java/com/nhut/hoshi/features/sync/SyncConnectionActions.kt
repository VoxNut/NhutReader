package com.nhut.hoshi.features.sync

internal data class SyncConnectionActions(
    val showConnect: Boolean,
    val connectEnabled: Boolean,
    val showSignOut: Boolean,
    val signOutEnabled: Boolean,
)

internal fun syncConnectionActions(
    authStatus: DriveAuthStatus,
    isAuthorizing: Boolean,
): SyncConnectionActions =
    when (authStatus) {
        DriveAuthStatus.Connected -> SyncConnectionActions(
            showConnect = false,
            connectEnabled = false,
            showSignOut = true,
            signOutEnabled = !isAuthorizing,
        )
        DriveAuthStatus.MissingConfiguration -> SyncConnectionActions(
            showConnect = true,
            connectEnabled = !isAuthorizing,
            showSignOut = false,
            signOutEnabled = false,
        )
        DriveAuthStatus.NotConnected,
        is DriveAuthStatus.Failed,
        -> SyncConnectionActions(
            showConnect = true,
            connectEnabled = !isAuthorizing,
            showSignOut = false,
            signOutEnabled = false,
        )
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
