package com.example.team6mobileapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.team6mobileapp.model.Artikel
import com.example.team6mobileapp.network.ArtikelApiService
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtikelListScreen(onNavigateBack: () -> Unit, onNavigateToDetail: (Artikel) -> Unit) {
    val scope = rememberCoroutineScope()
    var artikels by remember { mutableStateOf<List<Artikel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val apiService = remember { ArtikelApiService.create() }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                artikels = apiService.getArtikel()
            } catch (e: Exception) {
                errorMessage = "Failed to load articles: ${e.message}"
                // DUMMY DATA for demonstration as requested in the issue
                artikels = listOf(
                    Artikel(1, "Ackersalat", "kg", 5612, 0),
                    Artikel(4, "Mangold", "kg", 3580, 0),
                    Artikel(12, "Kürbis", "Stk.", 6, 0),
                    Artikel(2, "Eisbergsalat", "kg", 3043, 0),
                    Artikel(13, "Kräuterseitling", "Stk.", 4, 0)
                )
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Artikel List") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("<") // Simple back button representation
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (artikels.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(artikels) { artikel ->
                        ArtikelListItem(artikel, onClick = { onNavigateToDetail(artikel) })
                    }
                }
            } else {
                Text(
                    text = errorMessage ?: "No articles found",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun ArtikelListItem(artikel: Artikel, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = artikel.name, style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Nr: ${artikel.nr}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Preis: ${artikel.preis / 100.0}€ / ${artikel.messeinheit}", style = MaterialTheme.typography.bodySmall)
            }
            Text(text = "Menge: ${artikel.menge}", style = MaterialTheme.typography.bodySmall)
        }
    }
}