package com.example.pgdquiz.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import com.example.pgdquiz.R

class PGDViewmodel : ViewModel() {
}

@Composable
fun buttonActivate(
    modifier: Modifier = Modifier
) {
    var isSelected by remember { mutableStateOf(false) }
    val buttonPainter = if (isSelected) {
        painterResource(id = R.drawable.buttonon)
    } else {
        painterResource(id = R.drawable.buttonoff)
    }

    Image(
        painter = buttonPainter,
        contentDescription = null,
        modifier = Modifier
            .clickable {
                isSelected = !isSelected
            }
    )
}


