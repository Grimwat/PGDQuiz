package com.example.pgdquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pgdquiz.domain.DefaultQuizDatastore
import com.example.pgdquiz.domain.DefaultQuestionLoader
import com.example.pgdquiz.ui.appUi.mainScreen.QuizMainScreen
import com.example.pgdquiz.ui.data.QuizType
import com.example.pgdquiz.ui.logic.QuizViewModel
import com.example.pgdquiz.ui.logic.QuizViewModelFactory
import com.example.pgdquiz.ui.theme.PgdQuizTheme

class MainActivity : ComponentActivity() {

    private var selectedQuizType by mutableStateOf<QuizType?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val app = application as QuizApplication

            val viewModel: QuizViewModel = viewModel(
                factory = QuizViewModelFactory(
                    quizDatastore = app.quizDatastore,
                    questionLoader = DefaultQuestionLoader(context = applicationContext)
                )
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