package com.nhut.hoshi.navigation

import com.nhut.hoshi.features.sasayaki.SasayakiSettings
import com.nhut.hoshi.features.sync.SyncSettings
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReaderRouteAutoSyncStateTest {
    @Test
    fun readerLoadWaitsForSyncAndSasayakiSettingsBeforeOpenImportDecision() {
        assertFalse(
            ReaderRouteAutoSyncState(
                syncSettings = null,
                sasayakiSettings = SasayakiSettings(),
            ).isReadyToLoad,
        )
        assertFalse(
            ReaderRouteAutoSyncState(
                syncSettings = SyncSettings(enabled = true, autoSyncEnabled = true),
                sasayakiSettings = null,
            ).isReadyToLoad,
        )
    }

    @Test
    fun openImportRunsOnlyAfterLoadedGlobalSyncAndAutoSyncAreEnabled() {
        assertFalse(
            ReaderRouteAutoSyncState(
                syncSettings = SyncSettings(enabled = false, autoSyncEnabled = true),
                sasayakiSettings = SasayakiSettings(),
            ).shouldSyncOnOpen,
        )
        assertFalse(
            ReaderRouteAutoSyncState(
                syncSettings = SyncSettings(enabled = true, autoSyncEnabled = false),
                sasayakiSettings = SasayakiSettings(),
            ).shouldSyncOnOpen,
        )
        assertTrue(
            ReaderRouteAutoSyncState(
                syncSettings = SyncSettings(enabled = true, autoSyncEnabled = true),
                sasayakiSettings = SasayakiSettings(),
            ).shouldSyncOnOpen,
        )
    }

    @Test
    fun audioBookSyncRequiresLoadedSasayakiSyncSettings() {
        assertFalse(
            ReaderRouteAutoSyncState(
                syncSettings = SyncSettings(enabled = true, autoSyncEnabled = true),
                sasayakiSettings = SasayakiSettings(enabled = false, syncEnabled = true),
            ).shouldSyncAudioBook,
        )
        assertTrue(
            ReaderRouteAutoSyncState(
                syncSettings = SyncSettings(enabled = true, autoSyncEnabled = true),
                sasayakiSettings = SasayakiSettings(enabled = true, syncEnabled = true),
            ).shouldSyncAudioBook,
        )
    }
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
