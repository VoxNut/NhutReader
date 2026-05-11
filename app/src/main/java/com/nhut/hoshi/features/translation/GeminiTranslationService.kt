package com.nhut.hoshi.features.translation

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class GeminiTranslationService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun translate(
        text: String,
        contextSentence: String,
        sourceLanguage: String,
        targetLanguage: String,
        userApiKey: String = "",
        modelName: String = "gemini-2.5-flash",
        ankiKeyword: String = ""
    ): GeminiTranslationResult = withContext(Dispatchers.IO) {
        val apiKey = userApiKey.trim().ifEmpty { TranslationSettings.DEFAULT_GEMINI_KEY.trim() }
        
        val customFormatting = if (ankiKeyword.isNotBlank()) {
            """
            
            Additional formatting for the value of "translated_text":
            - Wrap the entire translated sentence in <span class="group">...</span>.
            - Inside the sentence, wrap the exact English translation of the key Japanese word "$ankiKeyword" in <span class="highlight">...</span>.
            - Do not wrap other words.
            - Example: 
              Source: "牛乳やチーズを使った料理 là món ăn dùng..."
              Key Word: "牛乳"
              "translated_text": "<span class=\"group\">I cannot eat dishes that use <span class=\"highlight\">milk</span> or cheese.</span>"
            """.trimIndent()
        } else ""

        val systemPrompt = """
            You are a translation assistant for book readers.
            Translate the input text from $sourceLanguage to $targetLanguage.
            
            Context sentence / surrounding text:
            "$contextSentence"
            
            Input Text to translate:
            "$text"
            
            Format your response strictly as a JSON object with these keys:
            - "translated_text": the translated text in $targetLanguage$customFormatting
            - "romaji": romaji transcription (only if source is Japanese, else empty string)
            - "grammar_note": a brief grammar explanation of interesting structures/vocabulary in the text (keep it under 3 sentences, in $targetLanguage)
            - "jlpt_level": a JLPT level ("N5", "N4", "N3", "N2", "N1" or "") for key Japanese words (only if source is Japanese, else empty string)
        """.trimIndent()
 
        val url = "https://generativelanguage.googleapis.com/v1beta/models/$modelName:generateContent?key=$apiKey"

        val partObj = JSONObject().put("text", systemPrompt)
        val partsArray = JSONArray().put(partObj)
        val contentObj = JSONObject().put("parts", partsArray)
        val contentsArray = JSONArray().put(contentObj)
        val genConfig = JSONObject().put("responseMimeType", "application/json")
        val requestBodyJson = JSONObject()
            .put("contents", contentsArray)
            .put("generationConfig", genConfig)

        val requestBody = requestBodyJson.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errBody = response.body?.string()
                    Log.e("GeminiService", "HTTP error ${response.code}: $errBody")
                    val errorMsg = try {
                        val json = JSONObject(errBody ?: "")
                        json.getJSONObject("error").getString("message")
                    } catch (e: Exception) {
                        "HTTP error ${response.code}"
                    }
                    return@withContext GeminiTranslationResult(
                        translatedText = "Translation failed: ${response.code}",
                        romaji = "",
                        grammarNote = when (response.code) {
                            503 -> "This model is currently experiencing high demand ($errorMsg). Please try again or select a different Gemini model in Settings."
                            429 -> "Rate limit or quota exceeded ($errorMsg). Please check your API key/billing or select a different Gemini model in Settings."
                            404 -> "Model not found ($errorMsg). Please verify the Gemini model selected in settings."
                            else -> "Please verify your network connection and Gemini API Key settings ($errorMsg)."
                        },
                        jlptLevel = ""
                    )
                }
                val responseBody = response.body?.string() ?: throw IOException("Empty response body")

                val jsonResponse = JSONObject(responseBody)
                val candidates = jsonResponse.getJSONArray("candidates")
                val firstCandidate = candidates.getJSONObject(0)
                val content = firstCandidate.getJSONObject("content")
                val parts = content.getJSONArray("parts")
                val responseText = parts.getJSONObject(0).getString("text").trim()

                val geminiResult = JSONObject(responseText)
                val translated = geminiResult.optString("translated_text", "")
                val romaji = geminiResult.optString("romaji", "")
                val grammar = geminiResult.optString("grammar_note", "")
                val jlpt = geminiResult.optString("jlpt_level", "")

                return@withContext GeminiTranslationResult(
                    translatedText = translated,
                    romaji = romaji,
                    grammarNote = grammar,
                    jlptLevel = jlpt
                )
            }
        } catch (e: Exception) {
            Log.e("GeminiService", "Translation failed: ${e.message}", e)
            return@withContext GeminiTranslationResult(
                translatedText = "Translation failed: ${e.message}",
                romaji = "",
                grammarNote = "Please verify your network connection and Gemini API Key settings.",
                jlptLevel = ""
            )
        }
    }
}

data class GeminiTranslationResult(
    val translatedText: String,
    val romaji: String,
    val grammarNote: String,
    val jlptLevel: String
)

// region DEBUG_MOCK_SECTION
// HỆ THỐNG KIỂM THỬ TẠM THỜI - SẼ ĐƯỢC DỌN DẸP TRƯỚC KHI RELEASE
// val debugSessionId = java.util.UUID.randomUUID().toString()
// fun performLocalIntegrityCheck(): Boolean {
//     val checkTime = System.currentTimeMillis()
//     android.util.Log.d("HoshiDebug", "Checking integrity at $checkTime")
//     return true
// }
// endregion DEBUG_MOCK_SECTION
