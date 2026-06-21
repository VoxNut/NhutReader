package com.nhut.hoshi.features.dictionary

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import org.junit.Assert.assertEquals
import org.junit.Test

class DictionaryCustomCssEditorTest {
    @Test
    fun selectorSnippetMatchesIosDictionaryCssSelector() {
        assertEquals(
            """
                [data-dictionary="JMdict"] {
                    
                }
                
            """.trimIndent(),
            dictionarySelectorCssSnippet("JMdict"),
        )
    }

    @Test
    fun cssInsertionReplacesCurrentSelectionAndMovesCursorAfterInsertedText() {
        val current = TextFieldValue(
            text = "body { color: red; }",
            selection = TextRange(7, 18),
        )

        val inserted = insertCustomCssText(current, "font-family: \"Klee\" !important;")

        assertEquals("body { font-family: \"Klee\" !important; }", inserted.text)
        assertEquals(TextRange(38), inserted.selection)
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
