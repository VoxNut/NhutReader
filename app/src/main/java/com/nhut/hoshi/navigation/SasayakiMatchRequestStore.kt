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
