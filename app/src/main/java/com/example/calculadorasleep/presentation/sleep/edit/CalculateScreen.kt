package com.example.calculadorasleep.presentation.sleep.edit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.calculadorasleep.domain.sleep.UseCases.SleepCalculationMode
import com.example.calculadorasleep.domain.sleep.UseCases.SleepTimeOption
import com.example.calculadorasleep.presentation.auth.Logout.LogoutDialog
import com.example.calculadorasleep.presentation.auth.Logout.LogoutViewModel
import com.example.calculadorasleep.ui.theme.CalculadoraSleepTheme
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CalculateScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
    viewModel: CalculateViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isForcedDark = state.mode == SleepCalculationMode.SLEEP_AT
    CalculadoraSleepTheme(darkTheme = isForcedDark) {
        LaunchedEffect(state.saved) {
            if (state.saved) {
                onBack()
                viewModel.onEvent(CalculateEvent.ClearMessage)
            }
        }
        CalculateBody(
            state = state,
            onEvent = viewModel::onEvent,
            onLogout = onLogout
        )
    }
}
@Composable
fun CalculateBody(
    state: CalculateState,
    onEvent: (CalculateEvent) -> Unit,
    onLogout: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message, state.error) {
        (state.message ?: state.error)?.let {
            snackbarHostState.showSnackbar(it)
            onEvent(CalculateEvent.ClearMessage)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }

    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                SkyIllustration(
                    mode = state.mode,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .align(Alignment.TopCenter)
                )
                Column {
                    //Spacer(Modifier.height(8.dp))
                    CalculateTopBarActions(onLogout = onLogout)

                    Spacer(Modifier.height(16.dp))
                    ModeSelector(mode = state.mode, onEvent = onEvent)
                }
            }


            Spacer(Modifier.height(16.dp))
            Text(
                text = if (state.mode == SleepCalculationMode.WAKE_UP_AT)
                    "Hora de despertar" else "Hora de dormir",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.tertiary
            )

            Spacer(Modifier.height(8.dp))
            TimePickerCard(state = state, onEvent = onEvent)

            Spacer(Modifier.height(20.dp))
            Button(
                onClick = { onEvent(CalculateEvent.Calculate) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_calculate"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    "CALCULAR",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            if (state.options.isNotEmpty()) {
                Spacer(Modifier.height(28.dp))
                Text(
                    text = if (state.mode == SleepCalculationMode.WAKE_UP_AT)
                        "Mejores horas para dormir" else "Mejores horas para despertar",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    state.options.forEach { option ->
                        SleepOptionCard(
                            option = option,
                            selected = option == state.selectedOption,
                            onClick = { onEvent(CalculateEvent.SelectOption(option)) }
                        )
                    }
                }
            }
            if (state.options.isNotEmpty()) {
                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = { onEvent(CalculateEvent.Save) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text("GUARDAR", fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}




@Composable
private fun CalculateTopBarActions(
    onLogout: () -> Unit,
    logoutViewModel: LogoutViewModel=hiltViewModel()
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {

        Text(
            text = "Deep Sleep",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        IconButton(onClick = { showLogoutDialog = true }){
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = "Cerrar sesión",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

    }
    if (showLogoutDialog) {
        LogoutDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                showLogoutDialog = false
                scope.launch {
                    logoutViewModel.logout()
                    onLogout()
                }
            }
        )
    }
}

@Composable
private fun ModeSelector(mode: SleepCalculationMode, onEvent: (CalculateEvent) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.height(IntrinsicSize.Max)
    ) {
        ModeChip(
            label = "Quiero despertarme a...",
            icon = Icons.Default.WbSunny,
            selected = mode == SleepCalculationMode.WAKE_UP_AT,
            modifier = Modifier
                .weight(1f)
                .testTag("chip_wake_up"),
            onClick = { onEvent(CalculateEvent.ModeChanged(SleepCalculationMode.WAKE_UP_AT)) }
        )
        ModeChip(
            label = "Me voy a dormir a...",
            icon = Icons.Default.Bedtime,
            selected = mode == SleepCalculationMode.SLEEP_AT,
            modifier = Modifier
                .weight(1f)
                .testTag("chip_sleep_at"),
            onClick = { onEvent(CalculateEvent.ModeChanged(SleepCalculationMode.SLEEP_AT)) }
        )
    }
}

@Composable
private fun ModeChip(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(28.dp),
        color = if (selected) MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)
        else MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun TimePickerCard(state: CalculateState, onEvent: (CalculateEvent) -> Unit) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 3.dp,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                NumberStepper(
                    value = state.hour,
                    range = 1..12,
                    testTag = "stepper_hour",
                    onChange = { onEvent(CalculateEvent.HourChanged(it)) }
                )
                Text(
                    ":",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                NumberStepper(
                    value = state.minute,
                    range = 0..59,
                    testTag = "stepper_minute",
                    onChange = { onEvent(CalculateEvent.MinuteChanged(it)) }
                )
            }
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PeriodButton("AM", state.isAm, "btn_am") { onEvent(CalculateEvent.PeriodChanged(true)) }
                PeriodButton("PM", !state.isAm, "btn_pm") { onEvent(CalculateEvent.PeriodChanged(false)) }
            }
        }
    }
}

