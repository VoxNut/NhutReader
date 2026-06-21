package com.nhut.hoshi.features.firebase

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch
import com.nhut.hoshi.R
import com.nhut.hoshi.LocalHoshiUiDependencies
import com.nhut.hoshi.features.anki.ankiSettingsRepository
import com.nhut.hoshi.features.settings.GroupCard
import com.nhut.hoshi.features.settings.SectionTitle
import com.nhut.hoshi.features.settings.SettingsDetailScaffold
import com.nhut.hoshi.ui.HoshiBlockingProgressOverlay

@Composable
fun FirebaseSettingsView(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val currentUser by HoshiFirebaseManager.currentUser.collectAsState()
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSigningIn by remember { mutableStateOf(false) }
    val appContainer = LocalHoshiUiDependencies.current
    var statusMessage by remember { mutableStateOf<String?>(null) }

    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (idToken != null) {
                    scope.launch {
                        try {
                            isSigningIn = true
                            HoshiFirebaseManager.firebaseAuthWithGoogle(idToken)
                            errorMessage = null
                        } catch (e: Exception) {
                            errorMessage = e.message
                        } finally {
                            isSigningIn = false
                        }
                    }
                } else {
                    errorMessage = "No ID token received"
                }
            } catch (e: ApiException) {
                errorMessage = "Google Sign-In failed: ${e.statusCode}"
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        SettingsDetailScaffold(
            title = stringResource(R.string.firebase_settings_title),
            onClose = onClose,
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                SectionTitle(stringResource(R.string.firebase_account))
                GroupCard {
                    if (currentUser != null) {
                        ListItem(
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                            headlineContent = {
                                Text(
                                    stringResource(
                                        R.string.firebase_signed_in_as,
                                        currentUser?.displayName ?: currentUser?.email ?: "Unknown",
                                    ),
                                )
                            },
                            supportingContent = {
                                currentUser?.email?.let { email ->
                                    Text(email)
                                }
                            },
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
                            OutlinedButton(
                                onClick = {
                                    HoshiFirebaseManager.signOut(context)
                                    errorMessage = null
                                },
                                enabled = !isSigningIn,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(stringResource(R.string.action_sign_out))
                            }
                        }
                    } else {
                        ListItem(
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                            headlineContent = {
                                Text(stringResource(R.string.firebase_not_signed_in))
                            },
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
                            Button(
                                onClick = {
                                    if (!isSigningIn) {
                                        val signInClient = HoshiFirebaseManager.getGoogleSignInClient(context)
                                        signInLauncher.launch(signInClient.signInIntent)
                                    }
                                },
                                enabled = !isSigningIn,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(stringResource(R.string.firebase_sign_in))
                            }
                        }
                    }
                }

                if (currentUser != null) {
                    SectionTitle("Cloud Synchronization")
                    GroupCard {
                        ListItem(
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                            headlineContent = {
                                Text("Restore Data from Cloud")
                            },
                            supportingContent = {
                                Text("Sync and restore your Anki configs, Reader preferences, and Library shelves from Firebase.")
                            }
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
                            Button(
                                onClick = {
                                    scope.launch {
                                        try {
                                            isSigningIn = true
                                            statusMessage = null
                                            errorMessage = null
                                            val ankiRepo = context.ankiSettingsRepository()
                                            val readerRepo = appContainer.readerSettingsRepository
                                            val bookRepo = appContainer.bookRepository
                                            val success = HoshiFirebaseManager.restoreAllData(
                                                ankiSettingsRepository = ankiRepo,
                                                readerSettingsRepository = readerRepo,
                                                bookRepository = bookRepo
                                            )
                                            if (success) {
                                                statusMessage = "All data restored successfully from cloud!"
                                            } else {
                                                errorMessage = "Failed to restore data from cloud or no data found."
                                            }
                                        } catch (e: Exception) {
                                            errorMessage = "Error: ${e.message}"
                                        } finally {
                                            isSigningIn = false
                                        }
                                    }
                                },
                                enabled = !isSigningIn,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Restore Data")
                            }
                        }
                    }
                }

                if (statusMessage != null) {
                    GroupCard {
                        ListItem(
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                            headlineContent = {
                                Text(
                                    statusMessage ?: "",
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            },
                        )
                    }
                }

                if (errorMessage != null) {
                    GroupCard {
                        ListItem(
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                            headlineContent = {
                                Text(
                                    stringResource(R.string.firebase_sign_in_failed, errorMessage ?: ""),
                                    color = MaterialTheme.colorScheme.error,
                                )
                            },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if (isSigningIn) {
            HoshiBlockingProgressOverlay(
                message = stringResource(R.string.loading)
            )
        }
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
