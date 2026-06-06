package com.nhut.hoshi.features.sync

import java.io.IOException

internal fun Throwable.isTransientDeviceCodePollingFailure(): Boolean = this is IOException

internal fun Throwable.toDeviceCodePollingFailureResult(): DriveAuthorizationResult =
    if (isTransientDeviceCodePollingFailure()) {
        DriveAuthorizationResult.TransientNetworkFailure
    } else {
        DriveAuthorizationResult.Failed(message ?: "Google Drive authorization failed.")
    }

internal fun nextDeviceCodePollIntervalSeconds(
    currentIntervalSeconds: Long,
    result: DriveAuthorizationResult,
): Long =
    when (result) {
        DriveAuthorizationResult.SlowDown -> currentIntervalSeconds + DeviceCodeDriveAuthorizer.SlowDownIncrementSeconds
        DriveAuthorizationResult.TransientNetworkFailure ->
            (currentIntervalSeconds * DeviceCodeDriveAuthorizer.TransientNetworkBackoffMultiplier)
                .coerceAtMost(DeviceCodeDriveAuthorizer.MaxTransientNetworkBackoffSeconds)
        else -> currentIntervalSeconds
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
