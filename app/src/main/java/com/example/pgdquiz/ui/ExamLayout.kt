package com.example.pgdquiz.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.pgdquiz.R
import com.example.pgdquiz.ui.Composables.Banner
import com.example.pgdquiz.ui.Composables.BannerAd
import com.example.pgdquiz.ui.Composables.ButtonGrid
import com.example.pgdquiz.ui.Composables.Questionfield

@Composable
fun DrainLayout() {
    Banner(
        examType ="",
        examEmoji = painterResource(R.drawable.happypoo),
        emojiCont ="",
        attempts ="")
    Questionfield(
        question ="",
        icon = painterResource(R.drawable.flame1),
        streakcount ="",
        questionbackground = painterResource(R.drawable.buttonoff),
        streak ="")
    ButtonGrid()
    BannerAd()

}