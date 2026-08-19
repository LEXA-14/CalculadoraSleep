package com.example.calculadorasleep.presentation.quality

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.calculadorasleep.domain.sleep.UseCases.SleepCalculationMode
import com.example.calculadorasleep.presentation.sleep.edit.SkyIllustration
import com.example.calculadorasleep.ui.theme.CalculadoraSleepTheme

@Composable
fun SleepQualityScreen(
    onNavigateBack: () -> Unit,
    viewModel: SleepQualityViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isDark = isSystemInDarkTheme()
    CalculadoraSleepTheme(darkTheme = isDark) {
        LaunchedEffect(state.isSaved) {
            if (state.isSaved) {
                onNavigateBack()
            }
        }
        SleepQualityBody(
            state = state,
            onEvent = viewModel::onEvent
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SleepQualityBody(
    state: SleepQualityUiState,
    onEvent: (SleepQualityUiEvent) -> Unit
) {
    val isDark = isSystemInDarkTheme()
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            SkyIllustration(
                mode = if (isDark) SleepCalculationMode.SLEEP_AT else SleepCalculationMode.WAKE_UP_AT,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(160.dp)
                    .statusBarsPadding()
                    .padding(top = 8.dp, end = 8.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¡Buenos días!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "¿Cómo dormiste anoche?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        RatingBar(
                            rating = state.rating,
                            onRatingChanged = { onEvent(SleepQualityUiEvent.RatingChanged(it)) }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "¿Alguna sensación en particular?",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        val tags = listOf(
                            "Me siento descansado",
                            "Me costó despertar",
                            "Tuve sueños locos",
                            "Interrumpido"
                        )
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            tags.forEach { tag ->
                                val isSelected = state.selectedTag == tag
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { onEvent(SleepQualityUiEvent.TagToggled(tag)) },
                                    label = { Text(tag) },
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                        .testTag("tag_$tag"),
                                    shape = RoundedCornerShape(14.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { onEvent(SleepQualityUiEvent.SaveQuality) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("save_quality_btn"),
                    shape = RoundedCornerShape(26.dp),
                    enabled = state.rating != null && !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Guardar",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(
                    onClick = { onEvent(SleepQualityUiEvent.Dismiss) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Llenar más tarde",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun RatingBar(
    rating: Int?,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(5) { index ->
            val starIndex = index + 1
            val isFilled = rating != null && starIndex <= rating
            Icon(
                imageVector = if (isFilled) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = null,
                tint = if (isFilled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .size(44.dp)
                    .clickable { onRatingChanged(starIndex) }
                    .testTag("star_rating_$starIndex")
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SleepQualityBodyPreview() {
    MaterialTheme {
        SleepQualityBody(
            state = SleepQualityUiState(rating = 3),
            onEvent = {}
        )
    }
}