package com.example.a2zcare.presentation.screens.home

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a2zcare.presentation.theme.fieldCardColor

@Composable
fun WaterTrackingCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(160.dp)
            .padding(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = fieldCardColor,
            contentColor = fieldCardColor,
            disabledContainerColor = fieldCardColor,
            disabledContentColor = fieldCardColor,
        )
    ) {
    }
}

@Preview
@Composable
private fun PreviewRunningCard() {
    StepsCard()
}