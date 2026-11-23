package com.example.model

data class EarbudDevice(
    val id: String,
    val name: String,
    val brand: String,
    val macAddress: String,
    val isConnected: Boolean,
    val batteryLeft: Int,
    val batteryRight: Int,
    val batteryCase: Int?,
    val isCharging: Boolean,
    val ancMode: AncMode,
    val ancIntensity: Float,
    val spatialAudio: Boolean,
    val gamingMode: Boolean,
    val bassBoost: Boolean,
    val firmwareVersion: String
)

enum class AncMode {
    ANC, TRANSPARENCY, NORMAL
}

data class AncProfile(
    val id: String,
    val name: String,
    val intensity: Float,
    val windNoiseReduction: Boolean,
    val adaptive: Boolean
)

data class EqPreset(
    val id: String,
    val name: String,
    val gains: List<Float> // 10 bands: 32Hz to 16kHz
)

data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.AMOLED,
    val accentColorIndex: Int = 0,
    val language: String = "English",
    val appLockEnabled: Boolean = false,
    val autoReconnect: Boolean = true,
    val demoMode: Boolean = true
)

enum class ThemeMode {
    AMOLED, DARK, LIGHT, AUTO
}

data class ConnectionHistoryItem(
    val id: String,
    val deviceName: String,
    val timestamp: String,
    val status: String
)

data class TouchGestureConfig(
    val singleTap: String,
    val doubleTap: String,
    val tripleTap: String,
    val longPress: String
)
