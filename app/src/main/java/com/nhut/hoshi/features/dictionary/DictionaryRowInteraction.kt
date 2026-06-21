package com.nhut.hoshi.features.dictionary

internal object DictionaryRowInteraction {
    enum class Area {
        DragHandle,
        Content,
        EnableSwitch,
    }

    fun canRevealDeleteOnLongPress(area: Area): Boolean =
        area == Area.Content
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
