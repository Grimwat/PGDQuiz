package com.tradezmantom.pgdquiz.ui.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tradezmantom.pgdquiz.domain.interfaces.QuestionLoader
import com.tradezmantom.pgdquiz.domain.interfaces.QuizDatastore


class QuizViewModelFactory(
    private val quizDatastore: QuizDatastore,
    private val questionLoader: QuestionLoader
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizViewModel(quizDatastore, questionLoader) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}