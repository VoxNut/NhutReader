package com.nhut.hoshi.features.dictionary

import com.nhut.hoshi.dictionary.DictionaryInfo
import com.nhut.hoshi.dictionary.DictionaryType
import com.nhut.hoshi.dictionary.DictionaryUpdateCandidate
import com.nhut.hoshi.ui.UiText

internal data class DictionaryUiState(
    val selectedType: DictionaryType = DictionaryType.Term,
    val dictionaries: Map<DictionaryType, List<DictionaryInfo>> = emptyMap(),
    val updatableDictionaries: List<DictionaryUpdateCandidate> = emptyList(),
    val settings: DictionarySettings = DictionarySettings(),
    val mutationOperation: DictionaryMutationOperation? = null,
    val isImporting: Boolean = false,
    val isUpdating: Boolean = false,
    val showBlockingProgress: Boolean = false,
    val currentImportMessage: UiText? = null,
    val errorMessage: UiText? = null,
) {
    val isMutationInProgress: Boolean
        get() = mutationOperation != null

    val currentDictionaries: List<DictionaryInfo>
        get() = dictionaries[selectedType].orEmpty()
}

internal data class DictionaryListLayout(
    val dictionaryStartGlobalIndex: Int,
    val showErrorDialog: Boolean,
) {
    companion object {
        fun from(errorMessage: UiText?): DictionaryListLayout =
            DictionaryListLayout(
                dictionaryStartGlobalIndex = 1,
                showErrorDialog = errorMessage != null,
            )
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
