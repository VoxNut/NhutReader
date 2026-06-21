package com.nhut.hoshi.features.bookshelf

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.text.TextRange
import com.nhut.hoshi.ui.replaceTextAndSelectStart
import org.junit.Assert.assertEquals
import org.junit.Test

class BookshelfRenameTextFieldTest {
    @Test
    fun resetRenameTextFieldKeepsCursorAtTitleStart() {
        val state = TextFieldState("Previous title", TextRange("Previous title".length))
        val title = "A very long title that extends beyond the rename field width"

        state.replaceTextAndSelectStart(title)

        assertEquals(title, state.text.toString())
        assertEquals(TextRange.Zero, state.selection)
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
