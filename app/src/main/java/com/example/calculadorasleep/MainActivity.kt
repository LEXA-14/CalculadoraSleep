package com.example.calculadorasleep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.calculadorasleep.navigation.SleepNavDisplay
import com.example.calculadorasleep.presentation.darkMode.ThemeViewModel
import com.example.calculadorasleep.ui.theme.CalculadoraSleepTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val currentUser = FirebaseAuth.getInstance().currentUser
        val isUserLoggedIn = currentUser != null

        setContent {
            val themeViewModel: ThemeViewModel= hiltViewModel()
            val isDarkMode by themeViewModel.isDarkMode.collectAsStateWithLifecycle()
            CalculadoraSleepTheme (darkTheme = isDarkMode){
                Surface(modifier = Modifier.fillMaxSize()) {
                    SleepNavDisplay(isLoggedIn = isUserLoggedIn)
                }
            }
        }
    }
}
