package com.example.pgdquiz.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pgdquiz.R
import com.example.pgdquiz.ui.theme.PgdQuizTheme
import com.example.pgdquiz.ui.ui.Banner
import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.pgdquiz.ui.Composables.BannerAd
import com.example.pgdquiz.ui.ui.ButtonGrid
import com.example.pgdquiz.ui.ui.QuestionField

@Composable
fun DrainLayout(
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = viewModel()

) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Banner(
            examType = "Drainlaying",
            examEmoji = painterResource(R.drawable.happypoo),
            emojiCont = "happyPoo",
            attempts = "3",
            icon = painterResource(R.drawable.flame1),
            streak = "Streak Count",
            streakcount = "3",
            modifier = Modifier.fillMaxWidth()

        )
        Spacer(modifier = Modifier.padding(4.dp))
        QuestionField(
            question = viewModel.currentQuestion,
            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
        )
        ButtonGrid(viewModel = viewModel)
//        BannerAd()
    }
}


@Preview
@Composable
fun Drainpreview() {
    PgdQuizTheme {
        DrainLayout()
    }
}
