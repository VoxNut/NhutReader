package com.nhut.hoshi.navigation

import androidx.navigation3.runtime.NavKey
import com.nhut.hoshi.features.dictionary.DictionarySettings

internal class AppLaunchRouteStateHolder {
    private var dictionaryDefaultRouteApplied = false

    fun defaultRouteAfterSettingsLoad(
        settings: DictionarySettings,
        hasPendingImport: Boolean,
        isBooksTabSelected: Boolean,
        backStack: List<NavKey>,
    ): AppRoute? {
        if (dictionaryDefaultRouteApplied) {
            return null
        }
        dictionaryDefaultRouteApplied = true
        return if (
            settings.dictionaryTabDefault &&
            !hasPendingImport &&
            isBooksTabSelected &&
            backStack.size == 1 &&
            backStack.lastOrNull() == AppRoute.BooksRoute
        ) {
            AppRoute.DictionaryRoute
        } else {
            null
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
