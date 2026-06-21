package com.nhut.hoshi.features.bookshelf

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PendingImportGateTest {
    @Test
    fun rejectsDuplicatePendingImportsUntilTheActiveImportFinishes() {
        val gate = PendingImportGate<String>()

        assertTrue(gate.tryStart("content://books/test.epub"))
        assertFalse(gate.tryStart("content://books/test.epub"))
        assertFalse(gate.tryStart("content://books/other.epub"))

        gate.finish("content://books/test.epub")

        assertTrue(gate.tryStart("content://books/test.epub"))
    }

    @Test
    fun ignoresFinishForAnOlderImportToken() {
        val gate = PendingImportGate<String>()

        assertTrue(gate.tryStart("content://books/test.epub"))
        gate.finish("content://books/other.epub")

        assertFalse(gate.tryStart("content://books/test.epub"))
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
