package com.example.ui.gestures

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.TouchGestureConfig
import com.example.viewmodel.BudsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GesturesScreen(viewModel: BudsViewModel, onBack: () -> Unit) {
    val gestures by viewModel.touchGestures.collectAsState()

    var singleTap by remember { mutableStateOf(gestures.singleTap) }
    var doubleTap by remember { mutableStateOf(gestures.doubleTap) }
    var tripleTap by remember { mutableStateOf(gestures.tripleTap) }
    var longPress by remember { mutableStateOf(gestures.longPress) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Touch Gestures & Controls", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Customize earbud touch & press actions", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))

            GestureItem("Single Tap", singleTap) { singleTap = it }
            GestureItem("Double Tap", doubleTap) { doubleTap = it }
            GestureItem("Triple Tap", tripleTap) { tripleTap = it }
            GestureItem("Long Press", longPress) { longPress = it }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.updateGestures(TouchGestureConfig(singleTap, doubleTap, tripleTap, longPress))
                    onBack()
                },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Save Gesture Settings", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun GestureItem(title: String, value: String, onChanged: (String) -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Play / Pause", "Next Track", "Previous Track", "Volume Control", "Switch ANC", "Voice Assistant").forEach { action ->
                    FilterChip(
                        selected = value == action,
                        onClick = { onChanged(action) },
                        label = { Text(action) }
                    )
                }
            }
        }
    }
}
