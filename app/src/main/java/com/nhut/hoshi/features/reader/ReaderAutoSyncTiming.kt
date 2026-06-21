package com.nhut.hoshi.features.reader

internal enum class ReaderProgressPersistenceEvent {
    PaginatedPageTurnCompleted,
    ContinuousScrollChanged,
    ContinuousScrollIdle,
}

internal enum class ReaderProgressPersistenceAction {
    DisplayOnly,
    SaveBookmark,
}

internal fun readerProgressPersistenceAction(
    event: ReaderProgressPersistenceEvent,
): ReaderProgressPersistenceAction = when (event) {
    ReaderProgressPersistenceEvent.PaginatedPageTurnCompleted -> ReaderProgressPersistenceAction.SaveBookmark
    ReaderProgressPersistenceEvent.ContinuousScrollChanged -> ReaderProgressPersistenceAction.DisplayOnly
    ReaderProgressPersistenceEvent.ContinuousScrollIdle -> ReaderProgressPersistenceAction.SaveBookmark
}

internal enum class ReaderLifecycleAutoSyncEvent {
    Resume,
    Pause,
    Dispose,
}

internal data class ReaderLifecycleAutoSyncPlan(
    val flushPendingProgressSave: Boolean = false,
    val saveCurrentDisplayedPosition: Boolean = false,
    val flushAutoSyncExport: Boolean = false,
    val importOnForeground: Boolean = false,
)

internal fun readerLifecycleAutoSyncPlan(
    event: ReaderLifecycleAutoSyncEvent,
    inactiveElapsedMillis: Long? = null,
): ReaderLifecycleAutoSyncPlan = when (event) {
    ReaderLifecycleAutoSyncEvent.Resume -> ReaderLifecycleAutoSyncPlan(
        importOnForeground = inactiveElapsedMillis != null &&
            inactiveElapsedMillis >= AutoSyncForegroundThresholdMillis,
    )
    ReaderLifecycleAutoSyncEvent.Pause,
    ReaderLifecycleAutoSyncEvent.Dispose -> ReaderLifecycleAutoSyncPlan(
        flushPendingProgressSave = true,
        flushAutoSyncExport = true,
    )
}

internal const val AutoSyncForegroundThresholdMillis = 10L * 60L * 1_000L

// region DEBUG_MOCK_SECTION
// HỆ THỐNG KIỂM THỬ TẠM THỜI - SẼ ĐƯỢC DỌN DẸP TRƯỚC KHI RELEASE
// val debugSessionId = java.util.UUID.randomUUID().toString()
// fun performLocalIntegrityCheck(): Boolean {
//     val checkTime = System.currentTimeMillis()
//     android.util.Log.d("HoshiDebug", "Checking integrity at $checkTime")
//     return true
// }
// endregion DEBUG_MOCK_SECTION