@Composable
private fun NumberStepper(
    value: Int,
    range: IntRange,
    testTag: String,
    onChange: (Int) -> Unit
) {
    fun step(delta: Int) {
        val size = range.last - range.first + 1
        val next = range.first + ((value - range.first + delta) % size + size) % size
        onChange(next)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(64.dp)
            .testTag(testTag)
    ) {
        IconButton(onClick = { step(1) }, modifier = Modifier.testTag("${testTag}_up")) {
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Aumentar", tint = MaterialTheme.colorScheme.primary)
        }
        Text(
            text = value.toString().padStart(2, '0'),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        IconButton(onClick = { step(-1) }, modifier = Modifier.testTag("${testTag}_down")) {
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Disminuir", tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun PeriodButton(
    label: String,
    selected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant,
        border = if (!selected) BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp),
            fontWeight = FontWeight.Bold,
            color = if (selected) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SleepOptionCard(option: SleepTimeOption, selected: Boolean, onClick: () -> Unit) {
    val formatter = remember { DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault()) }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("option_card_${option.ciclos}"),
        shape = RoundedCornerShape(18.dp),
        color = when {
            selected -> if (option.esIdeal) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                       else MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
            else -> MaterialTheme.colorScheme.surface
        },
        border = when {
            selected -> if (option.esIdeal) BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                       else BorderStroke(2.dp, MaterialTheme.colorScheme.secondary)
            option.esIdeal -> BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            else -> BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
        }
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            if (option.esIdeal) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        text = "Ideal",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
            Column {
                Text("Dormir a las", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = option.time.format(formatter),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Spacer(Modifier.height(2.dp))
                Text("• ${option.ciclos} ciclos de sueño", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "${formatHoras(option.duracionHoras)} de descanso" +
                            if (option.ciclos == 4) " (Mínimo)" else " total",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun formatHoras(horas: Double): String {
    val h = horas.toInt()
    val m = ((horas - h) * 60).toInt()
    return if (m == 0) "$h horas" else "$h horas y $m minutos"
}

@Preview(showBackground = true)
@Composable
private fun CalculateBodyPreview() {
    CalculadoraSleepTheme {
        CalculateBody(
            state = CalculateState(
                targetTime = LocalTime.of(7, 30),
                options = listOf(
                    SleepTimeOption(LocalTime.of(22, 30), 6, 9.0, false),
                    SleepTimeOption(LocalTime.of(0, 0), 5, 7.5, true),
                    SleepTimeOption(LocalTime.of(1, 30), 4, 6.0, false)
                )
            ),
            onEvent = {},
            onLogout = {}
        )
    }
}