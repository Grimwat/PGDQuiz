package com.example.pgdquiz.ui.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import com.example.pgdquiz.R

@Composable
fun NextButton (modifier: Modifier = Modifier) {
    Box(modifier = modifier,
            contentAlignment = Alignment.Center) {
        Button(
            onClick = {},
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Unspecified
            ),
            modifier = Modifier
                .background(Color.Transparent)
        )
        {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Color.Transparent)
            ) {
                Image(
                    painter = painterResource(R.drawable.nextbutton),
                    contentDescription = "Next Question",
                    modifier = modifier
                )
                Text(
                    text = "Flush"
                )
            }
        }
    }
}