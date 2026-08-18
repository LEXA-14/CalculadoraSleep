package com.example.calculadorasleep.presentation.quality

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.calculadorasleep.ui.theme.CalculadoraSleepTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SleepQualityActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraSleepTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SleepQualityScreen(
                        onNavigateBack = { finish() }
                    )
                }
            }
        }
    }
}
