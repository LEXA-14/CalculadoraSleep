package com.example.calculadorasleep.presentation.alarm

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.calculadorasleep.domain.sleep.model.Alarm

@Composable
fun AlarmScreen (
    viewModel: AlarmViewModel = hiltViewModel(),
    onAddAlarm: () -> Unit,

) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    RequestNotificationPermission()
    AlarmListBody(state, viewModel::onEvent, onAddAlarm)
}

@Composable
fun RequestNotificationPermission() {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ -> }

    LaunchedEffect(key1 = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmListBody(
    state: AlarmUiState,
    onEvent: (AlarmUiEvent) -> Unit,
    onAddAlarm: () -> Unit,

) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(AlarmUiEvent.ClearMessage)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Deep Sleep",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Ajustes"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddAlarm,
                modifier = Modifier.testTag("fab_add")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar alarma"
                )
            }
        },

    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center).testTag("loading")
                )
            } else if (state.alarms.isEmpty()) {
                Text(
                    text = "No hay alarmas configuradas",
                    modifier = Modifier.align(Alignment.Center).testTag("empty_message"),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val currentTime = java.time.LocalTime.now()
                    val nextAlarm = state.alarms
                        .filter { it.isEnabled }
                        .minByOrNull { alarm ->
                            val alarmTime = java.time.LocalTime.of(alarm.time.hour, alarm.time.minute)
                            if (alarmTime.isBefore(currentTime)) {
                                (alarmTime.hour * 60 + alarmTime.minute) + 1440
                            } else {
                                alarmTime.hour * 60 + alarmTime.minute
                            }
                        }

                    if (nextAlarm != null) {
                        NextAlarmCard(alarm = nextAlarm)
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = state.alarms,
                            key = { it.alarmId }
                        ) { alarm ->
                            AlarmItem(
                                alarm = alarm,
                                onToggle = { isEnabled ->
                                    onEvent(AlarmUiEvent.ToggleAlarm(alarm, isEnabled))
                                },
                                onItemClick = {
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NextAlarmCard(alarm: Alarm) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("card_next_alarm")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "PRÓXIMA ALARMA",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))

            val hour24 = alarm.time.hour
            val minute = alarm.time.minute
            val isAm = hour24 < 12
            val hour12 = when {
                hour24 == 0 -> 12
                hour24 > 12 -> hour24 - 12
                else -> hour24
            }
            val period = if (isAm) "AM" else "PM"
            val timeString = "${hour12.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')} $period"

            Text(
                text = timeString,
                style = MaterialTheme.typography.displayMedium
            )
            if (alarm.label.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = alarm.label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun AlarmItem(
    alarm: Alarm,
    onToggle: (Boolean) -> Unit,
    onItemClick: () -> Unit
) {
    ElevatedCard(
        onItemClick,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("alarm_item_${alarm.alarmId}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                val hour24 = alarm.time.hour

                val minute = alarm.time.minute
                val isAm = hour24 < 12
                val hour12 = when {
                    hour24 == 0 -> 12
                    hour24 > 12 -> hour24 - 12
                    else -> hour24
                }
                val period = if (isAm) "AM" else "PM"
                val timeString = "${hour12.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')} $period"

                Text(
                    text = timeString,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = alarm.label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Switch(
                checked = alarm.isEnabled,
                onCheckedChange = onToggle,
                modifier = Modifier.testTag("switch_${alarm.alarmId}")
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AlarmListBodyPreview() {
    MaterialTheme {
        val state = AlarmUiState(
            isLoading = false,
            alarms = listOf(
                Alarm(alarmId = 1, time = kotlinx.datetime.LocalTime(7, 30), isEnabled = true, label = "Despertar"),
                Alarm(alarmId = 2, time = kotlinx.datetime.LocalTime(8, 0), isEnabled = false, label = "Trabajo")
            )
        )
        AlarmListBody(state, {}, {}, )
    }
}