package com.nhut.hoshi.features.bookshelf

import com.nhut.hoshi.epub.BookEntry
import com.nhut.hoshi.epub.BookShelf
import com.nhut.hoshi.epub.BookSortOption
import com.nhut.hoshi.ui.UiText

data class BookshelfUiState(
    val bookEntries: List<BookEntry> = emptyList(),
    val remoteBookEntries: List<RemoteBookEntry> = emptyList(),
    val bookProgressById: Map<String, Double> = emptyMap(),
    val remoteProgressById: Map<String, Double> = emptyMap(),
    val remoteImportProgressById: Map<String, Double> = emptyMap(),
    val remoteBusyBookIds: Set<String> = emptySet(),
    val coverSourcesById: Map<String, BookCoverSource> = emptyMap(),
    val remoteCoverSourcesById: Map<String, BookCoverSource> = emptyMap(),
    val shelves: List<BookShelf> = emptyList(),
    val sections: List<BookshelfSectionModel> = emptyList(),
    val sortOption: BookSortOption = BookSortOption.Recent,
    val showReading: Boolean = false,
    val isSelecting: Boolean = false,
    val selectedBookIds: Set<String> = emptySet(),
    val shelfExpansionState: Map<String, Boolean> = emptyMap(),
    val sasayakiEnabled: Boolean = false,
    val hasLoadedBooks: Boolean = false,
    val isLoading: Boolean = false,
    val blockingProgressMessage: UiText? = null,
    val statusMessage: UiText? = null,
    val errorMessage: UiText? = null,
    val openReaderBookId: String? = null,
)

data class RemoteBookEntry(
    val id: String,
    val folderId: String,
    val folderName: String,
    val title: String,
    val syncFiles: com.nhut.hoshi.features.sync.DriveSyncFiles,
)

data class BookCoverSource(
    val path: String,
    val cacheKey: String,
)

data class BookshelfLoadResult(
    val entries: List<BookEntry>,
    val progressById: Map<String, Double>,
    val coverSourcesById: Map<String, BookCoverSource>,
    val shelves: List<BookShelf>,
    val settings: BookshelfSettings,
)

data class RemoteBookshelfLoadResult(
    val remoteEntries: List<RemoteBookEntry> = emptyList(),
    val remoteProgressById: Map<String, Double> = emptyMap(),
    val remoteCoverSourcesById: Map<String, BookCoverSource> = emptyMap(),
)
