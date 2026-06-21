package com.nhut.hoshi.features.dictionary

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DictionaryRowInteractionTest {
    @Test
    fun longPressDeleteEntryOnlyComesFromContentArea() {
        assertTrue(DictionaryRowInteraction.canRevealDeleteOnLongPress(DictionaryRowInteraction.Area.Content))
        assertFalse(DictionaryRowInteraction.canRevealDeleteOnLongPress(DictionaryRowInteraction.Area.DragHandle))
        assertFalse(DictionaryRowInteraction.canRevealDeleteOnLongPress(DictionaryRowInteraction.Area.EnableSwitch))
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
