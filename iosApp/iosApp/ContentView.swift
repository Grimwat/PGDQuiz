import SwiftUI
import sharedKit

/// Minimal starting screen wired to the shared quiz domain/ViewModel — not a port of the
/// Android Compose UI. Swap this out for real screens once the wiring below is confirmed
/// working (see the exact generated Swift API via Xcode's "Generate Interface" on sharedKit).
struct ContentView: View {
    @StateObject private var model = QuizViewModelObservable()

    var body: some View {
        VStack(spacing: 20) {
            if model.uiState.isLoading {
                ProgressView()
            } else if let question = model.uiState.currentQuestion {
                quizQuestion(question)
            } else {
                startScreen
            }
        }
        .padding()
    }

    private var startScreen: some View {
        VStack(spacing: 16) {
            Text("PGD Quiz")
                .font(.largeTitle.bold())
            Button("Start Plumbing Quiz") {
                model.startQuiz(difficulty: .easy, quizType: .plumbing)
            }
            .buttonStyle(.borderedProminent)
        }
    }

    private func quizQuestion(_ question: Question) -> some View {
        VStack(spacing: 16) {
            Text(question.question)
                .font(.title2)
                .multilineTextAlignment(.center)

            ForEach(model.uiState.optionsAndAnswer, id: \.self) { option in
                Button(option) {
                    model.selectAnswer(option)
                    model.triggerShowCorrectAnswer()
                }
                .buttonStyle(.bordered)
                .disabled(model.uiState.showCorrectAnswer)
            }

            if model.uiState.showCorrectAnswer {
                Text(model.uiState.selectedAnswer == question.answer ? "Correct!" : "Answer: \(question.answer)")
                    .font(.headline)
            }
        }
    }
}

#Preview {
    ContentView()
}
