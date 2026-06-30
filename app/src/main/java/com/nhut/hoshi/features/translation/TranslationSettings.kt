package com.nhut.hoshi.features.translation

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class TranslationSettings(
    val geminiApiKey: String = "",
    val targetLanguage: String = "Vietnamese",
    val autoTranslate: Boolean = true,
    val geminiModel: String = "gemini-2.5-flash"
) {
    companion object {
        const val DEFAULT_GEMINI_KEY = ""
        
        val LANGUAGES = listOf(
            "English",
            "Vietnamese",
            "Japanese",
            "Chinese (Simplified)",
            "Chinese (Traditional)",
            "Korean",
            "French",
            "German",
            "Spanish"
        )

        val MODELS = listOf(
            "gemini-2.5-flash",
            "gemini-2.5-pro",
            "gemini-2.5-flash-lite",
            "gemini-3-flash-preview"
        )
    }
}

val Context.translationSettingsDataStore by preferencesDataStore(name = "translation-settings")

class TranslationSettingsRepository(private val dataStore: DataStore<Preferences>) {
    
    val settings: Flow<TranslationSettings> = dataStore.data.map { preferences ->
        TranslationSettings(
            geminiApiKey = preferences[KEY_GEMINI_API_KEY] ?: "",
            targetLanguage = preferences[KEY_TARGET_LANGUAGE] ?: "Vietnamese",
            autoTranslate = preferences[KEY_AUTO_TRANSLATE] ?: true,
            geminiModel = preferences[KEY_GEMINI_MODEL] ?: "gemini-2.5-flash"
        )
    }
    
    suspend fun updateGeminiApiKey(key: String) {
        dataStore.edit { preferences ->
            preferences[KEY_GEMINI_API_KEY] = key
        }
    }
    
    suspend fun updateTargetLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[KEY_TARGET_LANGUAGE] = language
        }
    }
    
    suspend fun updateAutoTranslate(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_AUTO_TRANSLATE] = enabled
        }
    }

    suspend fun updateGeminiModel(model: String) {
        dataStore.edit { preferences ->
            preferences[KEY_GEMINI_MODEL] = model
        }
    }
    
    companion object {
        private val KEY_GEMINI_API_KEY = stringPreferencesKey("gemini_api_key")
        private val KEY_TARGET_LANGUAGE = stringPreferencesKey("target_language")
        private val KEY_AUTO_TRANSLATE = booleanPreferencesKey("auto_translate")
        private val KEY_GEMINI_MODEL = stringPreferencesKey("gemini_model")
    }
}
