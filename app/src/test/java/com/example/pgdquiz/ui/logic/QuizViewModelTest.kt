package com.example.pgdquiz.ui.logic

import com.example.pgdquiz.fakes.FakeDataStore
import com.example.pgdquiz.fakes.FakeQuestionLoader
import com.example.pgdquiz.ui.data.QuizDifficulty
import com.example.pgdquiz.ui.data.QuizType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class QuizViewModelTest {
    val quizDatastore = FakeDataStore()
    val questionLoader = FakeQuestionLoader()
    val viewModel = QuizViewModel(quizDatastore, questionLoader)

    @Test
    fun `selectAnswer updates selectedAnswer`() {
        assertTrue(viewModel.quizUiState.value.selectedAnswer == "")
        viewModel.selectAnswer("option1")
        assertTrue(viewModel.quizUiState.value.selectedAnswer == "option1")
    }
    @Test
    fun `startQuiz sets isQuizStarted to true`() {
        assertFalse(viewModel.quizUiState.value.isQuizStarted)
        viewModel.startQuiz( QuizDifficulty.EASY, QuizType.DEFAULT,)
        assertTrue(viewModel.quizUiState.value.isQuizStarted)
    }
    @Test
    fun `startQuiz sets quizDifficultyEasy`() {
        assertEquals(viewModel.quizUiState.value.quizDifficulty, QuizDifficulty.EASY)
        viewModel.startQuiz( QuizDifficulty.EASY, QuizType.DEFAULT,)
        assertEquals(viewModel.quizUiState.value.quizDifficulty, QuizDifficulty.EASY)
    }
    @Test
    fun `startQuiz sets quizDifficultyMedium`() {
        assertEquals(viewModel.quizUiState.value.quizDifficulty, QuizDifficulty.EASY)
        viewModel.startQuiz( QuizDifficulty.MEDIUM, QuizType.DEFAULT,)
        assertEquals(viewModel.quizUiState.value.quizDifficulty, QuizDifficulty.MEDIUM)
    }
    @Test
    fun `startQuiz sets quizDifficultyHard`() {
        assertEquals(viewModel.quizUiState.value.quizDifficulty, QuizDifficulty.EASY)
        viewModel.startQuiz( QuizDifficulty.HARD, QuizType.DEFAULT,)
        assertEquals(viewModel.quizUiState.value.quizDifficulty, QuizDifficulty.HARD)
    }
    @Test
    fun `startQuiz sets quizTypeDrainlaying`() {
        assertEquals(viewModel.quizUiState.value.quizType, QuizType.DEFAULT)
        viewModel.startQuiz( QuizDifficulty.EASY, QuizType.DRAIN_LAYING,)
        assertEquals(viewModel.quizUiState.value.quizType, QuizType.DRAIN_LAYING)
    }

    @Test
    fun `startQuiz sets quizTypeGassfitting`() {
        assertEquals(viewModel.quizUiState.value.quizType, QuizType.DEFAULT)
        viewModel.startQuiz( QuizDifficulty.EASY, QuizType.GASFITTING,)
        assertEquals(viewModel.quizUiState.value.quizType, QuizType.GASFITTING)
    }

}
