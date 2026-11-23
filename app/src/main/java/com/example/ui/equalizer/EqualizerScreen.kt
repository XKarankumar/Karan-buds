package com.example.ui.equalizer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.viewmodel.BudsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EqualizerScreen(viewModel: BudsViewModel, onBack: () -> Unit) {
    val device by viewModel.connectedDevice.collectAsState()
    val eqPresets by viewModel.eqPresets.collectAsState()
    val currentGains by viewModel.currentEqGains.collectAsState()

    val bands = listOf("32Hz", "64Hz", "125Hz", "250Hz", "500Hz", "1kHz", "2kHz", "4kHz", "8kHz", "16kHz")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("10-Band Equalizer & Audio", fontWeight = FontWeight.Bold) },
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilterChip(
                    selected = device?.spatialAudio == true,
                    onClick = { viewModel.toggleSpatialAudio() },
                    label = { Text("3D Spatial Audio") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = device?.bassBoost == true,
                    onClick = { viewModel.toggleBassBoost() },
                    label = { Text("Mega Bass") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = device?.gamingMode == true,
                    onClick = { viewModel.toggleGamingMode() },
                    label = { Text("Gaming Mode") },
                    modifier = Modifier.weight(1f)
                )
            }

            Text("Sound Presets", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(eqPresets) { preset ->
                    Button(
                        onClick = { viewModel.applyEqPreset(preset) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (currentGains == preset.gains) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            preset.name,
                            color = if (currentGains == preset.gains) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Frequency Bands (-12dB to +12dB)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        currentGains.forEachIndexed { index, gain ->
                            val bandName = bands.getOrElse(index) { "$index" }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                            ) {
                                Text("${gain.toInt()}dB", style = MaterialTheme.typography.labelSmall)
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .width(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Slider(
                                        value = gain,
                                        onValueChange = { newGain ->
                                            val updated = currentGains.toMutableList()
                                            updated[index] = newGain
                                            viewModel.updateEqGains(updated)
                                        },
                                        valueRange = -12f..12f,
                                        modifier = Modifier
                                            .graphicsLayer(rotationZ = -90f)
                                            .width(160.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(bandName, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
