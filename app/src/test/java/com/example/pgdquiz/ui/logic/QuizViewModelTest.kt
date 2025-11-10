package com.example.pgdquiz.ui.logic

import MainDispatcherRule
import com.example.pgdquiz.fakes.FakeDataStore
import com.example.pgdquiz.fakes.FakeQuestionLoader
import com.example.pgdquiz.ui.data.QuizDifficulty
import com.example.pgdquiz.ui.data.QuizType
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class QuizViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var quizDatastore: FakeDataStore
    private lateinit var questionLoader: FakeQuestionLoader
    private lateinit var viewModel: QuizViewModel

    @Before
    fun setup() {
        quizDatastore = FakeDataStore()
        questionLoader = FakeQuestionLoader()
        viewModel = QuizViewModel(quizDatastore, questionLoader)
    }

    @Test
    fun `selectAnswer updates selectedAnswer`() {
        assertTrue(viewModel.quizUiState.value.selectedAnswer == "")
        viewModel.selectAnswer("option1")
        assertTrue(viewModel.quizUiState.value.selectedAnswer == "option1")
    }
    @Test
    fun `startQuiz sets isQuizStarted to true`() {
        assertFalse(viewModel.quizUiState.value.isQuizStarted)
        viewModel.startQuiz( QuizDifficulty.EASY, QuizType.DEFAULT)
        assertTrue(viewModel.quizUiState.value.isQuizStarted)
    }
    @Test
    fun `startQuiz sets quizDifficultyEasy`() {
        assertEquals(viewModel.quizUiState.value.quizDifficulty, QuizDifficulty.EASY)
        viewModel.startQuiz( QuizDifficulty.EASY, QuizType.DEFAULT)
        assertEquals(viewModel.quizUiState.value.quizDifficulty, QuizDifficulty.EASY)
    }
    @Test
    fun `startQuiz sets quizDifficultyMedium`() {
        assertEquals(viewModel.quizUiState.value.quizDifficulty, QuizDifficulty.EASY)
        viewModel.startQuiz( QuizDifficulty.MEDIUM, QuizType.DEFAULT)
        assertEquals(viewModel.quizUiState.value.quizDifficulty, QuizDifficulty.MEDIUM)
    }
    @Test
    fun `startQuiz sets quizDifficultyHard`() {
        assertEquals(viewModel.quizUiState.value.quizDifficulty, QuizDifficulty.EASY)
        viewModel.startQuiz( QuizDifficulty.HARD, QuizType.DEFAULT)
        assertEquals(viewModel.quizUiState.value.quizDifficulty, QuizDifficulty.HARD)
    }
    @Test
    fun `startQuiz sets quizTypeDrainlaying`() {
        assertEquals(viewModel.quizUiState.value.quizType, QuizType.DEFAULT)
        viewModel.startQuiz( QuizDifficulty.EASY, QuizType.DRAIN_LAYING)
        assertEquals(viewModel.quizUiState.value.quizType, QuizType.DRAIN_LAYING)
    }

    @Test
    fun `startQuiz sets quizTypeGassfitting`() {
        assertEquals(viewModel.quizUiState.value.quizType, QuizType.DEFAULT)
        viewModel.startQuiz( QuizDifficulty.EASY, QuizType.GASFITTING)
        assertEquals(viewModel.quizUiState.value.quizType, QuizType.GASFITTING)
    }
    @Test
    fun `startQuiz sets quizTypePlumbing`() {
        assertEquals(viewModel.quizUiState.value.quizType, QuizType.DEFAULT)
        viewModel.startQuiz( QuizDifficulty.EASY, QuizType.PLUMBING)
        assertEquals(viewModel.quizUiState.value.quizType, QuizType.PLUMBING)
    }
    @Test
    fun `updatestreak updates answerStreak`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.PLUMBING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val correctAnswer = viewModel.quizUiState.value.currentQuestion?.answer
        requireNotNull(correctAnswer) { "Test setup failed: currentQuestion was null." }
        viewModel.selectAnswer(correctAnswer)
        viewModel.updateStreak()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("answerStreak should be 1 after one correct answer", 1, viewModel.quizUiState.value.answerStreak)
    }

}

