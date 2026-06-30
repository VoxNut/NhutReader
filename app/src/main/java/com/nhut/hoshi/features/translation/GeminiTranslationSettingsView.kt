package com.nhut.hoshi.features.translation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.nhut.hoshi.R
import com.nhut.hoshi.features.settings.GroupCard
import com.nhut.hoshi.features.settings.GroupDivider
import com.nhut.hoshi.features.settings.SectionTitle
import com.nhut.hoshi.features.settings.SettingsDetailScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeminiTranslationSettingsView(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val repository = remember { TranslationSettingsRepository(context.translationSettingsDataStore) }
    val settings by repository.settings.collectAsState(initial = TranslationSettings())
    val scope = rememberCoroutineScope()
    var apiKeyInput by remember(settings.geminiApiKey) { mutableStateOf(settings.geminiApiKey) }
    var languageDropdownExpanded by remember { mutableStateOf(false) }
    var modelDropdownExpanded by remember { mutableStateOf(false) }
    var apiKeyVisible by remember { mutableStateOf(false) }

    SettingsDetailScaffold(
        title = stringResource(R.string.gemini_settings_title),
        onClose = onClose,
        modifier = modifier.fillMaxSize(),
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

            SectionTitle(stringResource(R.string.gemini_api_key))
            GroupCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = apiKeyInput,
                        onValueChange = { apiKeyInput = it },
                        label = { Text(stringResource(R.string.gemini_api_key_hint)) },
                        visualTransformation = if (apiKeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { apiKeyVisible = !apiKeyVisible }) {
                                Icon(
                                    imageVector = if (apiKeyVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = stringResource(R.string.gemini_api_key_visibility)
                                )
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        androidx.compose.material3.TextButton(
                            onClick = {
                                scope.launch {
                                    repository.updateGeminiApiKey(apiKeyInput)
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.gemini_api_key_saved),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                        ) {
                            Text(stringResource(R.string.action_save))
                        }
                    }
                    if (settings.geminiApiKey.isEmpty()) {
                        Text(
                            text = stringResource(R.string.gemini_using_default_key),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            SectionTitle(stringResource(R.string.gemini_model_title))
            GroupCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    ExposedDropdownMenuBox(
                        expanded = modelDropdownExpanded,
                        onExpandedChange = { modelDropdownExpanded = it },
                    ) {
                        OutlinedTextField(
                            value = settings.geminiModel,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.gemini_model_title)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = modelDropdownExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        )
                        ExposedDropdownMenu(
                            expanded = modelDropdownExpanded,
                            onDismissRequest = { modelDropdownExpanded = false },
                        ) {
                            TranslationSettings.MODELS.forEach { model ->
                                DropdownMenuItem(
                                    text = { Text(model) },
                                    onClick = {
                                        scope.launch {
                                            repository.updateGeminiModel(model)
                                        }
                                        modelDropdownExpanded = false
                                    },
                                )
                            }
                        }
                    }
                }
            }

            SectionTitle(stringResource(R.string.gemini_target_language))
            GroupCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    ExposedDropdownMenuBox(
                        expanded = languageDropdownExpanded,
                        onExpandedChange = { languageDropdownExpanded = it },
                    ) {
                        OutlinedTextField(
                            value = settings.targetLanguage,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.gemini_target_language)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = languageDropdownExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        )
                        ExposedDropdownMenu(
                            expanded = languageDropdownExpanded,
                            onDismissRequest = { languageDropdownExpanded = false },
                        ) {
                            TranslationSettings.LANGUAGES.forEach { language ->
                                DropdownMenuItem(
                                    text = { Text(language) },
                                    onClick = {
                                        scope.launch {
                                            repository.updateTargetLanguage(language)
                                        }
                                        languageDropdownExpanded = false
                                    },
                                )
                            }
                        }
                    }
                }
            }

            GroupCard {
                ListItem(
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    headlineContent = { Text(stringResource(R.string.gemini_auto_translate)) },
                    trailingContent = {
                        Switch(
                            checked = settings.autoTranslate,
                            onCheckedChange = { checked ->
                                scope.launch {
                                    repository.updateAutoTranslate(checked)
                                }
                            },
                        )
                    },
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

