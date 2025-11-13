package com.example.pgdquiz.ui.logic

import MainDispatcherRule
import androidx.compose.animation.core.copy
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
    @Test
    fun `QuizDifficulty sets easy questions`() = runTest {
    assertEquals(viewModel.quizUiState.value.questions.size, 0)
    viewModel.startQuiz(QuizDifficulty.EASY, QuizType.PLUMBING)
    mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
    assertEquals(viewModel.quizUiState.value.questions.size, 3)
    }
    @Test
    fun `QuizDifficulty sets medium questions`() = runTest {
        assertEquals(viewModel.quizUiState.value.questions.size, 0)
        viewModel.startQuiz(QuizDifficulty.MEDIUM, QuizType.PLUMBING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(viewModel.quizUiState.value.questions.size, 3)
    }
    @Test
    fun `QuizDifficulty sets hard questions`() = runTest {
        assertEquals(viewModel.quizUiState.value.questions.size, 0)
        viewModel.startQuiz(QuizDifficulty.HARD, QuizType.PLUMBING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(viewModel.quizUiState.value.questions.size, 3)
    }
    @Test
    fun `isLoading is false when no questions are loaded`() = runTest {
        assertEquals(false, viewModel.quizUiState.value.isLoading)
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.DEFAULT)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        assertTrue("Questions list should be empty", viewModel.quizUiState.value.questions.isEmpty())
        assertEquals("isLoading should be false after failing to load questions", false, viewModel.quizUiState.value.isLoading)
    }
    @Test
    fun `nextQuestion updates to next question`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.PLUMBING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val expectedNextQuestion = viewModel.quizUiState.value.questions[1]
        viewModel.nextQuestion()
        val newState = viewModel.quizUiState.value
        assertEquals("Question index should be 1", 1, newState.currentQuestionIndex)
        assertEquals("Current question should be the second question in the list", expectedNextQuestion, newState.currentQuestion)
        assertEquals("Selected answer should be cleared", "", newState.selectedAnswer)
        assertEquals("showCorrectAnswer should be false", false, newState.showCorrectAnswer)
    }
    @Test
    fun `nextQuestion shows congratulations screen on last question`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.PLUMBING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        viewModel.nextQuestion()
        viewModel.nextQuestion()
        assertEquals("Should be on the last question", 2, viewModel.quizUiState.value.currentQuestionIndex)
        viewModel.nextQuestion()
        val newState = viewModel.quizUiState.value
        assertTrue("showCongratulationsScreen should be true", newState.showCongratulationsScreen)
        assertEquals("Selected answer should be cleared", "", newState.selectedAnswer)
        assertEquals("showCorrectAnswer should be false", false, newState.showCorrectAnswer)
    }
    // In QuizViewModelTest.kt

    @Test
    fun `triggerShowCorrectAnswer updates streak, shows answer, then advances question`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.PLUMBING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val correctAnswer = viewModel.quizUiState.value.currentQuestion!!.answer
        viewModel.selectAnswer(correctAnswer)
        viewModel.triggerShowCorrectAnswer()
        var currentState = viewModel.quizUiState.value
        assertEquals("Answer streak should be incremented", 1, currentState.answerStreak)
        assertTrue("showCorrectAnswer should be true immediately", currentState.showCorrectAnswer)
        assertEquals("Question index should still be 0 before the delay", 0, currentState.currentQuestionIndex)
        mainDispatcherRule.testDispatcher.scheduler.advanceTimeBy(2001L)
        currentState = viewModel.quizUiState.value
        assertEquals("Question index should be 1 after the delay", 1, currentState.currentQuestionIndex)
    }
    @Test
    fun `checkLivesAndStreak resets lives and streak on a new day`() = runTest {
        quizDatastore.setIsSameDay(false)
        quizDatastore.storeCurrentLives(0, QuizType.PLUMBING)
        quizDatastore.storeAnswerStreak(10)
        viewModel.checkLivesAndStreak()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val currentState = viewModel.quizUiState.value
        assertEquals("Plumbing lives should be reset to starting value", 5, currentState.plumbingQuizState.lives)
        assertEquals("GasFitting lives should be reset to starting value", 5, currentState.gasFittingQuizState.lives)
        assertEquals("DrainLaying lives should be reset to starting value", 5, currentState.drainLayingQuizState.lives)
        assertEquals("Answer streak should be reset to starting value", 0, currentState.answerStreak)
        assertEquals("Datastore lives should be reset", 5, quizDatastore.fetchCurrentLives(QuizType.PLUMBING))
        assertEquals("Datastore streak should be reset", 0, quizDatastore.fetchAnswerStreak())
    }








}

