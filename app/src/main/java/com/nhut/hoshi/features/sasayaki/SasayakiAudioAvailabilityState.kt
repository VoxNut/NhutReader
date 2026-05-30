package com.nhut.hoshi.features.sasayaki

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.nhut.hoshi.R
import com.nhut.hoshi.ui.UiText

class SasayakiAudioAvailabilityState {
    var errorMessage by mutableStateOf<UiText?>(null)
        private set

    var hasAudio by mutableStateOf(false)
        private set

    fun markRestoreFailed(error: Throwable) {
        errorMessage = error.localizedMessage?.let(UiText::Literal)
            ?: UiText.Resource(R.string.sasayaki_import_audiobook_failed)
        hasAudio = false
    }

    fun markRestoreSucceeded() {
        hasAudio = true
        errorMessage = null
    }

    fun markAudioCleared() {
        hasAudio = false
        errorMessage = null
    }

    fun markAudioUnavailable() {
        hasAudio = false
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
