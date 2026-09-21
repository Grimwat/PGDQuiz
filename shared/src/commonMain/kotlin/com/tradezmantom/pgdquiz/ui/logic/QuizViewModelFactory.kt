package com.tradezmantom.pgdquiz.ui.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.tradezmantom.pgdquiz.domain.interfaces.QuestionLoader
import com.tradezmantom.pgdquiz.domain.interfaces.QuizDatastore
import kotlin.reflect.KClass

class QuizViewModelFactory(
    private val quizDatastore: QuizDatastore,
    private val questionLoader: QuestionLoader
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        if (modelClass == QuizViewModel::class) {
            @Suppress("UNCHECKED_CAST")
            return QuizViewModel(quizDatastore, questionLoader) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
