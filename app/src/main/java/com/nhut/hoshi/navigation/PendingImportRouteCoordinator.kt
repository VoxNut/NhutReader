package com.nhut.hoshi.navigation

import androidx.navigation3.runtime.NavKey

internal class PendingImportRouteCoordinator {
    fun routePendingImport(
        hasPendingImport: Boolean,
        backStack: MutableList<NavKey>,
    ) {
        if (hasPendingImport) {
            backStack.routeExternalBookImport()
        }
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
