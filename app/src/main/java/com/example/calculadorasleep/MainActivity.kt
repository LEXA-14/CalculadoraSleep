package com.example.calculadorasleep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.calculadorasleep.navigation.SleepNavHost
import com.example.calculadorasleep.ui.theme.CalculadoraSleepTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraSleepTheme {
                Surface(modifier = Modifier.fillMaxSize()) {

                    SleepNavHost()


                }
            }
        }
    }
}


