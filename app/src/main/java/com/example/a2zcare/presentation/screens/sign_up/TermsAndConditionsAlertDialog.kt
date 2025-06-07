package com.example.a2zcare.presentation.screens.sign_up

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
@Composable
fun TermsAndConditionsAlertDialog(
    isOpen: Boolean,
    title: String,
    bodyText: String,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit
) {
    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = title) },
            text = {
                LazyColumn {
                    item {
                        Text(text = bodyText)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismissRequest() }) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onConfirmButtonClick()
                }) {
                    Text(text = "Continue")
                }
            }
        )
    }
}
