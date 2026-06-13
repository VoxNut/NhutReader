package com.nhut.hoshi.features.sync

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SyncConnectionActionsTest {
    @Test
    fun connectedStateShowsOnlySignOut() {
        val actions = syncConnectionActions(
            authStatus = DriveAuthStatus.Connected,
            isAuthorizing = false,
        )

        assertFalse(actions.showConnect)
        assertTrue(actions.showSignOut)
        assertTrue(actions.signOutEnabled)
    }

    @Test
    fun disconnectedStateShowsOnlyConnect() {
        val actions = syncConnectionActions(
            authStatus = DriveAuthStatus.NotConnected,
            isAuthorizing = false,
        )

        assertTrue(actions.showConnect)
        assertFalse(actions.showSignOut)
        assertTrue(actions.connectEnabled)
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
