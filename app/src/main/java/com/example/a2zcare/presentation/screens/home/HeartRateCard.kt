package com.example.a2zcare.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a2zcare.presentation.theme.backgroundColor
import com.example.a2zcare.presentation.theme.fieldColor

@Composable
fun HeartRateCard(

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .padding(12.dp) ,
        colors = CardDefaults.cardColors(
            containerColor = fieldColor,
            contentColor = fieldColor,
            disabledContainerColor = fieldColor,
            disabledContentColor = fieldColor,
        )
    ) {
        Column(

        ) {
            Row(

            ) {

            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHeartRateCard() {
    HeartRateCard()
}