package com.example.pgdquiz.ui.logic

import MainDispatcherRule
import androidx.compose.animation.core.copy
import com.example.pgdquiz.fakes.FakeDataStore
import com.example.pgdquiz.fakes.FakeQuestionLoader
import com.example.pgdquiz.ui.data.QuizDifficulty
import com.example.pgdquiz.ui.data.QuizType
import com.example.pgdquiz.util.STARTING_LIVES
import kotlinx.coroutines.test.runTest
import org.junit.Assert
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
    @Test
    fun `restoreLife sets lives for the plumbing type to 5`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.PLUMBING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("Lives should be 5 initially", 5, viewModel.quizUiState.value.plumbingQuizState.lives)
        viewModel.loseLife()
        assertEquals("Lives should be 4 after losing one", 4, viewModel.quizUiState.value.plumbingQuizState.lives)
        viewModel.restoreLife()
        val currentState = viewModel.quizUiState.value
        assertEquals("Plumbing lives should be restored to 5", 5, currentState.plumbingQuizState.lives)
        assertEquals("GasFitting lives should not be changed", STARTING_LIVES, currentState.gasFittingQuizState.lives)
        assertEquals("DrainLaying lives should not be changed", STARTING_LIVES, currentState.drainLayingQuizState.lives)
    }
    @Test
    fun `restoreLife sets lives for the drainlaying type to 5`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.DRAIN_LAYING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("Lives should be 5 initially", 5, viewModel.quizUiState.value.drainLayingQuizState.lives)
        viewModel.loseLife()
        assertEquals("Lives should be 4 after losing one", 4, viewModel.quizUiState.value.drainLayingQuizState.lives)
        viewModel.restoreLife()
        val currentState = viewModel.quizUiState.value
        assertEquals("DrainLaying lives should be restored to 5", 5, currentState.drainLayingQuizState.lives)
        assertEquals("GasFitting lives should not be changed", STARTING_LIVES, currentState.gasFittingQuizState.lives)
        assertEquals("Plumbing lives should not be changed", STARTING_LIVES, currentState.plumbingQuizState.lives)
    }
    @Test
    fun `restoreLife sets lives for the gasfitting type to 5`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.GASFITTING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(
            "Lives should be 5 initially",
            5,
            viewModel.quizUiState.value.gasFittingQuizState.lives
        )
        viewModel.loseLife()
        assertEquals(
            "Lives should be 4 after losing one",
            4,
            viewModel.quizUiState.value.gasFittingQuizState.lives
        )
        viewModel.restoreLife()
        val currentState = viewModel.quizUiState.value
        assertEquals(
            "GasFitting lives should be restored to 5",
            5,
            currentState.gasFittingQuizState.lives
        )
        assertEquals(
            "Plumbing lives should not be changed",
            STARTING_LIVES,
            currentState.plumbingQuizState.lives
        )
        assertEquals(
            "DrainLaying lives should not be changed",
            STARTING_LIVES,
            currentState.drainLayingQuizState.lives
        )
    }
    @Test
    fun `updateStreak increments streak and stores it on correct answer`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.PLUMBING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val initialLives = viewModel.quizUiState.value.plumbingQuizState.lives
        val initialStreak = viewModel.quizUiState.value.answerStreak
        val correctAnswer = viewModel.quizUiState.value.currentQuestion!!.answer
        viewModel.selectAnswer(correctAnswer)
        viewModel.updateStreak()
        val currentState = viewModel.quizUiState.value
        val expectedStreak = initialStreak + 1
        assertEquals("Answer streak should be incremented", expectedStreak, currentState.answerStreak)
        assertEquals("Lives should not change on correct answer", initialLives, currentState.plumbingQuizState.lives)
        assertEquals("Datastore should be updated with the new streak", expectedStreak, quizDatastore.fetchAnswerStreak())
    }
    @Test
    fun `updateStreak resets streak and loses a life on incorrect answer`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.PLUMBING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        viewModel.selectAnswer(viewModel.quizUiState.value.currentQuestion!!.answer)
        viewModel.updateStreak()
        assertEquals("Streak should be 1 initially for this test", 1, viewModel.quizUiState.value.answerStreak)
        val initialLives = viewModel.quizUiState.value.plumbingQuizState.lives
        val correctAnswer = viewModel.quizUiState.value.currentQuestion!!.answer
        val incorrectAnswer = "Some wrong answer"
        Assert.assertNotEquals(correctAnswer, incorrectAnswer)
        viewModel.selectAnswer(incorrectAnswer)
        viewModel.updateStreak()
        val currentState = viewModel.quizUiState.value
        assertEquals("Answer streak should be reset to 0", 0, currentState.answerStreak)
        assertEquals("A life should be lost on incorrect answer", initialLives - 1, currentState.plumbingQuizState.lives)
        assertEquals("Datastore should be updated with the reset streak", 0, quizDatastore.fetchAnswerStreak())
    }
    @Test
    fun `loseLife does nothing when lives are already zero`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.PLUMBING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val startingLives = viewModel.quizUiState.value.plumbingQuizState.lives
        assertEquals("Should start with starting lives", STARTING_LIVES, startingLives)
        for (i in 1..startingLives) {
            viewModel.loseLife()
        }
        assertEquals("Lives should be 0 after losing them all", 0, viewModel.quizUiState.value.plumbingQuizState.lives)
        viewModel.loseLife()
        val finalLives = viewModel.quizUiState.value.plumbingQuizState.lives
        assertEquals("Lives should remain 0 and not go negative", 0, finalLives)
    }
    @Test
    fun `updateStreak handles null currentQuestion by treating answer as incorrect`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.DEFAULT)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        Assert.assertNull("To test this path, currentQuestion must be null", viewModel.quizUiState.value.currentQuestion)
        quizDatastore.storeAnswerStreak(5)
        viewModel.checkLivesAndStreak()
        viewModel.selectAnswer("some answer")
        assertEquals("Pre-condition: Streak should be 5", 5, viewModel.quizUiState.value.answerStreak)
        viewModel.updateStreak()
        assertEquals("Streak should be reset to 0 when question is null", 0, viewModel.quizUiState.value.answerStreak)
        assertEquals("Datastore streak should also be reset to 0", 0, quizDatastore.fetchAnswerStreak())
    }

    @Test
    fun `setCurrentLives does nothing when quizType is DEFAULT`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.DEFAULT)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val initialPlumbingLives = viewModel.quizUiState.value.plumbingQuizState.lives
        val initialGasFittingLives = viewModel.quizUiState.value.gasFittingQuizState.lives
        val initialDrainLayingLives = viewModel.quizUiState.value.drainLayingQuizState.lives
        assertEquals("Pre-condition: Quiz type should be DEFAULT", QuizType.DEFAULT, viewModel.quizUiState.value.quizType)
        viewModel.loseLife()
        val currentState = viewModel.quizUiState.value
        assertEquals("Plumbing lives should not change", initialPlumbingLives, currentState.plumbingQuizState.lives)
        assertEquals("GasFitting lives should not change", initialGasFittingLives, currentState.gasFittingQuizState.lives)
        assertEquals("DrainLaying lives should not change", initialDrainLayingLives, currentState.drainLayingQuizState.lives)
    }
    @Test
    fun `startQuiz reloads questions when quiz type is different`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.PLUMBING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val stateAfterFirstQuiz = viewModel.quizUiState.value
        assertEquals("Pre-condition: Quiz type should be PLUMBING", QuizType.PLUMBING, stateAfterFirstQuiz.quizType)
        assertFalse("Pre-condition: Questions list should NOT be empty", stateAfterFirstQuiz.questions.isEmpty())
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.GASFITTING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val stateAfterSecondQuiz = viewModel.quizUiState.value
        assertEquals("Quiz type should now be GASFITTING", QuizType.GASFITTING, stateAfterSecondQuiz.quizType)
        assertEquals("The questions should have been reloaded for GASFITTING", 3, stateAfterSecondQuiz.questions.size)
    }
    @Test
    fun `startQuiz loads questions when question list is initially empty`() = runTest {
        assertTrue(
            "Pre-condition: The list of questions should be empty",
            viewModel.quizUiState.value.questions.isEmpty()
        )
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.PLUMBING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        assertFalse(
            "Questions should now be loaded",
            viewModel.quizUiState.value.questions.isEmpty()
        )
        assertEquals(
            "There should be 3 plumbing questions",
            3,
            viewModel.quizUiState.value.questions.size
        )
    }
    @Test
    fun `startQuiz reloads questions when switching to a different quiz type`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.PLUMBING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val stateAfterFirstQuiz = viewModel.quizUiState.value
        assertFalse(
            "Pre-condition: Questions list must NOT be empty for this test",
            stateAfterFirstQuiz.questions.isEmpty()
        )
        assertEquals(
            "Pre-condition: Quiz type should be PLUMBING",
            QuizType.PLUMBING,
            stateAfterFirstQuiz.quizType
        )

        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.GASFITTING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        val stateAfterSecondQuiz = viewModel.quizUiState.value
        assertEquals(
            "Quiz type should have switched to GASFITTING",
            QuizType.GASFITTING,
            stateAfterSecondQuiz.quizType
        )
        assertEquals(
            "Questions should have been reloaded for GASFITTING",
            3,
            stateAfterSecondQuiz.questions.size
        )
        assertEquals(
            "The current question should be a GASFITTING question",
            QuizType.GASFITTING,
            stateAfterSecondQuiz.currentQuestion?.quizType
        )
    }




















}

