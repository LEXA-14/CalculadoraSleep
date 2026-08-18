package com.example.calculadorasleep.presentation.auth.Logout

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@Composable
fun LogoutButton(
    onLogout: () -> Unit,
    viewModel: LogoutViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    IconButton(
        onClick = { showDialog = true },
        modifier = Modifier.testTag("logout_button")
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Logout,
            contentDescription = "Cerrar sesión"
        )
    }
    if (showDialog) {
        LogoutDialog(
            onDismiss = { showDialog = false },
            onConfirm = {
                showDialog = false
                scope.launch {
                    viewModel.logout()
                    onLogout()
                }
            }
        )
    }
}

@Composable
fun LogoutDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("logout_dialog"),
        title = { Text(text = "Cerrar sesión") },
        text = { Text(text = "¿Estás seguro de que deseas cerrar sesión?") },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                modifier = Modifier.testTag("logout_confirm")
            ) {
                Text("Cerrar sesión")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("logout_cancel")
            ) {
                Text("Cancelar")
            }
        }
    )
}