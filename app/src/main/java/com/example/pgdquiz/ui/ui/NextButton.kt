package com.example.pgdquiz.ui.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
        {Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(
                    color = MaterialTheme.colorScheme.outline

                )
                .border(
                    width = 4.dp,
                    color = MaterialTheme.colorScheme.primary

                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Next Question", color = Color.White)
        }
        }
    }
}