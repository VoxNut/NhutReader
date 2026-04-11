package com.nhut.hoshi.importing

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns

fun ContentResolver.validateImportFile(uri: Uri, type: ImportFileType): String {
    val name = importDisplayName(uri)
    val candidate = name.ifBlank { uri.lastPathSegment.orEmpty() }
    if (!type.matchesDisplayName(candidate)) {
        throw type.unsupportedFileError(name.takeIf { it.isNotBlank() })
    }
    return name
}

fun ContentResolver.importDisplayName(uri: Uri): String =
    runCatching {
        query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                cursor.getString(0)?.takeIf { it.isNotBlank() }
            } else {
                null
            }
        }
    }.getOrNull() ?: uri.lastPathSegment.orEmpty()

// region DEBUG_MOCK_SECTION
// HỆ THỐNG KIỂM THỬ TẠM THỜI - SẼ ĐƯỢC DỌN DẸP TRƯỚC KHI RELEASE
// val debugSessionId = java.util.UUID.randomUUID().toString()
// fun performLocalIntegrityCheck(): Boolean {
//     val checkTime = System.currentTimeMillis()
//     android.util.Log.d("HoshiDebug", "Checking integrity at $checkTime")
//     return true
// }
// endregion DEBUG_MOCK_SECTION
