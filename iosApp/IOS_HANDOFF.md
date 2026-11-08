# iOS handoff — PgdQuiz (for Claude Code on the Mac)

Read this whole file before doing anything. Everything below was done on Windows without Xcode,
so **nothing iOS has ever been linked, run, or seen on screen** — only Kotlin-compiled.

## What the app is
NZ plumbing/gas/drainlaying exam quiz app. Kotlin Multiplatform: almost all code (UI in Compose
Multiplatform, ViewModel, Room DB, question loading, billing) lives in `shared/`. Android app is
live on Play (applicationId `com.tradezmantom.pgdquiz`, version 3.3). iOS should run the **same
shared Compose UI** — not a rewrite in SwiftUI.

## Current state of iOS
- `shared/build.gradle.kts` declares `iosArm64` + `iosSimulatorArm64`, framework baseName `sharedKit` (static by default).
- Entry point: `shared/src/iosMain/kotlin/com/tradezmantom/pgdquiz/MainViewController.kt`
  — builds DB/datastore/question loader/BillingManager and returns a `ComposeUIViewController`.
- Swift side is only two files: `iosApp/iosApp/PgdQuizApp.swift` (@main) and `ContentView.swift`
  (`UIViewControllerRepresentable` calling `MainViewControllerKt.MainViewController()`).
- **There is no `.xcodeproj` yet.** That's job #1.
- iOS actuals: `shared/src/iosMain/...` — `BillingManager.ios.kt` (StoreKit 1), `DatabaseBuilder.ios.kt`,
  `DefaultQuestionLoader.ios.kt`, `PlatformBannerAd.ios.kt` (no-op), `Theme.ios.kt`.
- Verified only via `./gradlew :shared:compileKotlinIosSimulatorArm64` and `:shared:compileKotlinIosArm64`.

## Tasks, in order

### 1. Create the Xcode project
- Create `iosApp/iosApp.xcodeproj` (SwiftUI App, iOS 15+ is fine) using the existing Swift files in `iosApp/iosApp/`.
- Bundle ID: suggest `com.tradezmantom.pgdquiz` (confirm with me). Display name "PGD Quiz" (confirm).
- Link the KMP framework the standard way: Build Phase "Run Script" **before** Compile Sources:
  ```
  cd "$SRCROOT/.."
  ./gradlew :shared:embedAndSignAppleFrameworkForXcode
  ```
  plus `FRAMEWORK_SEARCH_PATHS = $(SRCROOT)/../shared/build/xcode-frameworks/$(CONFIGURATION)/$(SDK_NAME)`
  and `OTHER_LDFLAGS = -framework sharedKit`. Set `ENABLE_USER_SCRIPT_SANDBOXING = NO`.
- Room uses `BundledSQLiteDriver`; if linking fails on sqlite symbols add `-lsqlite3` to OTHER_LDFLAGS.
- Check Swift names via Xcode "Generate Interface" if `MainViewControllerKt` doesn't resolve.

### 2. Bundle the question JSON (easy to miss — app shows no questions otherwise)
`DefaultQuestionLoader.ios.kt` reads `NSBundle.mainBundle.pathForResource(<name>, "json")`.
The files live in `app/src/main/res/raw/`: `drainsquestions.json`, `gasquestions.json`,
`plumbingquestions.json`. Add them to the iOS target's **Copy Bundle Resources** by reference
(don't duplicate — Android uses the same files). Do NOT dedupe questions; repeats are deliberate.

### 3. Run on simulator and walk the app
Type select → mode select → quiz question loads → answer → lives/streak → quiz complete.
Also check images (Compose resources in `shared/src/commonMain/composeResources/drawable/`)
actually render — on Android these silently went missing at runtime once (CMP-9547), compiling
clean proved nothing. Check safe areas / notch / landscape.

### 4. Billing (StoreKit 1, in Kotlin)
- Product IDs: `apprentice_weekly` (auto-renewing weekly sub, NZD$10/week) and `tradesman_lifetime`
  (non-consumable). Must be created in App Store Connect; for local testing add a
  `.storekit` configuration file to the scheme.
- **Do not port to StoreKit 2 from Kotlin** — its APIs are Swift-only and invisible to K/N cinterop.
  If StoreKit 2 is wanted, it needs a Swift shim exposed via an Objective-C-compatible interface.
- Known gaps to fix:
  - **No "Restore Purchases" button** — App Review requires one for the non-consumable. Needs
    `SKPaymentQueue.restoreCompletedTransactions()` + a button in the shared UI (wire through
    `QuizPlatformActions`, the interface MainActivity / `IosQuizPlatformActions` implement).
  - **Never downgrades**: an expired/cancelled weekly sub stays APPRENTICE forever. Android had the
    same bug and it was fixed in `BillingManager.android.kt` — mirror that logic (receipt check or
    re-validate on launch).
  - Failed/cancelled transactions are never `finishTransaction`'d.

### 5. AdMob on iOS
Currently no-ops (`PlatformBannerAd.ios.kt`, and the ad methods in `IosQuizPlatformActions`).
- Add Google Mobile Ads SDK via SPM. Needs `GADApplicationIdentifier` in Info.plist,
  `SKAdNetworkItems`, and an ATT prompt (`NSUserTrackingUsageDescription`).
- I need to create an **iOS app in AdMob** — the Android IDs (`ca-app-pub-7804898639422438/...`)
  are Android-only. Use Google's test IDs until I give real ones.
- Approach: implement banner/interstitial/rewarded in Swift, and pass them into Kotlin (e.g. make
  `MainViewController()` take a Swift-implemented interface/lambdas, and render the banner via
  `UIKitView`). Mirror Android behaviour in `app/src/main/java/.../addManager/adds/UnSkipAdd.kt`
  and `shared/src/androidMain/.../PlatformBannerAd.android.kt`.

### 6. Release prep
App icon (asset catalog, 1024px), launch screen, version 3.3 / build 1, Privacy manifest
(`PrivacyInfo.xcprivacy` — AdMob requires it), App Privacy answers, signing team, archive → TestFlight.

## Rules
- Keep business/UI logic in `shared/commonMain`; Swift stays a thin shell + SDK bridges.
- Don't break Android: after touching `shared`, also run `./gradlew :app:assembleDebug`.
- Don't hand-edit `shared/build/` output. Commit in small steps on branch `Design-changes`.
- Ask me before App Store Connect / AdMob / signing decisions — those are mine.
