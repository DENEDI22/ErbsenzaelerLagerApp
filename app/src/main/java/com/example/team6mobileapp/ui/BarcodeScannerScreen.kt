package com.example.team6mobileapp.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.team6mobileapp.model.Artikel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarcodeScannerScreen(onNavigateToList: () -> Unit) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Barcode Scanner") },
                actions = {
                    IconButton(onClick = onNavigateToList) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "View Articles"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            var barcodeValue by remember { mutableStateOf("") }
            if (hasCameraPermission) {

                Box(modifier = Modifier.weight(1f)) {
                    CameraPreview(onBarcodeDetected = { value ->
                        barcodeValue = value
                    })

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = if (barcodeValue.isNotEmpty()) "Last Scan: $barcodeValue" else "Point at a barcode",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Camera permission required")
                }
            }

            val artikel = Artikel.fromBarcode(barcodeValue)
            if (artikel != null) {
                ArtikelDetailsCard(artikel = artikel)
            }
        }
    }
}

@Composable
fun ArtikelDetailsCard(artikel: Artikel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            tonalElevation = 6.dp,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth(0.95f)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Artikel Detected",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Name: ${artikel.name}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Menge: ${artikel.menge}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(top = 8.dp)
                ) {
                    Button(onClick = { ReceiveArtikel(artikel) },
                        modifier = Modifier.padding(5.dp)
                            .fillMaxWidth(0.5f)) {
                        Text(text = "Receive Artikel", color = MaterialTheme.typography.bodyLarge.color)
                    }
                    Button(onClick = { SendArtikel(artikel) },
                        modifier = Modifier.padding(5.dp)
                            .fillMaxWidth(1f)) {
                        Text(text = "Send Artikel", color = MaterialTheme.typography.bodyLarge.color)
                    }
                }
                Button(onClick = { ShowDetailedArtikelCard(artikel)},
                    Modifier.fillMaxWidth(1f)){
                    Text(text = "Show Details", color = MaterialTheme.typography.bodyLarge.color)
                }
            }
        }
    }
}

fun SendArtikel(artikel: Artikel) {
}

fun ReceiveArtikel(artikel: Artikel) {
}

fun ShowDetailedArtikelCard(artikel: Artikel) {
}