package com.nhut.hoshi.content

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ContentLanguageProfileTest {
    @Test
    fun defaultProfileIsFixedJapaneseAndUsesAndroidFonts() {
        val japanese = ContentLanguageProfile.Default

        assertEquals("ja", japanese.htmlLang)
        assertEquals("ja-JP", japanese.composeLocaleTag)
        assertEquals("ja-JP", japanese.inputLocaleTag)
        assertTrue(japanese.webViewFontFamilyCss.contains("Noto Sans CJK JP"))
        assertTrue(japanese.readerSerifFontFamilyCss.contains("Noto Serif CJK JP"))
        assertFalse(japanese.webViewFontFamilyCss.contains("Hira" + "gino"))
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
