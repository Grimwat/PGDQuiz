package com.example.pgdquiz.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
    Image(
        painter = painterResource(R.drawable.background),
        contentDescription = "TradesManTom")
    Column {
        Banner(
            examType = "DrainLayer",
            examEmoji = painterResource(R.drawable.happypoo),
            emojiCont = "HappyPoos",
            attempts = "3",
            modifier = Modifier.fillMaxWidth()
        )
        Questionfield(
            question = "What pipe is used for most Home Sewer Drains?",
            icon = painterResource(R.drawable.flame1),
            streakcount = "0",
            streak = ""
        )
        ButtonGrid()
        BannerAd(
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Preview
@Composable
fun Preview() {
    PgdQuizTheme {
        DrainLayout()
    }
}