package com.example.pgdquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.pgdquiz.ui.DrainLayout
import com.example.pgdquiz.ui.QuizViewModel
import com.example.pgdquiz.ui.theme.PgdQuizTheme
import com.example.pgdquiz.ui.ui.QuizMode
import com.example.pgdquiz.ui.ui.QuizModeSelection

class MainActivity : ComponentActivity() {
    private val viewModel = QuizViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var selectedMode by remember { mutableStateOf<QuizMode?>(null) }
            val context = LocalContext.current

            PgdQuizTheme {
                if (selectedMode == null) {
                    QuizModeSelection(
                        onSelectMode = { mode ->
                            selectedMode = mode
                            viewModel.loadQuestions(context, mode)
                        }
                    )
                } else {
                    DrainLayout(
                        viewModel = viewModel,
                        quizMode = selectedMode!!,
                        onExit = {
                            selectedMode = null
                        },
                        onBackToModeSelect = {
                            selectedMode = null
                        }
                    )
                }
            }
        }
    }
}