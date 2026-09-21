import SwiftUI
import sharedKit

/// Hosts the shared Compose UI (the same screens Android runs) via ComposeUIViewController.
/// All screen/ViewModel/theme code lives in Kotlin now (shared/commonMain) — this is just
/// the thin platform wrapper. See MainViewController.kt for the wiring (database, billing,
/// QuizPlatformActions).
struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea(.container, edges: .all)
    }
}

#Preview {
    ContentView()
}
