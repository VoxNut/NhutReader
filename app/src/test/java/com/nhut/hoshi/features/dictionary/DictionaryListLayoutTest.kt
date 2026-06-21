package com.nhut.hoshi.features.dictionary

import com.nhut.hoshi.ui.UiText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DictionaryListLayoutTest {
    @Test
    fun errorMessageDoesNotReserveAListRow() {
        val layout = DictionaryListLayout.from(errorMessage = UiText.Literal("Failed to import"))

        assertEquals(1, layout.dictionaryStartGlobalIndex)
        assertTrue(layout.showErrorDialog)
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
