package com.example.pgdquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pgdquiz.ui.QuizViewModel
import com.example.pgdquiz.ui.ui.QuizApp

class MainActivity : ComponentActivity() {
    private val viewModel = QuizViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            QuizApp(viewModel = viewModel)
        }
    }
}