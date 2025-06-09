package com.example.pgdquiz.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pgdquiz.R
import androidx.compose.runtime.State
import com.example.pgdquiz.ui.ui.QuizState
import com.google.gson.Gson
import java.io.InputStreamReader


class QuizViewModel : ViewModel() {

    private val quizStates: MutableMap<QuizType, QuizState> = mutableMapOf(
        QuizType.DRAINLAYING to QuizState(),
        QuizType.PLUMBING to QuizState(),
        QuizType.GASFITTING to QuizState()
    )
    private var allQuestions: List<Question> = emptyList()
    private var questions: List<Question> = emptyList()
    private val _currentQuestionIndex = mutableStateOf(0)
    val currentQuestionIndex: MutableState<Int> = _currentQuestionIndex

    private val _quizType = mutableStateOf(QuizType.DEFAULT)
    val quizType: State<QuizType> = _quizType

    val currentQuestion: Question?
        get() = questions.getOrNull(_currentQuestionIndex.value)

    private val _streakCount = mutableStateOf(0)
    val streakCount: MutableState<Int> = _streakCount

    private val _lives = mutableStateOf(3)
    val lives: MutableState<Int> = _lives

    private val _selectedAnswers = mutableStateOf(mutableSetOf<String>())
    val selectedAnswers: State<Set<String>> get() = _selectedAnswers

    private val _quizComplete = mutableStateOf(false)
    val quizComplete: MutableState<Boolean> = _quizComplete

    private val gson = Gson()

    fun selectAnswer(answer: String) {
        _selectedAnswers.value = mutableSetOf(answer)
    }

    fun loadQuestionsFromRawResource(context: Context, resId: Int) {
        try {
            val inputStream = context.resources.openRawResource(resId)
            val reader = InputStreamReader(inputStream)
            val jsonString = reader.readText()

            Log.d("QuizViewModel", "Raw JSON content: $jsonString")

            val parsedResponse = gson.fromJson(jsonString, QuestionsResponse::class.java)
            reader.close()
            inputStream.close()

            val filteredQuestions = parsedResponse?.questions?.mapNotNull { question ->
                if (question == null) return@mapNotNull null

                val correctAnswers = when (val ans = question.answer) {
                    is String -> listOf(ans)
                    is List<*> -> ans.filterIsInstance<String>()
                    else -> emptyList()
                }

                if (correctAnswers.isEmpty()) {
                    Log.e("QuizViewModel", "Skipping question with no valid answers: $question")
                    return@mapNotNull null
                }

                val safeOptions = question.options.orEmpty().filter { it.isNotEmpty() }
                val combined = (safeOptions + correctAnswers).distinct().shuffled()
                val paddedOptions = when {
                    combined.size >= 4 -> combined.take(4)
                    combined.isNotEmpty() -> combined + List(4 - combined.size) { "Unknown" }
                    else -> listOf("Unknown", "Unknown", "Unknown", "Unknown")
                }
                question.copy(options = paddedOptions)
            } ?: emptyList()

            allQuestions = filteredQuestions
            questions = filteredQuestions
            _currentQuestionIndex.value = 0

            if (questions.isEmpty()) {
                Log.e("QuizViewModel", "All questions were invalid or missing answers.")
            } else {
                Log.d("QuizViewModel", "Loaded Questions: $questions")
            }

        } catch (e: Exception) {
            Log.e("QuizViewModel", "Error loading questions: ${e.message}")
            e.printStackTrace()
        }
    }

    fun reset(quizType: QuizType = QuizType.DEFAULT) {
        _quizType.value = quizType
        _currentQuestionIndex.value = 0
        _streakCount.value = 0
        _lives.value = 3
        _quizComplete.value = false
        _selectedAnswers.value = mutableSetOf()

        if (quizType != QuizType.DEFAULT) {
            allQuestions = emptyList()
            questions = emptyList()
            quizStates[quizType] = QuizState()
        }

        quizStates[quizType] = QuizState()
    }
    fun restoreLife() {
        if (_lives.value <= 0) {
            _lives.value = 1
            _quizComplete.value = false
        }
    }

