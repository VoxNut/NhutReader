package com.nhut.hoshi.importing

import android.content.Context

fun Throwable.localizedImportMessage(context: Context, fallback: String): String =
    if (this is UnsupportedImportFileTypeException) {
        context.getString(messageRes)
    } else {
        localizedMessage ?: fallback
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
