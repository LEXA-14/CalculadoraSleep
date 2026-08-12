package com.example.calculadorasleep.presentation.sleep.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.calculadorasleep.domain.sleep.UseCases.SleepStats
import com.example.calculadorasleep.domain.sleep.model.Sleep
import com.example.calculadorasleep.ui.theme.CalculadoraSleepTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    HistoryBody(state = state, onEvent = viewModel::onEvent)
}

@Composable
fun HistoryBody(
    state: HistoryUiState,
    onEvent: (HistoryUiEvent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message, state.error) {
        (state.message ?: state.error)?.let {
            snackbarHostState.showSnackbar(it)
            onEvent(HistoryUiEvent.ClearMessage)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Historial de sueño",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))
            StatsCard(stats = state.stats)

            Spacer(Modifier.height(20.dp))

            when {
                state.isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.testTag("loading"))
                    }
                }

                state.registros.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Aún no hay registros de sueño",
                            modifier = Modifier.testTag("empty_message"),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items = state.registros, key = { it.sleepId }) { sleep ->
                            SleepHistoryItem(
                                sleep = sleep,
                                onDelete = { onEvent(HistoryUiEvent.Delete(sleep)) }
                            )
                        }
                        item { Spacer(Modifier.height(16.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsCard(stats: SleepStats) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatItem("Noches", stats.noches.toString())
            StatItem("Promedio", "${stats.duracionPromedioMin} min")
            StatItem("Ciclos", "%.1f".format(stats.ciclosPromedio))
            StatItem(
                "Calidad",
                stats.calidadPromedio?.let { "%.1f★".format(it) } ?: "—"
            )
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun SleepHistoryItem(
    sleep: Sleep,
    onDelete: () -> Unit
) {
    val timeFormatter = remember { DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault()) }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd MMM", Locale.getDefault()) }
    val zone = remember { ZoneId.systemDefault() }

    val dormir = remember(sleep.dormirTiempo) {
        Instant.ofEpochMilli(sleep.dormirTiempo).atZone(zone)
    }
    val despertar = remember(sleep.despertarTiempo) {
        Instant.ofEpochMilli(sleep.despertarTiempo).atZone(zone)
    }
    val duracionMin = (sleep.despertarTiempo - sleep.dormirTiempo) / 60000

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("history_item_${sleep.sleepId}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.NightsStay,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary
            )
            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = dateFormatter.format(dormir),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${timeFormatter.format(dormir)} → ${timeFormatter.format(despertar)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${duracionMin / 60}h ${duracionMin % 60}min · ${sleep.ciclos} ciclos" +
                            (sleep.calidadSleep?.let { " · $it★" } ?: ""),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.testTag("btn_delete_${sleep.sleepId}")
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar registro")
            }
        }
    }
}
@Preview
@Composable
private fun HistoryBodyPreview() {
    CalculadoraSleepTheme {
        HistoryBody(
            state = HistoryUiState(
                registros = listOf(
                    Sleep(sleepId = 1, dormirTiempo = System.currentTimeMillis() - 28_800_000, despertarTiempo = System.currentTimeMillis(), ciclos = 5, calidadSleep = 4)
                ),
                stats = SleepStats(noches = 5, duracionPromedioMin = 420, ciclosPromedio = 4.6, calidadPromedio = 3.8)
            ),
            onEvent = {}
        )
    }
}