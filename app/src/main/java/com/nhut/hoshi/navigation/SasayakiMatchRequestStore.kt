package com.nhut.hoshi.navigation

import com.nhut.hoshi.features.bookshelf.SasayakiMatchRequest

internal class SasayakiMatchRequestStore {
    private var requests: Map<String, SasayakiMatchRequest> = emptyMap()

    fun put(request: SasayakiMatchRequest) {
        requests = requests + (request.bookId to request)
    }

    fun get(bookId: String): SasayakiMatchRequest? =
        requests[bookId]
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
