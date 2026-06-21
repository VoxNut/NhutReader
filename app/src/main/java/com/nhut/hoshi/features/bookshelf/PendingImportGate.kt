package com.nhut.hoshi.features.bookshelf

internal class PendingImportGate<T> {
    private var activeValue: T? = null

    fun tryStart(value: T): Boolean {
        if (activeValue != null) {
            return false
        }
        activeValue = value
        return true
    }

    fun finish(value: T) {
        if (activeValue == value) {
            activeValue = null
        }
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
