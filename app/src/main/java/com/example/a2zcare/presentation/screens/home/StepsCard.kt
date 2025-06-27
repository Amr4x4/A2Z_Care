package com.example.a2zcare.presentation.screens.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.a2zcare.R
import com.example.a2zcare.presentation.navegation.Screen
import com.example.a2zcare.presentation.theme.fieldCardColor
import com.example.a2zcare.presentation.theme.lightGreen

@Composable
fun StepsCard(
    modifier: Modifier = Modifier,
    steps: Int = 5000,
    navController: NavController
) {
    val progress = (steps.coerceIn(0, 10000)) / 10000f
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
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(bottom = 4.dp)
                    .weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Steps",
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 18.sp, lineHeight = 20.sp ),
                )
                Spacer( modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = 8.dp.toPx()
                        drawArc(
                            color = Color.White.copy(alpha = 0.2f), // background track
                            startAngle = 150f,
                            sweepAngle = 240f,
                            useCenter = false,
                            style = Stroke(stroke, cap = StrokeCap.Round),
                            size = Size(size.width, size.height),
                            topLeft = Offset.Zero
                        )
                        drawArc(
                            color = Color.Green,
                            startAngle = 150f,
                            sweepAngle = 240f * progress,
                            useCenter = false,
                            style = Stroke(stroke, cap = StrokeCap.Round),
                            size = Size(size.width, size.height),
                            topLeft = Offset.Zero
                        )
                    }
                    Column(
                        modifier = Modifier
                            .size(70.dp)
                            .padding(top = 5.dp),
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Today's steps",
                            color = Color.Cyan,
                            style = MaterialTheme.typography.headlineSmall
                                .copy(fontSize = 9.sp, lineHeight = 20.sp)
                        )
                        Text(
                            text = "$steps",
                            color = lightGreen,
                            style = MaterialTheme.typography.headlineSmall
                                .copy(fontSize = 11.sp, lineHeight = 20.sp)
                        )
                        Text(
                            text = "/10,000",
                            color = Color.Cyan,
                            style = MaterialTheme.typography.headlineSmall
                                .copy(fontSize = 9.sp, lineHeight = 20.sp)
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.shoes),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(47.dp)
                )
                IconButton(
                    onClick = {
                        navController.navigate(Screen.StepsTracking.route)
                    },
                    modifier = Modifier
                        .size(37.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowCircleRight,
                        contentDescription = "More information about step trucker",
                        tint = Color.LightGray,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

    }
}

@Preview
@Composable
private fun PreviewRunningCard() {
    StepsCard(navController = rememberNavController())
}