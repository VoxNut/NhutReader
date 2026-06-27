package com.nhut.hoshi

import androidx.compose.runtime.staticCompositionLocalOf
import dagger.Lazy
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import com.nhut.hoshi.di.ApplicationScope
import com.nhut.hoshi.dictionary.DictionaryRepository
import com.nhut.hoshi.epub.BookRepository
import com.nhut.hoshi.epub.EpubBookParser
import com.nhut.hoshi.features.audio.AudioSettingsRepository
import com.nhut.hoshi.features.audio.LocalAudioRepository
import com.nhut.hoshi.features.backup.HoshiBackupRepository
import com.nhut.hoshi.features.dictionary.DictionarySettingsRepository
import com.nhut.hoshi.features.reader.ReaderFontManager
import com.nhut.hoshi.features.reader.ReaderSettingsRepository
import com.nhut.hoshi.features.sasayaki.SasayakiSettingsRepository
import com.nhut.hoshi.features.storage.StorageCleanupRepository
import com.nhut.hoshi.features.sync.DeviceCodeDriveAuthorizer
import com.nhut.hoshi.features.sync.SyncManager
import com.nhut.hoshi.features.sync.SyncSettingsRepository
import com.nhut.hoshi.features.update.AndroidUpdateDownloadManager
import com.nhut.hoshi.features.update.UpdateCheckService
import com.nhut.hoshi.features.update.UpdateDownloadStore
import com.nhut.hoshi.features.update.UpdatePromptEvents
import com.nhut.hoshi.features.update.UpdateScheduler
import com.nhut.hoshi.features.update.UpdateSettingsRepository

internal class HoshiUiDependencies @Inject constructor(
    @param:ApplicationScope private val appScopeProvider: Lazy<CoroutineScope>,
    private val bookRepositoryProvider: Lazy<BookRepository>,
    private val dictionaryRepositoryProvider: Lazy<DictionaryRepository>,
    private val readerSettingsRepositoryProvider: Lazy<ReaderSettingsRepository>,
    private val dictionarySettingsRepositoryProvider: Lazy<DictionarySettingsRepository>,
    private val audioSettingsRepositoryProvider: Lazy<AudioSettingsRepository>,
    private val sasayakiSettingsRepositoryProvider: Lazy<SasayakiSettingsRepository>,
    private val syncSettingsRepositoryProvider: Lazy<SyncSettingsRepository>,
    private val updateSettingsRepositoryProvider: Lazy<UpdateSettingsRepository>,
    private val updateDownloadStoreProvider: Lazy<UpdateDownloadStore>,
    private val epubBookParserProvider: Lazy<EpubBookParser>,
    private val readerFontManagerProvider: Lazy<ReaderFontManager>,
    private val localAudioRepositoryProvider: Lazy<LocalAudioRepository>,
    private val backupRepositoryProvider: Lazy<HoshiBackupRepository>,
    private val storageCleanupRepositoryProvider: Lazy<StorageCleanupRepository>,
    private val deviceCodeDriveAuthorizerProvider: Lazy<DeviceCodeDriveAuthorizer>,
    private val syncManagerProvider: Lazy<SyncManager>,
    private val updateDownloadManagerProvider: Lazy<AndroidUpdateDownloadManager>,
    private val updateCheckServiceProvider: Lazy<UpdateCheckService>,
    private val updatePromptEventsProvider: Lazy<UpdatePromptEvents>,
    private val updateSchedulerProvider: Lazy<UpdateScheduler>,
) {
    val appScope: CoroutineScope get() = appScopeProvider.get()
    val bookRepository: BookRepository get() = bookRepositoryProvider.get()
    val dictionaryRepository: DictionaryRepository get() = dictionaryRepositoryProvider.get()
    val readerSettingsRepository: ReaderSettingsRepository get() = readerSettingsRepositoryProvider.get()
    val dictionarySettingsRepository: DictionarySettingsRepository get() = dictionarySettingsRepositoryProvider.get()
    val audioSettingsRepository: AudioSettingsRepository get() = audioSettingsRepositoryProvider.get()
    val sasayakiSettingsRepository: SasayakiSettingsRepository get() = sasayakiSettingsRepositoryProvider.get()
    val syncSettingsRepository: SyncSettingsRepository get() = syncSettingsRepositoryProvider.get()
    val updateSettingsRepository: UpdateSettingsRepository get() = updateSettingsRepositoryProvider.get()
    val updateDownloadStore: UpdateDownloadStore get() = updateDownloadStoreProvider.get()
    val epubBookParser: EpubBookParser get() = epubBookParserProvider.get()
    val readerFontManager: ReaderFontManager get() = readerFontManagerProvider.get()
    val localAudioRepository: LocalAudioRepository get() = localAudioRepositoryProvider.get()
    val backupRepository: HoshiBackupRepository get() = backupRepositoryProvider.get()
    val storageCleanupRepository: StorageCleanupRepository get() = storageCleanupRepositoryProvider.get()
    val deviceCodeDriveAuthorizer: DeviceCodeDriveAuthorizer get() = deviceCodeDriveAuthorizerProvider.get()
    val syncManager: SyncManager get() = syncManagerProvider.get()
    val updateDownloadManager: AndroidUpdateDownloadManager get() = updateDownloadManagerProvider.get()
    val updateCheckService: UpdateCheckService get() = updateCheckServiceProvider.get()
    val updatePromptEvents: UpdatePromptEvents get() = updatePromptEventsProvider.get()
    val updateScheduler: UpdateScheduler get() = updateSchedulerProvider.get()
}

internal val LocalHoshiUiDependencies = staticCompositionLocalOf<HoshiUiDependencies> {
    error("HoshiUiDependencies is not provided.")
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
