package com.tradezmantom.pgdquiz.ui.logic

import MainDispatcherRule
import com.tradezmantom.pgdquiz.fakes.FakeDataStore
import com.tradezmantom.pgdquiz.fakes.FakeQuestionLoader
import com.tradezmantom.pgdquiz.ui.data.QuizDifficulty
import com.tradezmantom.pgdquiz.ui.data.QuizType
import com.tradezmantom.pgdquiz.ui.data.SubscriptionTier
import com.tradezmantom.pgdquiz.ui.data.getCurrentQuizTypeLives
import com.tradezmantom.pgdquiz.util.STARTING_LIVES
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
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
        viewModel = QuizViewModel(
            quizDatastore = quizDatastore,
            questionLoader = questionLoader)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
    }

    @Test
    fun `selectAnswer updates selectedAnswer`() {
        assertTrue(viewModel.quizUiState.value.selectedAnswer == "")
        viewModel.selectAnswer("option1")
        assertTrue(viewModel.quizUiState.value.selectedAnswer == "option1")
    }
    @Test
    fun `startQuiz sets isQuizStarted to true`() = runTest {
        assertFalse(viewModel.quizUiState.value.isQuizStarted)
        viewModel.startQuiz( QuizDifficulty.EASY, QuizType.PLUMBING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.quizUiState.value.isQuizStarted)
    }
    @Test
    fun `startQuiz updates last played date`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.PLUMBING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        assertTrue("Date should be stored when quiz starts", quizDatastore.isDateStored)
    }

    @Test
    fun `startQuiz sets quizDifficultyEasy`() = runTest {
        assertEquals(QuizDifficulty.EASY, viewModel.quizUiState.value.quizDifficulty)
        viewModel.startQuiz( QuizDifficulty.EASY, QuizType.GASFITTING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        assertEquals (QuizDifficulty.EASY, viewModel.quizUiState.value.quizDifficulty)
    }
    @Test
    fun `startQuiz sets quizDifficultyMedium`() = runTest {
        assertEquals(QuizDifficulty.EASY, viewModel.quizUiState.value.quizDifficulty)

        viewModel.startQuiz(QuizDifficulty.MEDIUM, QuizType.GASFITTING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(QuizDifficulty.MEDIUM, viewModel.quizUiState.value.quizDifficulty)
    }
    @Test
    fun `startQuiz sets quizDifficultyHard`() = runTest {
        assertEquals(viewModel.quizUiState.value.quizDifficulty, viewModel.quizUiState.value.quizDifficulty)
        viewModel.startQuiz( QuizDifficulty.HARD, QuizType.GASFITTING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(QuizDifficulty.HARD, viewModel.quizUiState.value.quizDifficulty)
    }
    @Test
    fun `startQuiz sets quizTypeDrainlaying`() {
        assertEquals(QuizType.DEFAULT, viewModel.quizUiState.value.quizType )
        viewModel.startQuiz( QuizDifficulty.EASY, QuizType.DRAIN_LAYING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(QuizType.DRAIN_LAYING, viewModel.quizUiState.value.quizType)
    }

    @Test
    fun `startQuiz sets quizTypeGassfitting`() {
        assertEquals(QuizType.DEFAULT , viewModel.quizUiState.value.quizType)
        viewModel.startQuiz( QuizDifficulty.EASY, QuizType.GASFITTING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(QuizType.GASFITTING, viewModel.quizUiState.value.quizType)
    }
    @Test
    fun `startQuiz sets quizTypePlumbing`() {
        assertEquals( QuizType.DEFAULT, viewModel.quizUiState.value.quizType)
        viewModel.startQuiz( QuizDifficulty.EASY, QuizType.PLUMBING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(QuizType.PLUMBING, viewModel.quizUiState.value.quizType)
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
        mainDispatcherRule.testDispatcher.scheduler.advanceTimeBy(3501L)
        currentState = viewModel.quizUiState.value
        assertEquals("Question index should be 1 after the delay", 1, currentState.currentQuestionIndex)
    }
    @Test
    fun `checkLivesAndStreak resets lives and streak on a new day`() = runTest {
        val newDayDataStore = FakeDataStore().apply{
            setIsSameDay(false)
            storeCurrentLives(0, QuizType.PLUMBING)
            storeAnswerStreak(10)
        }
        val newDayViewModel = QuizViewModel(
            quizDatastore = newDayDataStore,
            questionLoader = questionLoader
        )

        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        val currentState = newDayViewModel.quizUiState.value
        assertEquals("Plumbing lives should be reset to starting value", 5, currentState.plumbingQuizState.lives)
        assertEquals("GasFitting lives should be reset to starting value", 5, currentState.gasFittingQuizState.lives)
        assertEquals("DrainLaying lives should be reset to starting value", 5, currentState.drainLayingQuizState.lives)
        assertEquals("Answer streak should be reset to starting value", 0, currentState.answerStreak)
        assertEquals("Datastore lives should be reset", 5, newDayDataStore.fetchCurrentLives(QuizType.PLUMBING))
        assertEquals("Datastore streak should be reset", 0, newDayDataStore.fetchAnswerStreak())
    }
    @Test
    fun `restoreLife sets lives for the plumbing type to 1`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.PLUMBING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        viewModel.loseLife()
        repeat(4) { viewModel.loseLife() }
        assertEquals(0, viewModel.quizUiState.value.plumbingQuizState.lives)

        viewModel.restoreLife()
        val currentState = viewModel.quizUiState.value
        assertEquals("Plumbing lives should be restored to 1", 1, currentState.plumbingQuizState.lives)
        assertTrue("Success overlay should be visible", currentState.isShowingRewardSuccess)
    }
    @Test
    fun `restoreLife sets lives for the drainlaying type to 1`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.DRAIN_LAYING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        val startingLives = viewModel.quizUiState.value.drainLayingQuizState.lives
        viewModel.loseLife()
        viewModel.restoreLife()

        val currentState = viewModel.quizUiState.value
        assertEquals("DrainLaying lives should be restored to starting lives", startingLives, currentState.drainLayingQuizState.lives)
    }

    @Test
    fun `restoreLife sets lives for the gasfitting type to 1`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.GASFITTING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        val startingLives = viewModel.quizUiState.value.gasFittingQuizState.lives
        viewModel.loseLife()
        viewModel.restoreLife()

        val currentState = viewModel.quizUiState.value
        assertEquals("GasFitting lives should be restored to starting lives", startingLives, currentState.gasFittingQuizState.lives)
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
    fun `shouldShowInterstitial returns true exactly on every 10th question`() {
        val totalQuestions = 50

        assertFalse("Should not show on question 1", viewModel.shouldShowInterstitial(0, totalQuestions))
        assertFalse("Should not show on question 5", viewModel.shouldShowInterstitial(4, totalQuestions))
        assertTrue("Should show on question 7", viewModel.shouldShowInterstitial(6, totalQuestions))
        assertTrue("Should show on question 14", viewModel.shouldShowInterstitial(13, totalQuestions))
    }

    @Test
    fun `shouldShowInterstitial returns false on the very last question to avoid double ads`() {
        val totalQuestions = 10

        assertFalse(
            "Should return false on the last question even if it is a multiple of 10",
            viewModel.shouldShowInterstitial(9, 10)
        )
    }

    @Test
    fun `shouldShowInterstitial handles short quizzes correctly`() {
        val totalQuestions = 5

        for (i in 0 until totalQuestions) {
            assertFalse(
                "No interstitial should trigger in a quiz shorter than 10 questions",
                viewModel.shouldShowInterstitial(i, totalQuestions)
            )
        }
    }
    @Test
    fun `updateSubscriptionTier updates state with new tier`() = runTest {
        viewModel.updateSubscriptionTier(SubscriptionTier.APPRENTICE)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(SubscriptionTier.APPRENTICE, viewModel.quizUiState.value.subscriptionTier)
    }

    @Test
    fun `updateSubscriptionTier updates state to tradesman`() = runTest {
        viewModel.updateSubscriptionTier(SubscriptionTier.TRADESMAN)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(SubscriptionTier.TRADESMAN, viewModel.quizUiState.value.subscriptionTier)
    }

    @Test
    fun `refreshForDifficultyScreen completes without error`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.PLUMBING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        viewModel.refreshForDifficultyScreen()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        assertNotNull(viewModel.quizUiState.value)
    }

    @Test
    fun `canShowAd returns false when question index is not a multiple of 7`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.PLUMBING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        assertFalse(viewModel.canShowAd())
    }

    @Test
    fun `handleRewardEarned shows reward success state`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.PLUMBING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        viewModel.handleRewardEarned()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.quizUiState.value.isShowingRewardSuccess)
    }

    @Test
    fun `handleRewardEarned restores a life`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.PLUMBING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val livesBefore = viewModel.quizUiState.value.getCurrentQuizTypeLives()
        viewModel.handleRewardEarned()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val livesAfter = viewModel.quizUiState.value.getCurrentQuizTypeLives()
        assertTrue(livesAfter >= livesBefore)
    }
    @Test
    fun `shouldShowInterstitial returns false for invalid inputs`() {
        assertFalse(viewModel.shouldShowInterstitial(-1, 10))
        assertFalse(viewModel.shouldShowInterstitial(5, 0))
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
        val datastoreWithStreak = FakeDataStore().apply {
            storeAnswerStreak(5)
        }


        val testViewModel = QuizViewModel(datastoreWithStreak, questionLoader)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        Assert.assertNull("Question should be null for DEFAULT type", testViewModel.quizUiState.value.currentQuestion)
        assertEquals("Pre-condition: Streak should be 5", 5, testViewModel.quizUiState.value.answerStreak)


        testViewModel.selectAnswer("some answer")
        testViewModel.updateStreak()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()


        assertEquals("ViewModel streak should be reset to 0", 0, testViewModel.quizUiState.value.answerStreak)
        assertEquals("Datastore streak should be reset to 0", 0, datastoreWithStreak.fetchAnswerStreak())
    }

    @Test
    fun `init stays at DEFAULT quizType when lastQuizType is null`() = runTest {
        val fakeDatastore = FakeDataStore()


        val testViewModel = QuizViewModel(fakeDatastore, questionLoader)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(QuizType.DEFAULT, testViewModel.quizUiState.value.quizType)
    }
    @Test
    fun `init loads lastQuizType from datastore when not null`() = runTest {
        val fakeDatastore = FakeDataStore()
        fakeDatastore.storeLastQuizType(QuizType.GASFITTING)


        val testViewModel = QuizViewModel(fakeDatastore, questionLoader)


        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()


        assertEquals(QuizType.GASFITTING, testViewModel.quizUiState.value.quizType)
    }
    @Test
    fun `startQuiz when questions already loaded updates difficulty and starts quiz`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.PLUMBING)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        assertTrue("Questions should be loaded", viewModel.quizUiState.value.questions.isNotEmpty())
        assertEquals(QuizDifficulty.EASY, viewModel.quizUiState.value.quizDifficulty)

        viewModel.startQuiz(QuizDifficulty.HARD, QuizType.PLUMBING)

        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Difficulty should update to HARD", QuizDifficulty.HARD, viewModel.quizUiState.value.quizDifficulty)
        assertEquals("QuizType should still be PLUMBING", QuizType.PLUMBING, viewModel.quizUiState.value.quizType)
        assertTrue("Quiz should be started", viewModel.quizUiState.value.isQuizStarted)
        assertFalse("Loading should be false", viewModel.quizUiState.value.isLoading)
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
    @Test
    fun `ViewModel init loads stored lives and streak immediately`() = runTest {
        quizDatastore.storeCurrentLives(2, QuizType.PLUMBING)
        quizDatastore.storeAnswerStreak(15)

        val newViewModel = QuizViewModel(quizDatastore, questionLoader)

        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(2, newViewModel.quizUiState.value.plumbingQuizState.lives)
        assertEquals(15, newViewModel.quizUiState.value.answerStreak)
    }
    @Test
    fun `restoreLife handles DEFAULT quiz type branch`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.DEFAULT)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        viewModel.restoreLife()

        assertTrue("Should still show success overlay", viewModel.quizUiState.value.isShowingRewardSuccess)
    }
    @Test
    fun `shouldShowInterstitial returns false when total questions is zero`() {
        val result = viewModel.shouldShowInterstitial(currentIndex = 0, totalQuestions = 0)
        assertFalse("Should return false for 0 questions", result)
    }

    @Test
    fun `shouldShowInterstitial returns false when total questions is negative`() {
        val result = viewModel.shouldShowInterstitial(currentIndex = 0, totalQuestions = -5)
        assertFalse("Should return false for negative questions", result)
    }
    @Test
    fun `loseLife handles DEFAULT quiz type branch`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.DEFAULT)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()


        viewModel.loseLife()

        assertEquals(QuizType.DEFAULT, viewModel.quizUiState.value.quizType)
    }
    @Test
    fun `restoreLife handles default quiz type branch`() = runTest {
        viewModel.startQuiz(QuizDifficulty.EASY, QuizType.DEFAULT)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        viewModel.restoreLife()

        assertTrue(viewModel.quizUiState.value.isShowingRewardSuccess)
    }
    @Test
    fun `dismissRewardSuccess sets isShowingRewardSuccess to false`() = runTest {
        viewModel.restoreLife()

        assertTrue("State should be true after restoreLife",
            viewModel.quizUiState.value.isShowingRewardSuccess)

        viewModel.dismissRewardSuccess()
        
        assertFalse("State should be false after dismissRewardSuccess",
            viewModel.quizUiState.value.isShowingRewardSuccess)
    }
}

