package com.nhut.hoshi.features.anki

import org.junit.Assert.assertEquals
import org.junit.Test

class AnkiFetchActionTest {
    @Test
    fun unavailableAnkiDroidShowsUnavailableBeforeRequestingPermission() {
        assertEquals(
            AnkiFetchAction.ShowApiUnavailable,
            ankiFetchAction(
                backendKind = AnkiBackendKind.AnkiDroid,
                isAnkiDroidAvailable = false,
                permissionGranted = false,
            ),
        )
    }

    @Test
    fun availableAnkiDroidRequestsPermissionWhenMissing() {
        assertEquals(
            AnkiFetchAction.RequestPermission,
            ankiFetchAction(
                backendKind = AnkiBackendKind.AnkiDroid,
                isAnkiDroidAvailable = true,
                permissionGranted = false,
            ),
        )
    }

    @Test
    fun grantedPermissionFetchesConfiguration() {
        assertEquals(
            AnkiFetchAction.FetchConfiguration,
            ankiFetchAction(
                backendKind = AnkiBackendKind.AnkiDroid,
                isAnkiDroidAvailable = true,
                permissionGranted = true,
            ),
        )
    }

    @Test
    fun ankiConnectFetchesConfigurationWithoutAnkiDroidPermission() {
        assertEquals(
            AnkiFetchAction.FetchConfiguration,
            ankiFetchAction(
                backendKind = AnkiBackendKind.AnkiConnect,
                isAnkiDroidAvailable = false,
                permissionGranted = false,
            ),
        )
    }

    @Test
    fun appSettingsIntentUsesPackageUriForThisApp() {
        assertEquals(
            "package:com.nhut.hoshi.debug",
            ankiPermissionSettingsUri("com.nhut.hoshi.debug"),
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
