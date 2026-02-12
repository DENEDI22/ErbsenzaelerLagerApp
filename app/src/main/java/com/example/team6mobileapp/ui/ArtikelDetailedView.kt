package com.example.team6mobileapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.sharp.KeyboardArrowDown
import androidx.compose.material.icons.sharp.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.team6mobileapp.model.Artikel
import com.example.team6mobileapp.model.ArtikelUpdateRequest
import com.example.team6mobileapp.network.DbClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtikelDetailedView(onNavigateBack: () -> Unit, artikel: Artikel) {
    val db = DbClient
    val scope = rememberCoroutineScope()
    var currentArtikel by remember { mutableStateOf(artikel) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var inputAmount by remember { mutableStateOf("1") }

    fun updateMenge(delta: Int) {
        val updatedArtikel = currentArtikel.copy(menge = currentArtikel.menge + delta)
        scope.launch {
            try {
                val req = ArtikelUpdateRequest(
                    name = updatedArtikel.name,
                    messeinheit = updatedArtikel.messeinheit,
                    preis = updatedArtikel.preis,
                    menge = updatedArtikel.menge
                )
                val updated = db.updateArtikel(updatedArtikel.nr, req)
                currentArtikel = updated
                errorMessage = null
            } catch (e: Throwable) {
                errorMessage = "Fehler beim Aktualisieren: ${e.message}"
                android.util.Log.e("ArtikelDetailedView", "Update Error", e)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Artikel Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Zurück")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Name: ${currentArtikel.name}", style = MaterialTheme.typography.headlineSmall)
            Text(text = "Nummer: ${currentArtikel.nr}")
            Text(text = "Preis: ${currentArtikel.preis * 0.01f} €")
            Text(text = "Einheit: ${currentArtikel.messeinheit}")

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = {
                        val delta = inputAmount.toIntOrNull() ?: 0
                        updateMenge(-delta)
                    }, modifier = Modifier.background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.small
                    )
                ) {
                    Icon(
                        imageVector = Icons.Sharp.KeyboardArrowDown,
                        contentDescription = "Decrease amount"
                    )
                }

                Text(
                    text = "${currentArtikel.menge}",
                    modifier = Modifier.padding(horizontal = 24.dp),
                    style = MaterialTheme.typography.headlineMedium
                )

                IconButton(
                    onClick = {
                        val delta = inputAmount.toIntOrNull() ?: 0
                        updateMenge(delta)
                    }, modifier = Modifier.background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.small
                    )
                ) {
                    Icon(
                        imageVector = Icons.Sharp.KeyboardArrowUp,
                        contentDescription = "Increase amount"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = inputAmount,
                onValueChange = { inputAmount = it },
                label = { Text("Anzahl") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}