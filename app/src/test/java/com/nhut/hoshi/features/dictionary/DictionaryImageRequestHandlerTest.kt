package com.nhut.hoshi.features.dictionary

import org.junit.Assert.assertEquals
import org.junit.Test

class DictionaryImageRequestHandlerTest {
    @Test
    fun dictionaryImageMimeTypeMatchesIosImageHandler() {
        assertEquals("image/svg+xml", dictionaryImageMimeType("icons/arrow.svg"))
        assertEquals("image/png", dictionaryImageMimeType("photo.PNG"))
        assertEquals("image/jpeg", dictionaryImageMimeType("image.jpeg"))
        assertEquals("application/octet-stream", dictionaryImageMimeType("unknown.bin"))
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
