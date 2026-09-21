package com.tradezmantom.pgdquiz.ui.appUi.examLayoutScreen.portrait


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tradezmantom.pgdquiz.ui.appUi.examLayoutScreen.AnswerButton
import com.tradezmantom.pgdquiz.ui.data.QuizType
import com.tradezmantom.pgdquiz.ui.logic.QuizViewModel

@Composable
fun ButtonsPortrait(
    modifier: Modifier = Modifier,
    quizType: QuizType,
    viewModel: QuizViewModel = viewModel()
) {
    val state by viewModel.quizUiState.collectAsStateWithLifecycle()
    val currentQuestion = state.currentQuestion ?: return

    val lazyListState = rememberLazyListState()
    val showScrollIndicator by remember {
        derivedStateOf {
            lazyListState.canScrollForward || lazyListState.canScrollBackward
        }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .padding(end = if (showScrollIndicator) 12.dp else 0.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                items = state.optionsAndAnswer,
                key = { option -> option }
            ) { option ->
                val isSelected = state.selectedAnswer == option
                val isCorrect = currentQuestion.isOptionCorrect(option)

                AnswerButton(
                    optionText = option,
                    isSelected = isSelected,
                    onButtonSelected = { viewModel.selectAnswer(option) },
                    modifier = Modifier.fillMaxWidth(),
                    isCorrect = isCorrect,
                    showCorrectAnswer = state.showCorrectAnswer,
                    shape = RectangleShape
                )
            }
        }

        if (showScrollIndicator) {
            val scrollProgress by remember {
                derivedStateOf {
                    val layoutInfo = lazyListState.layoutInfo
                    val totalItems = layoutInfo.totalItemsCount
                    if (totalItems == 0) return@derivedStateOf 0f
                    val visibleItems = layoutInfo.visibleItemsInfo
                    if (visibleItems.isEmpty()) return@derivedStateOf 0f
                    val firstVisible = visibleItems.first().index.toFloat()
                    val lastVisible = visibleItems.last().index.toFloat()
                    val visibleRange = lastVisible - firstVisible
                    firstVisible / (totalItems - visibleRange).coerceAtLeast(1f)
                }
            }

            val trackHeight = 200.dp
            val thumbHeight = 40.dp

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(6.dp)
                    .height(trackHeight)
                    .clip(RoundedCornerShape(3.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            ) {
                Box(
                    modifier = Modifier
                        .width(6.dp)
                        .height(thumbHeight)
                        .offset(y = ((trackHeight - thumbHeight) * scrollProgress))
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.outline,
                                    MaterialTheme.colorScheme.primary
                                )
                            )
                        )
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(40.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            if (showScrollIndicator) MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
                            else Color.Transparent
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = showScrollIndicator,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = "▼ scroll",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}