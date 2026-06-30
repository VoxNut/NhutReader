package com.nhut.hoshi.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.nhut.hoshi.features.anki.AndroidAnkiContentApi
import com.nhut.hoshi.features.anki.AnkiBackend
import com.nhut.hoshi.features.anki.AnkiContentApi
import com.nhut.hoshi.features.anki.AnkiDroidBackendAdapter
import com.nhut.hoshi.features.bookshelf.AndroidBookshelfRepository
import com.nhut.hoshi.features.bookshelf.BookshelfRepository
import com.nhut.hoshi.features.dictionary.AndroidDictionarySearchRepository
import com.nhut.hoshi.features.dictionary.AndroidDictionaryViewModelRepository
import com.nhut.hoshi.features.dictionary.DictionarySearchRepository
import com.nhut.hoshi.features.dictionary.DictionaryViewModelRepository
import com.nhut.hoshi.features.sync.DeviceCodeDriveAuthorizer
import com.nhut.hoshi.features.sync.DriveAccessTokenProvider
import com.nhut.hoshi.features.sync.DriveAuthorizer
import com.nhut.hoshi.features.sync.DriveSyncDataSource
import com.nhut.hoshi.features.sync.GoogleDriveClient
import com.nhut.hoshi.features.update.AndroidUpdateDownloadManager
import com.nhut.hoshi.features.update.GitHubReleaseUpdateRepository
import com.nhut.hoshi.features.update.ReleaseUpdateRepository
import com.nhut.hoshi.features.update.UpdateDownloadController

@Module
@InstallIn(SingletonComponent::class)
internal interface HoshiBindingsModule {
    @Binds
    @Singleton
    fun bindDriveAuthorizer(authorizer: DeviceCodeDriveAuthorizer): DriveAuthorizer

    @Binds
    @Singleton
    fun bindDriveAccessTokenProvider(authorizer: DeviceCodeDriveAuthorizer): DriveAccessTokenProvider

    @Binds
    @Singleton
    fun bindDriveSyncDataSource(client: GoogleDriveClient): DriveSyncDataSource

    @Binds
    @Singleton
    fun bindBookshelfRepository(repository: AndroidBookshelfRepository): BookshelfRepository

    @Binds
    @Singleton
    fun bindDictionaryViewModelRepository(repository: AndroidDictionaryViewModelRepository): DictionaryViewModelRepository

    @Binds
    @Singleton
    fun bindDictionarySearchRepository(repository: AndroidDictionarySearchRepository): DictionarySearchRepository

    @Binds
    @Singleton
    fun bindAnkiContentApi(api: AndroidAnkiContentApi): AnkiContentApi

    @Binds
    @Singleton
    fun bindAnkiBackend(backend: AnkiDroidBackendAdapter): AnkiBackend

    @Binds
    @Singleton
    fun bindUpdateDownloadController(manager: AndroidUpdateDownloadManager): UpdateDownloadController

    @Binds
    @Singleton
    fun bindReleaseUpdateRepository(repository: GitHubReleaseUpdateRepository): ReleaseUpdateRepository
}
