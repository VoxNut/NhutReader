package com.nhut.hoshi.navigation

import com.nhut.hoshi.features.sasayaki.SasayakiSettings
import com.nhut.hoshi.features.sync.SyncSettings

internal data class ReaderRouteAutoSyncState(
    val syncSettings: SyncSettings?,
    val sasayakiSettings: SasayakiSettings?,
) {
    val isReadyToLoad: Boolean
        get() = syncSettings != null && sasayakiSettings != null

    val isReaderAutoSyncEnabled: Boolean
        get() = syncSettings?.enabled == true && syncSettings.autoSyncEnabled

    val shouldSyncOnOpen: Boolean
        get() = isReaderAutoSyncEnabled

    val shouldSyncAudioBook: Boolean
        get() = sasayakiSettings?.enabled == true && sasayakiSettings.syncEnabled
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
