package com.example.a2zcare.presentation.common_ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a2zcare.presentation.theme.selected
import com.example.a2zcare.presentation.theme.unselected

@Composable
fun ConfirmationBottomBar(
    buttonText: String,
    enabled: Boolean,
    onConfirmationClick: () -> Unit,
    content: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, bottom = 36.dp)
    ) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onConfirmationClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = selected,
                contentColor = Color.White,
                disabledContainerColor = unselected,
                disabledContentColor = Color.Gray
            )
        ) {
            if (content != null) {
                content()
            } else {
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewConfirmationBottomBar() {
    ConfirmationBottomBar(
        buttonText = "Confirm",
        enabled = false,
        onConfirmationClick = { }
    )
}
