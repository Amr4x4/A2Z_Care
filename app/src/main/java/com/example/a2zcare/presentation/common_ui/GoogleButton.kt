package com.example.a2zcare.presentation.common_ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a2zcare.R
import com.example.a2zcare.presentation.theme.fieldColor

@Composable
fun GoogleButton(
    text: String = "Sign Up with Google",
    loadingText: String = "Creating Account...",
) {
    var clicked by remember { mutableStateOf(false) }
    Surface (
        onClick = { clicked = !clicked },
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        color = fieldColor
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Icon(
                painter = painterResource(R.drawable.google_icon_24dp),
                contentDescription = "Google Button",
                tint = Color.Unspecified
            )
            Spacer( modifier = Modifier.width(8.dp) )
            Text(
                text = if (clicked) loadingText else text,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
            Spacer( modifier = Modifier.width(16.dp) )
            if (clicked) {
                Spacer(modifier = Modifier.width(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(16.dp)
                        .width(16.dp),
                    strokeWidth = 2.dp,
                    color = Color.Gray
                )
            }

        }

    }
    
}

@Preview(showBackground = true)
@Composable
private fun PreviewGoogleButton() {
    GoogleButton(text = "Sign Up with Google")
}