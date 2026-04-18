package com.nhut.hoshi.navigation

import com.nhut.hoshi.features.bookshelf.MainTab
import org.junit.Assert.assertEquals
import org.junit.Test

class AppShellTabSelectionTest {
    @Test
    fun repeatedDictionaryTabSelectionRequestsSearchFocus() {
        assertEquals(
            8,
            nextDictionaryFocusRequestKey(
                selectedTab = MainTab.Dictionary,
                requestedTab = MainTab.Dictionary,
                currentKey = 7,
            ),
        )
    }

    @Test
    fun switchingToDictionaryDoesNotRequestRepeatedFocus() {
        assertEquals(
            7,
            nextDictionaryFocusRequestKey(
                selectedTab = MainTab.Books,
                requestedTab = MainTab.Dictionary,
                currentKey = 7,
            ),
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
