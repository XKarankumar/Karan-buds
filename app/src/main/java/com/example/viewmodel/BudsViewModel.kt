package com.example.viewmodel

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class BudsViewModel(application: Application) : AndroidViewModel(application) {

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager = application.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        bluetoothManager?.adapter
    }

    private val _connectedDevice = MutableStateFlow<EarbudDevice?>(
        EarbudDevice(
            id = "karan_buds_pro_1",
            name = "Karan Buds Pro 3",
            brand = "Karan",
            macAddress = "98:52:4F:7A:8C:21",
            isConnected = true,
            batteryLeft = 92,
            batteryRight = 88,
            batteryCase = 95,
            isCharging = true,
            ancMode = AncMode.ANC,
            ancIntensity = 0.85f,
            spatialAudio = true,
            gamingMode = false,
            bassBoost = true,
            firmwareVersion = "v3.4.12-Pro"
        )
    )
    val connectedDevice: StateFlow<EarbudDevice?> = _connectedDevice.asStateFlow()

    private val _savedDevices = MutableStateFlow<List<EarbudDevice>>(
        listOf(
            EarbudDevice("1", "Karan Buds Pro 3", "Karan", "98:52:4F:7A:8C:21", true, 92, 88, 95, true, AncMode.ANC, 0.85f, true, false, true, "v3.4.12"),
            EarbudDevice("2", "Apple AirPods Pro 2", "Apple", "AC:87:A3:12:4F:90", false, 80, 80, 85, false, AncMode.ANC, 0.8f, true, false, false, "v6.1.0"),
            EarbudDevice("3", "Samsung Galaxy Buds3 Pro", "Samsung", "88:34:12:7F:6E:5A", false, 75, 78, 90, false, AncMode.ANC, 0.9f, true, false, true, "v2.0.4"),
            EarbudDevice("4", "OnePlus Buds Pro 3", "OnePlus", "54:65:76:87:98:A0", false, 90, 89, 92, false, AncMode.TRANSPARENCY, 0.5f, false, true, true, "v1.5.2"),
            EarbudDevice("5", "Nothing Ear (3)", "Nothing", "CC:DD:EE:FF:11:22", false, 88, 86, 78, false, AncMode.ANC, 0.8f, true, false, false, "v1.1.0"),
            EarbudDevice("6", "Boat Airdopes 141", "Boat", "11:22:33:44:55:66", false, 65, 60, 50, false, AncMode.NORMAL, 0.0f, false, false, false, "v1.0.0")
        )
    )
    val savedDevices: StateFlow<List<EarbudDevice>> = _savedDevices.asStateFlow()

    private val _scannedDevices = MutableStateFlow<List<EarbudDevice>>(emptyList())
    val scannedDevices: StateFlow<List<EarbudDevice>> = _scannedDevices.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val _showPopup = MutableStateFlow(false)
    val showPopup: StateFlow<Boolean> = _showPopup.asStateFlow()

    private val _popupDevice = MutableStateFlow<EarbudDevice?>(null)
    val popupDevice: StateFlow<EarbudDevice?> = _popupDevice.asStateFlow()

    private val _ancProfiles = MutableStateFlow<List<AncProfile>>(
        listOf(
            AncProfile("1", "Maximum Noise Isolation", 1.0f, true, true),
            AncProfile("2", "Commute & Flight", 0.8f, true, false),
            AncProfile("3", "Office & Café Focus", 0.6f, false, true),
            AncProfile("4", "Outdoor Wind Shield", 0.7f, true, false)
        )
    )
    val ancProfiles: StateFlow<List<AncProfile>> = _ancProfiles.asStateFlow()

    private val _eqPresets = MutableStateFlow<List<EqPreset>>(
        listOf(
            EqPreset("flat", "Flat / Studio", listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)),
            EqPreset("bass", "Karan Mega Bass", listOf(6f, 5f, 4f, 2f, 0f, -1f, 0f, 2f, 4f, 5f)),
            EqPreset("vocal", "Crystal Vocal", listOf(-2f, -1f, 1f, 3f, 5f, 4f, 2f, 0f, -1f, -2f)),
            EqPreset("electronic", "EDM & Club", listOf(4f, 3f, 1f, 0f, -2f, 2f, 3f, 4f, 5f, 4f)),
            EqPreset("rock", "Rock Legend", listOf(3f, 2f, 1f, -1f, -2f, 0f, 2f, 3f, 4f, 3f)),
            EqPreset("classical", "Concert Hall", listOf(4f, 3f, 2f, 1f, 0f, 0f, 1f, 2f, 3f, 3f))
        )
    )
    val eqPresets: StateFlow<List<EqPreset>> = _eqPresets.asStateFlow()

    private val _currentEqGains = MutableStateFlow<List<Float>>(listOf(6f, 5f, 4f, 2f, 0f, -1f, 0f, 2f, 4f, 5f))
    val currentEqGains: StateFlow<List<Float>> = _currentEqGains.asStateFlow()

    private val _touchGestures = MutableStateFlow(
        TouchGestureConfig(
            singleTap = "Play / Pause",
            doubleTap = "Next Track",
            tripleTap = "Previous Track",
            longPress = "Switch ANC / Transparency"
        )
    )
    val touchGestures: StateFlow<TouchGestureConfig> = _touchGestures.asStateFlow()

    private val _appSettings = MutableStateFlow(AppSettings())
    val appSettings: StateFlow<AppSettings> = _appSettings.asStateFlow()

    private val _connectionHistory = MutableStateFlow<List<ConnectionHistoryItem>>(
        listOf(
            ConnectionHistoryItem("1", "Karan Buds Pro 3", getFormattedTime(System.currentTimeMillis() - 3600000), "Connected"),
            ConnectionHistoryItem("2", "Apple AirPods Pro 2", getFormattedTime(System.currentTimeMillis() - 86400000), "Disconnected"),
            ConnectionHistoryItem("3", "Samsung Galaxy Buds3 Pro", getFormattedTime(System.currentTimeMillis() - 172800000), "Connected")
        )
    )
    val connectionHistory: StateFlow<List<ConnectionHistoryItem>> = _connectionHistory.asStateFlow()

    private val _isFinderActive = MutableStateFlow(false)
    val isFinderActive: StateFlow<Boolean> = _isFinderActive.asStateFlow()

    fun triggerPopup(device: EarbudDevice) {
        _popupDevice.value = device
        _showPopup.value = true
        triggerHaptic()
    }

    fun dismissPopup() {
        _showPopup.value = false
    }

    fun connectDevice(device: EarbudDevice) {
        viewModelScope.launch {
            _scannedDevices.value = _scannedDevices.value.map {
                if (it.id == device.id) it.copy(isConnected = true) else it.copy(isConnected = false)
            }
            _savedDevices.value = _savedDevices.value.map {
                if (it.id == device.id) it.copy(isConnected = true) else it.copy(isConnected = false)
            }
            _connectedDevice.value = device.copy(isConnected = true)
            triggerPopup(device)
            addHistory(device.name, "Connected")
        }
    }

    fun disconnectDevice() {
        viewModelScope.launch {
            _connectedDevice.value?.let { current ->
                addHistory(current.name, "Disconnected")
            }
            _connectedDevice.value = null
            triggerHaptic()
        }
    }

    fun scanDevices() {
        viewModelScope.launch {
            _isScanning.value = true
            _scannedDevices.value = emptyList()
            delay(2000) // simulating Bluetooth LE scan

            // Populate mock / real detected devices
            val discovered = listOf(
                EarbudDevice("s1", "Apple AirPods Pro 2", "Apple", "AA:BB:CC:11:22:33", false, 95, 95, 90, false, AncMode.ANC, 0.8f, true, false, false, "v6.1"),
                EarbudDevice("s2", "Samsung Galaxy Buds3 Pro", "Samsung", "DD:EE:FF:44:55:66", false, 88, 85, 92, false, AncMode.ANC, 0.9f, true, false, true, "v2.0"),
                EarbudDevice("s3", "Sony WF-1000XM5", "Sony", "12:34:56:78:90:AB", false, 91, 90, 85, false, AncMode.ANC, 0.95f, true, false, true, "v4.2.1"),
                EarbudDevice("s4", "OnePlus Buds Pro 3", "OnePlus", "98:76:54:32:10:FE", false, 82, 84, 88, false, AncMode.TRANSPARENCY, 0.4f, false, true, true, "v1.8"),
                EarbudDevice("s5", "Nothing Ear (3)", "Nothing", "AB:CD:EF:01:23:45", false, 86, 86, 80, false, AncMode.ANC, 0.8f, true, false, false, "v1.2"),
                EarbudDevice("s6", "JBL Tour Pro 3", "JBL", "55:66:77:88:99:AA", false, 79, 81, 75, false, AncMode.ANC, 0.75f, true, false, true, "v3.1")
            )
            _scannedDevices.value = discovered
            _isScanning.value = false
            triggerHaptic()
        }
    }

    fun setAncMode(mode: AncMode) {
        viewModelScope.launch {
            _connectedDevice.value = _connectedDevice.value?.copy(ancMode = mode)
            triggerHaptic()
        }
    }

    fun setAncIntensity(intensity: Float) {
        viewModelScope.launch {
            _connectedDevice.value = _connectedDevice.value?.copy(ancIntensity = intensity)
        }
    }

    fun toggleSpatialAudio() {
        viewModelScope.launch {
            _connectedDevice.value = _connectedDevice.value?.let { it.copy(spatialAudio = !it.spatialAudio) }
            triggerHaptic()
        }
    }

    fun toggleGamingMode() {
        viewModelScope.launch {
            _connectedDevice.value = _connectedDevice.value?.let { it.copy(gamingMode = !it.gamingMode) }
            triggerHaptic()
        }
    }

    fun toggleBassBoost() {
        viewModelScope.launch {
            _connectedDevice.value = _connectedDevice.value?.let { it.copy(bassBoost = !it.bassBoost) }
            triggerHaptic()
        }
    }

    fun updateEqGains(gains: List<Float>) {
        _currentEqGains.value = gains
    }

    fun applyEqPreset(preset: EqPreset) {
        _currentEqGains.value = preset.gains
        triggerHaptic()
    }

    fun updateGestures(config: TouchGestureConfig) {
        _touchGestures.value = config
        triggerHaptic()
    }

    fun updateSettings(settings: AppSettings) {
        _appSettings.value = settings
        triggerHaptic()
    }

    fun startFinder() {
        _isFinderActive.value = true
        triggerHaptic()
        // Play vibration pulse
        viewModelScope.launch {
            delay(5000)
            _isFinderActive.value = false
        }
    }

    fun stopFinder() {
        _isFinderActive.value = false
    }

    private fun addHistory(deviceName: String, status: String) {
        val newItem = ConnectionHistoryItem(
            id = UUID.randomUUID().toString(),
            deviceName = deviceName,
            timestamp = getFormattedTime(System.currentTimeMillis()),
            status = status
        )
        _connectionHistory.value = listOf(newItem) + _connectionHistory.value
    }

    private fun getFormattedTime(timeMillis: Long): String {
        val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
        return sdf.format(Date(timeMillis))
    }

    private fun triggerHaptic() {
        try {
            val vibrator = getApplication<Application>().getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            vibrator?.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
        } catch (e: Exception) {
            // Ignore if vibration not permitted
        }
    }
}
