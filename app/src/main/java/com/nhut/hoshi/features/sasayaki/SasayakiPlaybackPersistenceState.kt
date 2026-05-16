package com.nhut.hoshi.features.sasayaki

import com.nhut.hoshi.epub.SasayakiPlaybackData

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SasayakiPlaybackPersistenceState(
    private val playbackRepository: SasayakiPlaybackRepository,
    private val audioSourceRepository: SasayakiAudioRepository,
    initialPlayback: SasayakiPlaybackData?,
    private val persistenceScope: CoroutineScope,
) {
    var playback by mutableStateOf(initialPlayback ?: SasayakiPlaybackData(lastPosition = 0.0))
        private set

    val delay: Double get() = playback.delay
    val rate: Float get() = playback.rate
    val audioStorageSummary: String
        get() = audioSourceRepository.storageSummary(playback)

    fun setDelay(value: Double) {
        playback = playback.copy(delay = value)
        save()
    }

    fun setRate(value: Float) {
        playback = playback.copy(rate = value)
        save()
    }

    fun importAudio(audioUri: Uri, copiedAudioFileName: String?) {
        playback = audioSourceRepository.importedPlayback(playback, audioUri, copiedAudioFileName)
        save()
    }

    fun clearAudioMetadata() {
        playback = playback.copy(
            lastPosition = 0.0,
            audioUri = null,
            audioFileName = null,
        )
        save()
    }

    fun savePosition(seconds: Double) {
        playback = playback.copy(lastPosition = seconds)
        save()
    }

    private fun save() {
        val snapshot = playback
        persistenceScope.launch(start = CoroutineStart.UNDISPATCHED) {
            withContext(NonCancellable) {
                playbackRepository.save(snapshot)
            }
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
