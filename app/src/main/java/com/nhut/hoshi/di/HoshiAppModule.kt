package com.nhut.hoshi.di

import android.content.ContentResolver
import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import com.nhut.hoshi.BuildConfig
import com.nhut.hoshi.features.anki.AnkiSettingsRepository
import com.nhut.hoshi.features.anki.ankiSettingsRepository
import com.nhut.hoshi.features.audio.AudioSettingsRepository
import com.nhut.hoshi.features.audio.audioSettingsRepository
import com.nhut.hoshi.features.bookshelf.BookshelfSettingsRepository
import com.nhut.hoshi.features.bookshelf.bookshelfSettingsRepository
import com.nhut.hoshi.features.dictionary.DictionarySettingsRepository
import com.nhut.hoshi.features.dictionary.dictionarySettingsRepository
import com.nhut.hoshi.features.reader.ReaderSettingsRepository
import com.nhut.hoshi.features.reader.readerSettingsRepository
import com.nhut.hoshi.features.sasayaki.SasayakiSettingsRepository
import com.nhut.hoshi.features.sasayaki.sasayakiSettingsRepository
import com.nhut.hoshi.features.sync.DriveSyncDataSource
import com.nhut.hoshi.features.sync.SyncSettingsRepository
import com.nhut.hoshi.features.sync.syncSettingsRepository
import com.nhut.hoshi.features.update.UpdateDownloadStore
import com.nhut.hoshi.features.update.UpdateSettingsRepository
import com.nhut.hoshi.features.update.updateDownloadStore
import com.nhut.hoshi.features.update.updateSettingsRepository

@Module
@InstallIn(SingletonComponent::class)
internal object HoshiAppModule {
    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(
        @MainDispatcher mainDispatcher: CoroutineDispatcher,
    ): CoroutineScope = CoroutineScope(SupervisorJob() + mainDispatcher)

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate

    @Provides
    @Singleton
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver =
        context.contentResolver

    @Provides
    @Singleton
    @FilesDir
    fun provideFilesDir(@ApplicationContext context: Context): File = context.filesDir

    @Provides
    @Singleton
    @CacheDir
    fun provideCacheDir(@ApplicationContext context: Context): File = context.cacheDir

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun provideReaderSettingsRepository(@ApplicationContext context: Context): ReaderSettingsRepository =
        context.readerSettingsRepository()

    @Provides
    @Singleton
    fun provideDictionarySettingsRepository(@ApplicationContext context: Context): DictionarySettingsRepository =
        context.dictionarySettingsRepository()

    @Provides
    @Singleton
    fun provideAudioSettingsRepository(@ApplicationContext context: Context): AudioSettingsRepository =
        context.audioSettingsRepository()

    @Provides
    @Singleton
    fun provideAnkiSettingsRepository(@ApplicationContext context: Context): AnkiSettingsRepository =
        context.ankiSettingsRepository()

    @Provides
    @Singleton
    fun provideSasayakiSettingsRepository(@ApplicationContext context: Context): SasayakiSettingsRepository =
        context.sasayakiSettingsRepository()

    @Provides
    @Singleton
    fun provideSyncSettingsRepository(
        @ApplicationContext context: Context,
        drive: DriveSyncDataSource,
    ): SyncSettingsRepository =
        context.syncSettingsRepository(drive)

    @Provides
    @Singleton
    fun provideBookshelfSettingsRepository(@ApplicationContext context: Context): BookshelfSettingsRepository =
        context.bookshelfSettingsRepository()

    @Provides
    @Singleton
    fun provideUpdateSettingsRepository(@ApplicationContext context: Context): UpdateSettingsRepository =
        context.updateSettingsRepository()

    @Provides
    @Singleton
    fun provideUpdateDownloadStore(@ApplicationContext context: Context): UpdateDownloadStore =
        context.updateDownloadStore()

    @Provides
    @CurrentVersionName
    fun provideCurrentVersionName(): String = BuildConfig.VERSION_NAME
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
