package com.nhut.hoshi.features.anki

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhut.hoshi.R
import com.nhut.hoshi.features.firebase.HoshiFirebaseManager
import com.nhut.hoshi.features.settings.SettingsDetailScaffold
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnkiHistoryView(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var miningHistory by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    var searchQuery by remember { mutableStateOf("") }
    var selectedBookFilter by remember { mutableStateOf("All Books") }
    var sortBy by remember { mutableStateOf("Newest") } // Newest, Oldest, Alphabetical

    var isBookFilterExpanded by remember { mutableStateOf(false) }
    var isSortExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        miningHistory = HoshiFirebaseManager.loadAnkiHistory()
        isLoading = false
    }

    val bookList = remember(miningHistory) {
        listOf("All Books") + miningHistory.mapNotNull { it["bookTitle"] as? String }.filter { it.isNotEmpty() }.distinct()
    }

    // Filter and Sort history
    val filteredAndSortedHistory = remember(miningHistory, searchQuery, selectedBookFilter, sortBy) {
        var list = miningHistory.filter { card ->
            val word = card["word"] as? String ?: ""
            val reading = card["reading"] as? String ?: ""
            val sentence = card["sentence"] as? String ?: ""
            val meaning = card["meaning"] as? String ?: ""
            val bookTitle = card["bookTitle"] as? String ?: ""
            
            val matchesQuery = word.contains(searchQuery, ignoreCase = true) ||
                    reading.contains(searchQuery, ignoreCase = true) ||
                    sentence.contains(searchQuery, ignoreCase = true) ||
                    meaning.contains(searchQuery, ignoreCase = true) ||
                    bookTitle.contains(searchQuery, ignoreCase = true)

            val matchesBook = selectedBookFilter == "All Books" || bookTitle == selectedBookFilter

            matchesQuery && matchesBook
        }

        list = when (sortBy) {
            "Oldest" -> list.sortedBy { (it["timestamp"] as? com.google.firebase.Timestamp)?.seconds ?: 0L }
            "Alphabetical" -> list.sortedBy { (it["word"] as? String ?: "").lowercase() }
            else -> list.sortedByDescending { (it["timestamp"] as? com.google.firebase.Timestamp)?.seconds ?: 0L }
        }

        list
    }

    val totalCards = miningHistory.size
    val uniqueBooks = remember(miningHistory) {
        miningHistory.mapNotNull { it["bookTitle"] as? String }.filter { it.isNotEmpty() }.distinct().size
    }

    SettingsDetailScaffold(
        title = stringResource(R.string.anki_history_title),
        onClose = onClose,
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Summary Stats Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$totalCards",
                                        style = MaterialTheme.typography.headlineLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = stringResource(R.string.anki_history_stat_total_cards),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .height(48.dp)
                                        .width(1.dp)
                                        .align(Alignment.CenterVertically)
                                )
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$uniqueBooks",
                                        style = MaterialTheme.typography.headlineLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = stringResource(R.string.anki_history_stat_books),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }
                    }

                    // Search & Filter controls
                    item {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(stringResource(R.string.anki_history_search_hint)) },
                            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Rounded.Close, contentDescription = null)
                                    }
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Filter Book Dropdown
                            Box(modifier = Modifier.weight(1f)) {
                                Button(
                                    onClick = { isBookFilterExpanded = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            Icons.Rounded.FilterList,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (selectedBookFilter == "All Books") "All Books" else selectedBookFilter.take(15) + if (selectedBookFilter.length > 15) "..." else "",
                                            maxLines = 1
                                        )
                                    }
                                }
                                DropdownMenu(
                                    expanded = isBookFilterExpanded,
                                    onDismissRequest = { isBookFilterExpanded = false }
                                ) {
                                    bookList.forEach { book ->
                                        DropdownMenuItem(
                                            text = { Text(book) },
                                            onClick = {
                                                selectedBookFilter = book
                                                isBookFilterExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Sort Dropdown
                            Box(modifier = Modifier.weight(1f)) {
                                Button(
                                    onClick = { isSortExpanded = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            Icons.Rounded.Sort,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = when (sortBy) {
                                                "Oldest" -> stringResource(R.string.anki_history_sort_oldest)
                                                "Alphabetical" -> stringResource(R.string.anki_history_sort_alphabetical)
                                                else -> stringResource(R.string.anki_history_sort_newest)
                                            }
                                        )
                                    }
                                }
                                DropdownMenu(
                                    expanded = isSortExpanded,
                                    onDismissRequest = { isSortExpanded = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.anki_history_sort_newest)) },
                                        onClick = {
                                            sortBy = "Newest"
                                            isSortExpanded = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.anki_history_sort_oldest)) },
                                        onClick = {
                                            sortBy = "Oldest"
                                            isSortExpanded = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.anki_history_sort_alphabetical)) },
                                        onClick = {
                                            sortBy = "Alphabetical"
                                            isSortExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Card List
                    if (filteredAndSortedHistory.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No cards match your search/filter.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        items(
                            items = filteredAndSortedHistory,
                            key = { it["timestamp"]?.toString() ?: it["word"].toString() + Math.random() }
                        ) { card ->
                            AnkiHistoryCard(card = card)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnkiHistoryCard(card: Map<String, Any>) {
    val word = card["word"] as? String ?: ""
    val reading = card["reading"] as? String ?: ""
    val sentence = card["sentence"] as? String ?: ""
    val meaning = card["meaning"] as? String ?: ""
    val deck = card["deck"] as? String ?: ""
    val bookTitle = card["bookTitle"] as? String ?: "Unknown Book"
    val timestamp = card["timestamp"] as? com.google.firebase.Timestamp

    var isExpanded by remember { mutableStateOf(false) }

    val formattedDate = remember(timestamp) {
        if (timestamp != null) {
            val date = timestamp.toDate()
            val sdf = SimpleDateFormat("MMM d, yyyy, h:mm a", Locale.US)
            sdf.format(date)
        } else {
            "Unknown date"
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Word + Reading, Deck tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = word,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (reading.isNotEmpty()) {
                        Text(
                            text = reading,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                if (deck.isNotEmpty()) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = deck,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Book title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Rounded.Book,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = bookTitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }

            // Context Sentence
            if (sentence.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = sentence,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            // Date added
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (meaning.isNotEmpty()) {
                    TextButton(
                        onClick = { isExpanded = !isExpanded },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = if (isExpanded) stringResource(R.string.anki_history_hide_meaning) else stringResource(R.string.anki_history_show_meaning),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

            // Meaning Details (collapsible)
            if (isExpanded && meaning.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                val textColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()
                AndroidView(
                    factory = { context ->
                        android.widget.TextView(context).apply {
                            textSize = 14f
                            setTextColor(textColor)
                            text = HtmlCompat.fromHtml(meaning, HtmlCompat.FROM_HTML_MODE_LEGACY)
                        }
                    },
                    modifier = Modifier.padding(horizontal = 4.dp).fillMaxWidth()
                )
            }
        }
    }
}
