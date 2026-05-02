package com.nhut.hoshi.dictionary

object DictionaryManager {
    fun collectDictionaries(
        storedDicts: List<DictionaryInfo>,
        configDicts: List<DictionaryConfig.DictionaryEntry>,
    ): List<DictionaryInfo> {
        val result = mutableListOf<DictionaryInfo>()
        configDicts.sortedBy { it.order }.forEach { config ->
            val stored = storedDicts.firstOrNull { it.path.name == config.fileName } ?: return@forEach
            result += stored.copy(
                isEnabled = config.isEnabled,
                order = config.order,
            )
        }

        val collectedFileNames = result.mapTo(mutableSetOf()) { it.path.name }
        storedDicts.forEach { stored ->
            if (stored.path.name !in collectedFileNames) {
                result += stored.copy(
                    isEnabled = true,
                    order = result.size,
                )
            }
        }
        return result
    }

    fun moveDictionaries(
        dictionaries: List<DictionaryInfo>,
        fromIndex: Int,
        toIndex: Int,
    ): List<DictionaryConfig.DictionaryEntry> {
        if (dictionaries.isEmpty() || fromIndex !in dictionaries.indices) {
            return dictionaries.toConfigEntries()
        }
        val reordered = dictionaries.toMutableList()
        val moved = reordered.removeAt(fromIndex)
        reordered.add(toIndex.coerceIn(0, reordered.size), moved)
        return reordered.toConfigEntries()
    }

    private fun List<DictionaryInfo>.toConfigEntries(): List<DictionaryConfig.DictionaryEntry> =
        mapIndexed { index, dictionary ->
            DictionaryConfig.DictionaryEntry(
                fileName = dictionary.path.name,
                isEnabled = dictionary.isEnabled,
                order = index,
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
