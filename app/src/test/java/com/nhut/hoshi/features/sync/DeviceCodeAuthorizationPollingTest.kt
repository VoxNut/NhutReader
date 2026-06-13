package com.nhut.hoshi.features.sync

import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DeviceCodeAuthorizationPollingTest {
    @Test
    fun transientNetworkFailuresContinuePollingWithBackoff() {
        assertTrue(UnknownHostException("oauth2.googleapis.com").isTransientDeviceCodePollingFailure())
        assertTrue(SocketTimeoutException("timeout").isTransientDeviceCodePollingFailure())
        assertTrue(IOException("temporary network failure").isTransientDeviceCodePollingFailure())
        assertFalse(DriveAuthException("invalid client").isTransientDeviceCodePollingFailure())

        assertEquals(
            DriveAuthorizationResult.TransientNetworkFailure,
            UnknownHostException("oauth2.googleapis.com").toDeviceCodePollingFailureResult(),
        )
        assertEquals(10L, nextDeviceCodePollIntervalSeconds(5, DriveAuthorizationResult.TransientNetworkFailure))
        assertEquals(60L, nextDeviceCodePollIntervalSeconds(60, DriveAuthorizationResult.TransientNetworkFailure))
    }

    @Test
    fun oauthSlowDownUsesGoogleRequiredIncrement() {
        assertEquals(10L, nextDeviceCodePollIntervalSeconds(5, DriveAuthorizationResult.SlowDown))
        assertEquals(5L, nextDeviceCodePollIntervalSeconds(5, DriveAuthorizationResult.Pending))
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
