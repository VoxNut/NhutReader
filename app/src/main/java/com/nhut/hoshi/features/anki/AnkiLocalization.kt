package com.nhut.hoshi.features.anki

import androidx.annotation.StringRes
import com.nhut.hoshi.R

@get:StringRes
internal val AnkiDuplicateScope.labelRes: Int
    get() = when (this) {
        AnkiDuplicateScope.Collection -> R.string.anki_duplicate_scope_collection
        AnkiDuplicateScope.Deck -> R.string.anki_duplicate_scope_deck
        AnkiDuplicateScope.DeckRoot -> R.string.anki_duplicate_scope_deck_root
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
