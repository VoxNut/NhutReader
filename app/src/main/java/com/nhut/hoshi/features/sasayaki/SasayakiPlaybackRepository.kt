package com.nhut.hoshi.features.sasayaki

import com.nhut.hoshi.epub.SasayakiPlaybackData

import com.nhut.hoshi.epub.SasayakiSidecarRepository
import java.io.File

interface SasayakiPlaybackRepository {
    suspend fun load(): SasayakiPlaybackData?
    suspend fun save(playback: SasayakiPlaybackData)
}

class BookSasayakiPlaybackRepository(
    private val bookRoot: File,
    private val sidecarRepository: SasayakiSidecarRepository,
) : SasayakiPlaybackRepository {
    override suspend fun load(): SasayakiPlaybackData? =
        sidecarRepository.loadSasayakiPlayback(bookRoot)

    override suspend fun save(playback: SasayakiPlaybackData) {
        sidecarRepository.saveSasayakiPlayback(bookRoot, playback)
    }
}
