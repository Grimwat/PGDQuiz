package com.example.pgdquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.pgdquiz.network.QuestionLoader
import com.example.pgdquiz.network.QuizDatastore
import com.example.pgdquiz.ui.appUi.mainScreen.QuizMainScreen
import com.example.pgdquiz.ui.data.QuizType
import com.example.pgdquiz.ui.logic.QuizViewModel
import com.example.pgdquiz.ui.theme.PgdQuizTheme

class MainActivity : ComponentActivity() {

    private var selectedQuizType by mutableStateOf<QuizType?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            val viewModel = QuizViewModel(
                quizDatastore = QuizDatastore(context = applicationContext),
                questionLoader = QuestionLoader(context = applicationContext)
            )

            PgdQuizTheme(quizType = selectedQuizType ?: QuizType.DEFAULT) {
                QuizMainScreen(
                    viewModel = viewModel,
                    onQuizTypeSelected = { selectedQuizType = it }
                )
            }
        }
    }
}