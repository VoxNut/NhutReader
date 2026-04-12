package com.nhut.hoshi.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoute : NavKey {
    @Serializable
    data object MainRoute : AppRoute

    @Serializable
    data object BooksRoute : AppRoute

    @Serializable
    data object DictionaryRoute : AppRoute

    @Serializable
    data object SettingsRoute : AppRoute

    @Serializable
    data class SettingsDetailRoute(
        val section: SettingsDetailSection,
    ) : AppRoute

    @Serializable
    data class ReaderRoute(
        val bookId: String,
    ) : AppRoute

    @Serializable
    data class SasayakiMatchRoute(
        val bookId: String,
    ) : AppRoute
}

@Serializable
enum class SettingsDetailSection {
    Dictionaries,
    Anki,
    AnkiHistory,
    Appearance,
    Behavior,
    Advanced,
    Diagnostics,
    About,
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
