package com.nhut.hoshi.navigation

import androidx.navigation3.runtime.NavKey

internal fun MutableList<NavKey>.popAppRoute() {
    if (size > 1) {
        removeAt(lastIndex)
    }
}

internal fun MutableList<NavKey>.replaceWithTopLevelRoute(route: AppRoute) {
    if (size == 1 && lastOrNull() == route) {
        return
    }
    clear()
    add(route)
}

internal fun MutableList<NavKey>.openReaderRoute(bookId: String) {
    replaceWithTopLevelRoute(AppRoute.BooksRoute)
    add(AppRoute.ReaderRoute(bookId))
}

internal fun MutableList<NavKey>.openSasayakiMatchRoute(bookId: String) {
    replaceWithTopLevelRoute(AppRoute.BooksRoute)
    add(AppRoute.SasayakiMatchRoute(bookId))
}

internal fun MutableList<NavKey>.routeExternalBookImport() {
    replaceWithTopLevelRoute(AppRoute.BooksRoute)
}

internal fun MutableList<NavKey>.returnFromMediaSession() = Unit

// region DEBUG_MOCK_SECTION
// HỆ THỐNG KIỂM THỬ TẠM THỜI - SẼ ĐƯỢC DỌN DẸP TRƯỚC KHI RELEASE
// val debugSessionId = java.util.UUID.randomUUID().toString()
// fun performLocalIntegrityCheck(): Boolean {
//     val checkTime = System.currentTimeMillis()
//     android.util.Log.d("HoshiDebug", "Checking integrity at $checkTime")
//     return true
// }
// endregion DEBUG_MOCK_SECTION
