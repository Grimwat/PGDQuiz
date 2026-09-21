package com.tradezmantom.pgdquiz.ui.logic

import kotlinx.coroutines.Job

// Swift can't collect a Kotlin Flow directly, so SwiftUI code observes QuizViewModel
// via watchUiState(onEach:) instead and calls close() on the returned handle to stop.
class Closeable(private val job: Job) {
    fun close() {
        job.cancel()
    }
}
