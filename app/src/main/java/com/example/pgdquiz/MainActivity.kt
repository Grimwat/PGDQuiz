package com.example.pgdquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.pgdquiz.ui.QuizType
import com.example.pgdquiz.ui.QuizViewModel
import com.example.pgdquiz.ui.theme.PgdQuizTheme
import com.example.pgdquiz.ui.ui.QuizApp

class MainActivity : ComponentActivity() {
    private val viewModel = QuizViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var selectedQuizType by remember { mutableStateOf<QuizType?>(null) }

            PgdQuizTheme(quizType = selectedQuizType ?: QuizType.DEFAULT) {
                QuizApp(
                    viewModel = viewModel,
                    onQuizTypeSelected = { selectedQuizType = it }
                )
            }
        }
    }
}