package com.nhut.hoshi

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProcessTextLookupRequestTest {
    @Test
    fun createsRequestForNonBlankProcessTextSelection() {
        val request = ProcessTextLookupRequest.from(
            action = "android.intent.action.PROCESS_TEXT",
            selectedText = " 食べる ",
        )

        assertEquals("食べる", request?.query)
    }

    @Test
    fun ignoresBlankSelectionsAndOtherActions() {
        assertNull(
            ProcessTextLookupRequest.from(
                action = "android.intent.action.PROCESS_TEXT",
                selectedText = "   ",
            ),
        )
        assertNull(
            ProcessTextLookupRequest.from(
                action = "android.intent.action.VIEW",
                selectedText = "食べる",
            ),
        )
    }

    @Test
    fun createsRequestForTranslateText() {
        val request = ProcessTextLookupRequest.from(
            action = "android.intent.action.TRANSLATE",
            selectedText = null,
            sharedText = "  食べる  ",
        )

        assertEquals("食べる", request?.query)
    }

    @Test
    fun createsRequestForSharedPlainText() {
        val request = ProcessTextLookupRequest.from(
            action = "android.intent.action.SEND",
            selectedText = null,
            sharedText = "  食べる  ",
        )

        assertEquals("食べる", request?.query)
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
