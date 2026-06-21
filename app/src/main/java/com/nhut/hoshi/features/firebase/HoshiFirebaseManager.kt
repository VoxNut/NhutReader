package com.nhut.hoshi.features.firebase

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.nhut.hoshi.features.anki.AnkiSettings
import com.nhut.hoshi.features.anki.AnkiSettingsRepository
import com.nhut.hoshi.features.anki.AnkiBackendKind
import com.nhut.hoshi.features.anki.AnkiDuplicateScope
import com.nhut.hoshi.features.reader.ReaderSettings
import com.nhut.hoshi.features.reader.ReaderSettingsRepository
import com.nhut.hoshi.features.reader.ReaderTheme
import com.nhut.hoshi.features.reader.ReaderInterfaceTheme
import com.nhut.hoshi.epub.BookRepository
import com.nhut.hoshi.epub.BookShelf

object HoshiFirebaseManager {
    private const val TAG = "HoshiFirebaseManager"

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private var googleSignInClient: GoogleSignInClient? = null

    private const val WEB_CLIENT_ID =
        "836831514413-cd4b13kqds0p9kqs8s5ad1er90bcubuf.apps.googleusercontent.com"

    fun initialize(context: Context) {
        _currentUser.value = auth.currentUser
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser
        }
        Log.d(TAG, "Firebase initialized. Current user: ${auth.currentUser?.email}")
    }

    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        return googleSignInClient ?: run {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_ID)
                .requestEmail()
                .build()
            GoogleSignIn.getClient(context, gso).also { googleSignInClient = it }
        }
    }

    suspend fun firebaseAuthWithGoogle(idToken: String) = withContext(Dispatchers.IO) {
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            _currentUser.value = result.user
            Log.d(TAG, "Google sign-in successful: ${result.user?.email}")
        } catch (e: Exception) {
            Log.e(TAG, "Google sign-in failed", e)
            throw e
        }
    }

    fun signOut(context: Context) {
        auth.signOut()
        getGoogleSignInClient(context).signOut()
        _currentUser.value = null
        Log.d(TAG, "User signed out")
    }

    fun saveTranslationHistory(
        text: String,
        translation: String,
        sourceLang: String,
        targetLang: String,
    ) {
        val userId = auth.currentUser?.uid ?: return
        try {
            val data = hashMapOf(
                "text" to text,
                "translation" to translation,
                "sourceLang" to sourceLang,
                "targetLang" to targetLang,
                "timestamp" to com.google.firebase.Timestamp.now(),
            )
            firestore.collection("users")
                .document(userId)
                .collection("translations")
                .add(data)
                .addOnSuccessListener {
                    Log.d(TAG, "Translation history saved successfully")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to save translation history to Firestore", e)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error in saveTranslationHistory", e)
        }
    }

    fun saveHighlight(
        bookId: String,
        chapterIndex: Int,
        text: String,
        note: String,
    ) {
        val userId = auth.currentUser?.uid ?: return
        try {
            val data = hashMapOf(
                "bookId" to bookId,
                "chapterIndex" to chapterIndex,
                "text" to text,
                "note" to note,
                "timestamp" to com.google.firebase.Timestamp.now(),
            )
            firestore.collection("users")
                .document(userId)
                .collection("highlights")
                .add(data)
                .addOnSuccessListener {
                    Log.d(TAG, "Highlight saved successfully")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to save highlight to Firestore", e)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error in saveHighlight", e)
        }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun loadTranslationHistory(): List<Map<String, Any>> = withContext(Dispatchers.IO) {
        val userId = auth.currentUser?.uid ?: return@withContext emptyList()
        try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("translations")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(100)
                .get()
                .await()
            snapshot.documents.mapNotNull { it.data }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load translation history", e)
            emptyList()
        }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun loadHighlights(bookId: String): List<Map<String, Any>> = withContext(Dispatchers.IO) {
        val userId = auth.currentUser?.uid ?: return@withContext emptyList()
        try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("highlights")
                .whereEqualTo("bookId", bookId)
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.mapNotNull { it.data }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load highlights", e)
            emptyList()
        }
    }

    fun saveReadingProgress(
        bookId: String,
        chapterIndex: Int,
        progress: Double,
        characterCount: Int,
        lastModified: Double?
    ) {
        val userId = auth.currentUser?.uid ?: return
        try {
            val data = hashMapOf(
                "bookId" to bookId,
                "chapterIndex" to chapterIndex,
                "progress" to progress,
                "characterCount" to characterCount,
                "lastModified" to (lastModified ?: 0.0),
                "timestamp" to com.google.firebase.Timestamp.now(),
            )
            firestore.collection("users")
                .document(userId)
                .collection("readingProgress")
                .document(bookId)
                .set(data)
                .addOnSuccessListener {
                    Log.d(TAG, "Reading progress saved for book: $bookId")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to save reading progress for book: $bookId", e)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error in saveReadingProgress", e)
        }
    }

    suspend fun loadReadingProgress(bookId: String): Map<String, Any>? = withContext(Dispatchers.IO) {
        val userId = auth.currentUser?.uid ?: return@withContext null
        try {
            val document = firestore.collection("users")
                .document(userId)
                .collection("readingProgress")
                .document(bookId)
                .get()
                .await()
            document.data
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load reading progress for book: $bookId", e)
            null
        }
    }

    fun saveAnkiMining(
        word: String,
        reading: String,
        sentence: String,
        meaning: String,
        deck: String,
        bookTitle: String,
    ) {
        val userId = auth.currentUser?.uid ?: return
        try {
            val data = hashMapOf(
                "word" to word,
                "reading" to reading,
                "sentence" to sentence,
                "meaning" to meaning,
                "deck" to deck,
                "bookTitle" to bookTitle,
                "timestamp" to com.google.firebase.Timestamp.now(),
            )
            firestore.collection("users")
                .document(userId)
                .collection("ankiHistory")
                .add(data)
                .addOnSuccessListener {
                    Log.d(TAG, "Anki mining history saved successfully")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to save Anki mining history", e)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error in saveAnkiMining", e)
        }
    }

    suspend fun loadAnkiHistory(): List<Map<String, Any>> = withContext(Dispatchers.IO) {
        val userId = auth.currentUser?.uid ?: return@withContext emptyList()
        try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("ankiHistory")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(100)
                .get()
                .await()
            snapshot.documents.mapNotNull { it.data }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load Anki mining history", e)
            emptyList()
        }
    }

    fun saveAnkiSettings(settings: AnkiSettings) {
        val userId = auth.currentUser?.uid ?: return
        try {
            val data = hashMapOf(
                "backendKind" to settings.backendKind.name,
                "selectedDeckId" to settings.selectedDeckId,
                "selectedDeckName" to settings.selectedDeckName,
                "selectedNoteTypeId" to settings.selectedNoteTypeId,
                "selectedNoteTypeName" to settings.selectedNoteTypeName,
                "tags" to settings.tags,
                "allowDupes" to settings.allowDupes,
                "checkDuplicatesAcrossAllModels" to settings.checkDuplicatesAcrossAllModels,
                "duplicateScope" to settings.duplicateScope.name,
                "compactGlossaries" to settings.compactGlossaries,
                "embedMedia" to settings.embedMedia,
                "ankiDroidForceSync" to settings.ankiDroidForceSync,
                "ankiConnectUrl" to settings.ankiConnectUrl,
                "ankiConnectForceSync" to settings.ankiConnectForceSync,
                "aiTranslateSentence" to settings.aiTranslateSentence,
                "aiTranslateField" to settings.aiTranslateField,
                "timestamp" to com.google.firebase.Timestamp.now()
            )
            firestore.collection("users")
                .document(userId)
                .collection("settings")
                .document("ankiSettings")
                .set(data)
                .addOnSuccessListener {
                    Log.d(TAG, "Anki settings saved successfully for user: $userId")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to save Anki settings to Firestore", e)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save Anki settings to Firestore", e)
        }
    }

    suspend fun loadAnkiSettings(): Map<String, Any>? = withContext(Dispatchers.IO) {
        val userId = auth.currentUser?.uid ?: return@withContext null
        try {
            val document = firestore.collection("users")
                .document(userId)
                .collection("settings")
                .document("ankiSettings")
                .get()
                .await()
            document.data
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load Anki settings from Firestore", e)
            null
        }
    }

    fun saveReaderSettings(settings: ReaderSettings) {
        val userId = auth.currentUser?.uid ?: return
        try {
            val data = hashMapOf(
                "theme" to settings.theme.name,
                "eInkMode" to settings.eInkMode,
                "uiTheme" to settings.uiTheme.name,
                "fontSize" to settings.fontSize,
                "verticalWriting" to settings.verticalWriting,
                "selectedFont" to settings.selectedFont,
                "hideFurigana" to settings.hideFurigana,
                "continuousMode" to settings.continuousMode,
                "lineHeight" to settings.lineHeight,
                "characterSpacing" to settings.characterSpacing,
                "paragraphSpacing" to settings.paragraphSpacing,
                "showTitle" to settings.showTitle,
                "showCharacters" to settings.showCharacters,
                "showPercentage" to settings.showPercentage,
                "timestamp" to com.google.firebase.Timestamp.now()
            )
            firestore.collection("users")
                .document(userId)
                .collection("settings")
                .document("readerSettings")
                .set(data)
                .addOnSuccessListener {
                    Log.d(TAG, "Reader settings saved successfully for user: $userId")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to save Reader settings to Firestore", e)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save Reader settings to Firestore", e)
        }
    }

    suspend fun loadReaderSettings(): Map<String, Any>? = withContext(Dispatchers.IO) {
        val userId = auth.currentUser?.uid ?: return@withContext null
        try {
            val document = firestore.collection("users")
                .document(userId)
                .collection("settings")
                .document("readerSettings")
                .get()
                .await()
            document.data
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load Reader settings from Firestore", e)
            null
        }
    }

    fun saveBookShelves(shelves: List<BookShelf>) {
        val userId = auth.currentUser?.uid ?: return
        try {
            val serializedShelves = shelves.map { shelf ->
                hashMapOf(
                    "name" to shelf.name,
                    "bookIds" to shelf.bookIds
                )
            }
            val data = hashMapOf(
                "shelves" to serializedShelves,
                "timestamp" to com.google.firebase.Timestamp.now()
            )
            firestore.collection("users")
                .document(userId)
                .collection("shelves")
                .document("bookShelves")
                .set(data)
                .addOnSuccessListener {
                    Log.d(TAG, "Book shelves saved successfully for user: $userId")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to save book shelves to Firestore", e)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save book shelves to Firestore", e)
        }
    }

    suspend fun loadBookShelves(): List<Map<String, Any>>? = withContext(Dispatchers.IO) {
        val userId = auth.currentUser?.uid ?: return@withContext null
        try {
            val document = firestore.collection("users")
                .document(userId)
                .collection("shelves")
                .document("bookShelves")
                .get()
                .await()
            val list = document.get("shelves") as? List<Map<String, Any>>
            list
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load book shelves from Firestore", e)
            null
        }
    }

    suspend fun restoreAllData(
        ankiSettingsRepository: AnkiSettingsRepository,
        readerSettingsRepository: ReaderSettingsRepository,
        bookRepository: BookRepository,
    ): Boolean = withContext(Dispatchers.IO) {
        val userId = auth.currentUser?.uid ?: return@withContext false
        try {
            // 1. Restore Anki Settings
            val rawAnki = loadAnkiSettings()
            if (rawAnki != null) {
                ankiSettingsRepository.update { current ->
                    current.copy(
                        backendKind = runCatching { AnkiBackendKind.valueOf(rawAnki["backendKind"] as String) }.getOrDefault(current.backendKind),
                        selectedDeckId = rawAnki["selectedDeckId"] as? Long,
                        selectedDeckName = rawAnki["selectedDeckName"] as? String,
                        selectedNoteTypeId = rawAnki["selectedNoteTypeId"] as? Long,
                        selectedNoteTypeName = rawAnki["selectedNoteTypeName"] as? String,
                        tags = rawAnki["tags"] as? String ?: current.tags,
                        allowDupes = rawAnki["allowDupes"] as? Boolean ?: current.allowDupes,
                        checkDuplicatesAcrossAllModels = rawAnki["checkDuplicatesAcrossAllModels"] as? Boolean ?: current.checkDuplicatesAcrossAllModels,
                        duplicateScope = runCatching { AnkiDuplicateScope.valueOf(rawAnki["duplicateScope"] as String) }.getOrDefault(current.duplicateScope),
                        compactGlossaries = rawAnki["compactGlossaries"] as? Boolean ?: current.compactGlossaries,
                        embedMedia = rawAnki["embedMedia"] as? Boolean ?: current.embedMedia,
                        ankiDroidForceSync = rawAnki["ankiDroidForceSync"] as? Boolean ?: current.ankiDroidForceSync,
                        ankiConnectUrl = rawAnki["ankiConnectUrl"] as? String ?: current.ankiConnectUrl,
                        ankiConnectForceSync = rawAnki["ankiConnectForceSync"] as? Boolean ?: current.ankiConnectForceSync,
                        aiTranslateSentence = rawAnki["aiTranslateSentence"] as? Boolean ?: current.aiTranslateSentence,
                        aiTranslateField = rawAnki["aiTranslateField"] as? String ?: current.aiTranslateField
                    )
                }
            }

            // 2. Restore Reader Settings
            val rawReader = loadReaderSettings()
            if (rawReader != null) {
                readerSettingsRepository.update { current ->
                    current.copy(
                        theme = runCatching { ReaderTheme.valueOf(rawReader["theme"] as String) }.getOrDefault(current.theme),
                        eInkMode = rawReader["eInkMode"] as? Boolean ?: current.eInkMode,
                        uiTheme = runCatching { ReaderInterfaceTheme.fromStorage(rawReader["uiTheme"] as String) }.getOrDefault(current.uiTheme),
                        fontSize = (rawReader["fontSize"] as? Long)?.toInt() ?: current.fontSize,
                        verticalWriting = rawReader["verticalWriting"] as? Boolean ?: current.verticalWriting,
                        selectedFont = rawReader["selectedFont"] as? String ?: current.selectedFont,
                        hideFurigana = rawReader["hideFurigana"] as? Boolean ?: current.hideFurigana,
                        continuousMode = rawReader["continuousMode"] as? Boolean ?: current.continuousMode,
                        lineHeight = rawReader["lineHeight"] as? Double ?: current.lineHeight,
                        characterSpacing = rawReader["characterSpacing"] as? Double ?: current.characterSpacing,
                        paragraphSpacing = rawReader["paragraphSpacing"] as? Double ?: current.paragraphSpacing,
                        showTitle = rawReader["showTitle"] as? Boolean ?: current.showTitle,
                        showCharacters = rawReader["showCharacters"] as? Boolean ?: current.showCharacters,
                        showPercentage = rawReader["showPercentage"] as? Boolean ?: current.showPercentage
                    )
                }
            }

            // 3. Restore Book Shelves
            val rawShelves = loadBookShelves()
            if (rawShelves != null) {
                val shelvesList = rawShelves.map { shelfMap ->
                    BookShelf(
                        name = shelfMap["name"] as? String ?: "",
                        bookIds = (shelfMap["bookIds"] as? List<*>)?.filterIsInstance<String>().orEmpty()
                    )
                }
                bookRepository.saveShelves(shelvesList)
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error in restoreAllData", e)
            false
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
