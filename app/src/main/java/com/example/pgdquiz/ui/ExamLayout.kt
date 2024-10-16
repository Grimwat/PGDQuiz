package com.example.pgdquiz.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
    Image(
        painter = painterResource(R.drawable.backg),
        contentDescription = "TradesManTom")
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
        Questionfield(
            question = "What minimum diameter\n is required for a vent stack",
            modifier = Modifier
                .padding(top = 30.dp, start = 45.dp)
        )
        ButtonGrid(
            modifier = Modifier
                .padding(top = 60.dp)
        )
        BannerAd()
    }
}


@Preview
@Composable
fun Drainpreview (){
    PgdQuizTheme {
        DrainLayout()
    }
}