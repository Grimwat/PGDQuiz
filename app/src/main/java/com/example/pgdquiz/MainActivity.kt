package com.example.pgdquiz


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pgdquiz.domain.DefaultQuestionLoader
import com.example.pgdquiz.ui.appUi.addManager.adds.UnSkipAddManager
import com.example.pgdquiz.ui.appUi.mainScreen.QuizMainScreen
import com.example.pgdquiz.ui.data.QuizType
import com.example.pgdquiz.ui.logic.QuizViewModel
import com.example.pgdquiz.ui.logic.QuizViewModelFactory
import com.example.pgdquiz.ui.theme.PgdQuizTheme
import com.google.android.gms.ads.MobileAds

class MainActivity : ComponentActivity() {

    private var selectedQuizType by mutableStateOf<QuizType?>(null)

    private var adManager: UnSkipAddManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        adManager = UnSkipAddManager(this)

        try {
            MobileAds.initialize(this) { _ ->
                window.decorView.postDelayed({
                    if (!isFinishing) {
                        Log.d("ADS", "Initial loading of ads started...")
                        adManager?.loadAllAds()
                    }
                }, 1500)
            }
        } catch (e: Exception) {
            Log.e("ADS_ERROR", "AdMob failed: ${e.message}")
        }

        setContent {
            val app = application as? QuizApplication

            if (app != null) {
                val viewModel: QuizViewModel = viewModel(
                    factory = QuizViewModelFactory(
                        quizDatastore = app.quizDatastore,
                        questionLoader = DefaultQuestionLoader(this@MainActivity)
                    )
                )

                PgdQuizTheme(quizType = selectedQuizType ?: QuizType.DEFAULT) {
                    QuizMainScreen(
                        viewModel = viewModel,
                        onQuizTypeSelected = { selectedQuizType = it }
                    )
                }
            } else {

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: QuizApplication not recognized. Check Manifest.")
                }
                Log.e("FATAL", "QuizApplication cast failed. Is it in AndroidManifest?")
            }
        }
    }

    fun showInterstitialAd(onAdDismissed: () -> Unit) {
        val manager = adManager
        if (manager != null) {
            manager.showInterstitial(this, onAdDismissed)
        } else {
            onAdDismissed()
        }
    }

    fun showRewardedAd(onRewardEarned: () -> Unit) {
        val manager = adManager
        if (manager != null) {
            manager.showRewarded(this, onRewardEarned)
        } else {
            onRewardEarned()
        }
    }
}