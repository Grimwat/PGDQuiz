import Foundation
import sharedKit

/// Bridges the shared Kotlin `QuizViewModel` (and its `StateFlow`) to SwiftUI's
/// `ObservableObject`/`@Published`. Swift can't collect a Kotlin `Flow` directly, so this
/// uses `QuizViewModel.watchUiState(onEach:)` — a callback-based alternative defined in
/// shared/commonMain specifically for this purpose.
@MainActor
final class QuizViewModelObservable: ObservableObject {
    @Published private(set) var uiState: QuizUiState

    private let viewModel: QuizViewModel
    private var watcher: Closeable?

    init() {
        let platformContext = PlatformContext()
        let database = DatabaseBuilderKt.createAppDatabase(platformContext: platformContext)
        let datastore = DefaultQuizDatastore(userStatsDao: database.userStatsDao())
        let questionLoader = DefaultQuestionLoader(platformContext: platformContext)

        let viewModel = QuizViewModel(quizDatastore: datastore, questionLoader: questionLoader)
        self.viewModel = viewModel
        self.uiState = viewModel.quizUiState.value

        self.watcher = viewModel.watchUiState { [weak self] state in
            DispatchQueue.main.async {
                self?.uiState = state
            }
        }
    }

    deinit {
        watcher?.close()
    }

    func startQuiz(difficulty: QuizDifficulty, quizType: QuizType) {
        viewModel.startQuiz(difficulty: difficulty, quizType: quizType)
    }

    func selectAnswer(_ answer: String) {
        viewModel.selectAnswer(answer: answer)
    }

    func triggerShowCorrectAnswer() {
        viewModel.triggerShowCorrectAnswer()
    }

    func nextQuestion() {
        viewModel.nextQuestion()
    }
}
