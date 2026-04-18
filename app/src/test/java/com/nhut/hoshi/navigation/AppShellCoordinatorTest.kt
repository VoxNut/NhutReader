package com.nhut.hoshi.navigation

import androidx.navigation3.runtime.NavKey
import com.nhut.hoshi.epub.BookEntry
import com.nhut.hoshi.epub.BookMetadata
import com.nhut.hoshi.features.bookshelf.SasayakiMatchRequest
import com.nhut.hoshi.features.dictionary.DictionarySettings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Test
import java.io.File

class AppShellCoordinatorTest {
    @Test
    fun dictionaryDefaultRouteAppliesOnceOnlyFromInitialBooksRoute() {
        val stateHolder = AppLaunchRouteStateHolder()
        val backStack = mutableListOf<NavKey>(AppRoute.BooksRoute)

        assertEquals(
            AppRoute.DictionaryRoute,
            stateHolder.defaultRouteAfterSettingsLoad(
                settings = DictionarySettings(dictionaryTabDefault = true),
                hasPendingImport = false,
                isBooksTabSelected = true,
                backStack = backStack,
            ),
        )
        assertNull(
            stateHolder.defaultRouteAfterSettingsLoad(
                settings = DictionarySettings(dictionaryTabDefault = true),
                hasPendingImport = false,
                isBooksTabSelected = true,
                backStack = backStack,
            ),
        )
    }

    @Test
    fun dictionaryDefaultRouteDoesNotOverridePendingImportOrNestedRoutes() {
        val pendingImportStateHolder = AppLaunchRouteStateHolder()
        val nestedRouteStateHolder = AppLaunchRouteStateHolder()

        assertNull(
            pendingImportStateHolder.defaultRouteAfterSettingsLoad(
                settings = DictionarySettings(dictionaryTabDefault = true),
                hasPendingImport = true,
                isBooksTabSelected = true,
                backStack = mutableListOf(AppRoute.BooksRoute),
            ),
        )
        assertNull(
            nestedRouteStateHolder.defaultRouteAfterSettingsLoad(
                settings = DictionarySettings(dictionaryTabDefault = true),
                hasPendingImport = false,
                isBooksTabSelected = true,
                backStack = mutableListOf(AppRoute.BooksRoute, AppRoute.ReaderRoute("book-a")),
            ),
        )
    }

    @Test
    fun dictionaryDefaultRouteDoesNotOverrideRestoredNonBooksTab() {
        val stateHolder = AppLaunchRouteStateHolder()

        assertNull(
            stateHolder.defaultRouteAfterSettingsLoad(
                settings = DictionarySettings(dictionaryTabDefault = true),
                hasPendingImport = false,
                isBooksTabSelected = false,
                backStack = mutableListOf(AppRoute.BooksRoute),
            ),
        )
    }

    @Test
    fun pendingImportCoordinatorRoutesOnlyWhenImportIsPending() {
        val coordinator = PendingImportRouteCoordinator()
        val backStack = mutableListOf<NavKey>(AppRoute.SettingsRoute, AppRoute.ReaderRoute("book-a"))

        coordinator.routePendingImport(hasPendingImport = false, backStack = backStack)
        assertEquals(listOf(AppRoute.SettingsRoute, AppRoute.ReaderRoute("book-a")), backStack)

        coordinator.routePendingImport(hasPendingImport = true, backStack = backStack)
        assertEquals(listOf(AppRoute.BooksRoute), backStack)
    }

    @Test
    fun sasayakiMatchRequestStoreStoresRequestsByBookId() {
        val store = SasayakiMatchRequestStore()
        val request = SasayakiMatchRequest(
            bookId = "book-a",
            bookEntry = BookEntry(
                root = File("book-a"),
                metadata = BookMetadata(
                    id = "book-a",
                    title = "Book A",
                    cover = null,
                    folder = "book-a",
                    lastAccess = 0.0,
                ),
            ),
        )

        store.put(request)

        assertSame(request, store.get("book-a"))
        assertNull(store.get("missing"))
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
