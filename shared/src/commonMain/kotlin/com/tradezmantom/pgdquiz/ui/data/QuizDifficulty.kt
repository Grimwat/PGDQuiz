package com.tradezmantom.pgdquiz.ui.data

/** [questionCount] is the target run length; a smaller question bank caps it. */
enum class QuizDifficulty(val questionCount: Int) {
    EASY(25), MEDIUM(50), HARD(100)
}
