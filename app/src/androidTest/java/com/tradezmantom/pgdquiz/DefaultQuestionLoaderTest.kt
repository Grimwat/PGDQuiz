package com.tradezmantom.pgdquiz

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tradezmantom.pgdquiz.billing.PlatformContext
import com.tradezmantom.pgdquiz.domain.DefaultQuestionLoader
import com.tradezmantom.pgdquiz.ui.data.QuizType
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DefaultQuestionLoaderTest {

    private val loader = DefaultQuestionLoader(
        PlatformContext(ApplicationProvider.getApplicationContext())
    )

    @Test
    fun loadsDrainLayingQuestionsFromBundledJson() = runTest {
        val questions = loader.loadQuestions(QuizType.DRAIN_LAYING)
        assertTrue("expected non-empty questions", questions.isNotEmpty())
        assertTrue(questions.all { it.quizType == QuizType.DRAIN_LAYING })
        assertTrue(questions.all { it.answer.isNotBlank() })
    }

    @Test
    fun loadsPlumbingQuestionsFromBundledJson() = runTest {
        val questions = loader.loadQuestions(QuizType.PLUMBING)
        assertTrue("expected non-empty questions", questions.isNotEmpty())
    }

    @Test
    fun loadsGasfittingQuestionsFromBundledJson() = runTest {
        val questions = loader.loadQuestions(QuizType.GASFITTING)
        assertTrue("expected non-empty questions", questions.isNotEmpty())
    }

    @Test
    fun defaultQuizTypeReturnsEmptyList() = runTest {
        val questions = loader.loadQuestions(QuizType.DEFAULT)
        assertEquals(emptyList<Any>(), questions)
    }
}
