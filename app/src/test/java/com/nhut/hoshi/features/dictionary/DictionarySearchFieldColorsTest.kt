package com.nhut.hoshi.features.dictionary

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.nhut.hoshi.ui.hoshiTextFieldCursorColor
import org.junit.Assert.assertEquals
import org.junit.Test

class DictionarySearchFieldColorsTest {
    @Test
    fun cursorUsesReadableSearchFieldForegroundColor() {
        val darkModeForeground = Color(0xFFECE6F0)

        assertEquals(darkModeForeground, hoshiTextFieldCursorColor(darkModeForeground))
    }

    @Test
    fun searchKeyboardHintsJapaneseInputAndShowsOnFocus() {
        val options = dictionarySearchKeyboardOptions()

        assertEquals("ja-JP", options.hintLocales?.single()?.toLanguageTag())
        assertEquals(true, options.showKeyboardOnFocus)
    }

    @Test
    fun searchTextStyleUsesFixedJapaneseContentProfile() {
        val textStyle = dictionarySearchTextStyle(
            baseStyle = TextStyle.Default,
            color = Color.Black,
        )

        assertEquals("ja-JP", textStyle.localeList?.single()?.toLanguageTag())
        assertEquals(Color.Black, textStyle.color)
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
