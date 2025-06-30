package com.example.a2zcare.presentation.screens.vip_payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.a2zcare.R
import com.example.a2zcare.presentation.theme.gold

@Composable
fun VIPCard(
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(10.dp)
            .clickable{ navController },
        colors = CardDefaults.cardColors(
            containerColor = gold,
            contentColor = gold,
            disabledContainerColor = gold,
            disabledContentColor = gold,
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.pro_icon),
                contentDescription = "VIP Card Icon",
                modifier = Modifier
                    .padding(10.dp)
                    .background(
                        shape = CircleShape,
                        color = Color.White
                    ),
                alignment = Alignment.Center,
                contentScale = ContentScale.Fit
            )
            Spacer( modifier = Modifier.width(6.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Upgrade Plan Now!",
                    color = Color.Red,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Enjoy all the VIP features.",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodySmall
                )

            }

        }

    }
}

@Preview
@Composable
private fun PreviewVIPCard() {
    VIPCard(navController = rememberNavController())
}