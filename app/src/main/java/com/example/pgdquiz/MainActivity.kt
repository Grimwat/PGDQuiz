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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.pgdquiz.ui.DrainLayout
import com.example.pgdquiz.ui.QuizViewModel
import com.example.pgdquiz.ui.QuizType
import com.example.pgdquiz.ui.QuizMode
import com.example.pgdquiz.ui.QuizModeSelection
import com.example.pgdquiz.ui.QuizTypeSelection
import com.example.pgdquiz.ui.theme.PgdQuizTheme

class MainActivity : ComponentActivity() {
    private val viewModel = QuizViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var selectedQuizType by remember { mutableStateOf<QuizType?>(null) }
            var selectedMode by remember { mutableStateOf<QuizMode?>(null) }
            val context = LocalContext.current

            PgdQuizTheme {
                when {
                    selectedQuizType == null -> {
                        QuizTypeSelection(
                            tradeTom = painterResource(R.drawable.neonsign),
                            onSelectQuizType = { quizType ->
                                selectedQuizType = quizType }
                        )
                    }

                    selectedMode == null -> {
                        QuizModeSelection(
                            onSelectMode = { mode ->
                                selectedMode = mode
                                viewModel.loadQuestions(context, mode, selectedQuizType!!)
                            }
                        )
                    }

                    selectedMode == null -> {
                        QuizTypeSelection(
                            tradeTom = painterResource(R.drawable.neonsign),
                            onSelectQuizType = { quizType ->
                                selectedQuizType = quizType
                            }
                        )
                    }

                    else -> {
                        DrainLayout(
                            viewModel = viewModel,
                            onExit = { selectedMode = null },
                            quizMode = selectedMode!!,
                            quizType = selectedQuizType!!,
                            onBackToModeSelect = { selectedMode = null }
                        )
                    }
                }
            }
        }
    }
}