package com.example.pgdquiz.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pgdquiz.R
import com.example.pgdquiz.ui.Composables.Banner
import com.example.pgdquiz.ui.Composables.BannerAd
import com.example.pgdquiz.ui.Composables.ButtonGrid
import com.example.pgdquiz.ui.Composables.Questionfield
import com.example.pgdquiz.ui.theme.PgdQuizTheme

@Composable
fun DrainLayout(
    modifier: Modifier = Modifier
) {
        Column(modifier = modifier) {
            Banner(
                examType = "Drainlaying",
                examEmoji = painterResource(R.drawable.happypoo),
                emojiCont = "happyPoo",
                attempts = "3",
                icon = painterResource(R.drawable.flame1),
                streak = "Streak Count",
                streakcount = "3"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Questionfield(
                question = "What minimum diameter\n is required for a vent stack",
            )
        }
    }


@Preview
@Composable
fun Drainpreview() {
    PgdQuizTheme {
        DrainLayout()
    }
}