        fun nextQuestion() {
            val selected = _selectedAnswers.value
            val correct = currentQuestion?.correctAnswers() ?: emptyList()

            if (selected.isNotEmpty()) {
                if (selected.toSet() == correct.toSet()) {
                    _streakCount.value++
                } else {
                    _streakCount.value = 0
                    if (_lives.value > 0) _lives.value--
                }
            }

            if (_currentQuestionIndex.value < questions.size - 1) {
                _currentQuestionIndex.value++
            } else {
                _quizComplete.value = true
            }

            _selectedAnswers.value = mutableSetOf()
        }

        fun restartQuiz(mode: QuizMode, context: Context, quizType: QuizType) {
            if (quizType == QuizType.DEFAULT) {
                Log.e("QuizViewModel", "Cannot load questions for DEFAULT quiz type")
                return
            }
            _quizType.value = quizType

            val resId = when (quizType) {
                QuizType.DRAINLAYING -> R.raw.drainsquestions
                QuizType.PLUMBING -> R.raw.plumbingquestions
                QuizType.GASFITTING -> R.raw.gasquestions
                else -> error("QuizType.DEFAULT should not be used here")
            }

            loadQuestionsFromRawResource(context, resId)

            val randomized = when (mode) {
                QuizMode.EASY -> allQuestions.shuffled().take(25)
                QuizMode.MEDIUM -> allQuestions.shuffled().take(50)
                QuizMode.HARD -> {
                    val base = allQuestions.shuffled().take(50)
                    (base + base.shuffled().take(50)).shuffled()
                }
            }

            questions = randomized
            _currentQuestionIndex.value = 0
            _lives.value = 3
            _streakCount.value = 0
            _quizComplete.value = false
        }

    fun loadQuestions(context: Context, mode: QuizMode, quizType: QuizType) {
        if (quizType == QuizType.DEFAULT) {
            Log.e("QuizViewModel", "Cannot load questions for DEFAULT quiz type")
            return
        }

        _quizType.value = quizType

        val resId = when (quizType) {
            QuizType.DRAINLAYING -> R.raw.drainsquestions
            QuizType.PLUMBING -> R.raw.plumbingquestions
            QuizType.GASFITTING -> R.raw.gasquestions
            else -> error("Invalid QuizType")
        }


        try {
            val inputStream = context.resources.openRawResource(resId)
            val reader = InputStreamReader(inputStream)
            val jsonString = reader.readText()

            val parsedResponse = gson.fromJson(jsonString, QuestionsResponse::class.java)
            reader.close()
            inputStream.close()

            if (parsedResponse.questions.isEmpty()) {
                throw IllegalArgumentException("No questions found in the JSON file for $quizType")
            }

            val fixedOptionsQuestions = parsedResponse.questions.map { question ->
                val correctAnswers = when (val ans = question.answer) {
                    is String -> listOf(ans)
                    is List<*> -> ans.filterIsInstance<String>()
                    else -> emptyList()
                }
                val safeOptions = question.options.orEmpty().filter { it.isNotEmpty() }
                val combined = (safeOptions + correctAnswers).distinct().shuffled()
                val paddedOptions = when {
                    combined.size >= 4 -> combined.take(4)
                    combined.isNotEmpty() -> combined + List(4 - combined.size) { "Unknown" }
                    else -> listOf("Unknown", "Unknown", "Unknown", "Unknown")
                }
                question.copy(options = paddedOptions)
            }

            questions = fixedOptionsQuestions.shuffled().take(
                when (mode) {
                    QuizMode.EASY -> 25
                    QuizMode.MEDIUM -> 50
                    QuizMode.HARD -> 100
                }
            )

            quizStates[quizType] = quizStates[quizType]?.copy(
                questions = questions,
                currentQuestionIndex = 0,
                streakCount = 0,
                lives = 3,
                quizComplete = false,
                selectedAnswers = mutableSetOf()
            ) ?: QuizState(questions = questions)

            _currentQuestionIndex.value = 0

            Log.d("QuizViewModel", "Loaded Questions: ${quizStates[quizType]?.questions}")

        } catch (e: Exception) {
            Log.e("QuizViewModel", "Error loading questions: ${e.message}")
            e.printStackTrace()
        }
    }
    }