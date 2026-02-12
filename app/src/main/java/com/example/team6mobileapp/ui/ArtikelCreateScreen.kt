package com.example.team6mobileapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.team6mobileapp.model.ArtikelCreateRequest
import com.example.team6mobileapp.network.ArtikelApiService
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtikelCreateScreen(onNavigateBack: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var messeinheit by remember { mutableStateOf("Stk.") }
    var preis by remember { mutableStateOf("") }
    var menge by remember { mutableStateOf("") }
    
    val messeinheiten = listOf("Stk.", "kg", "g", "l", "ml")
    var expanded by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    val apiService = remember { ArtikelApiService.create() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Artikel erstellen") },
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
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = messeinheit,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Messeinheit") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    messeinheiten.forEach { unit ->
                        DropdownMenuItem(
                            text = { Text(unit) },
                            onClick = {
                                messeinheit = unit
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = preis,
                onValueChange = { preis = it },
                label = { Text("Preis (in Cent)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = menge,
                onValueChange = { menge = it },
                label = { Text("Menge") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    scope.launch {
                        try {
                            val request = ArtikelCreateRequest(
                                name = name,
                                messeinheit = messeinheit,
                                preis = preis.toIntOrNull() ?: 0,
                                menge = menge.toIntOrNull() ?: 0
                            )
                            apiService.createArtikel(request)
                            onNavigateBack()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Erstellen")
            }
        }
    }
}
