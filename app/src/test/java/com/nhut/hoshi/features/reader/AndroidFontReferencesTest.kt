package com.nhut.hoshi.features.reader

import com.nhut.hoshi.features.dictionary.LookupPopupHtml
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AndroidFontReferencesTest {
    @Test
    fun readerCssUsesAndroidJapaneseFontsWithoutIosOnlyNames() {
        val css = ReaderContentStyles.styleTag()

        assertTrue(css.contains("Noto Serif CJK JP"))
        assertFalse(css.contains(iosOnlyJapaneseFontPrefix))
    }

    @Test
    fun lookupPopupHtmlUsesAndroidJapaneseFontsWithoutIosOnlyNames() {
        val html = LookupPopupHtml.renderIframeDocument()

        assertTrue(html.contains("Noto Sans CJK JP"))
        assertTrue(html.contains("""<html lang="ja""""))
        assertFalse(html.contains(iosOnlyJapaneseFontPrefix))
    }

    private companion object {
        const val iosOnlyJapaneseFontPrefix = "Hiragino"
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
