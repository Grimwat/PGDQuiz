package com.example.pgdquiz.ui.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.pgdquiz.ui.DrainLayout
import com.example.pgdquiz.ui.QuizViewModel
import com.example.pgdquiz.ui.theme.PgdQuizTheme


@Composable
fun LivesLost (
    onWatchAd: () -> Unit,
    onExit: () -> Unit
){
    AlertDialog(
        onDismissRequest = { },
        title = {
            Text(
                text = "Game Over",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text("Your 3 lives have been used up.")
        },
        confirmButton = {
            Button(onClick = onWatchAd) {
                Text("Watch Ad to Continue")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onExit) {
                Text("Exit Quiz")
            }
        }
    )
}






@Preview
@Composable
fun LivesPreview() {
    val viewModel = QuizViewModel()
    PgdQuizTheme {
        LivesLost() { }

    }
}
