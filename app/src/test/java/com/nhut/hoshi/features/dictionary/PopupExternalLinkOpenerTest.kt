package com.nhut.hoshi.features.dictionary

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PopupExternalLinkOpenerTest {
    @Test
    fun opensHttpLinksFromDictionaryGlossaries() {
        val opened = mutableListOf<ExternalBrowserRequest>()

        val handled = openPopupExternalLink(
            "https://www.pixiv.net/artworks/123",
            openExternalBrowser = opened::add,
        )

        assertTrue(handled)
        assertEquals(listOf(ExternalBrowserRequest("https://www.pixiv.net/artworks/123")), opened)
    }

    @Test
    fun ignoresNonWebLinksFromPopupMessages() {
        val opened = mutableListOf<ExternalBrowserRequest>()

        val handled = openPopupExternalLink(
            "javascript:alert(1)",
            openExternalBrowser = opened::add,
        )

        assertFalse(handled)
        assertTrue(opened.isEmpty())
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
