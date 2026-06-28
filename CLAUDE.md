# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test

# Run a single unit test class
./gradlew :core:config:test --tests "abika.sinau.moviedbnew.core.config.SomeTest"

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Lint
./gradlew lint

# Sync after adding a new module (also include it in settings.gradle.kts first)
./gradlew --refresh-dependencies
```

## Tech Stack

| Layer | Tech |
|---|---|
| UI | Jetpack Compose + Material3 (BOM 2026.02.01) |
| DI | Koin 4.x |
| Network | Ktor 3.x (Android engine) |
| Serialization | kotlinx.serialization |
| Storage | Room + DataStore |
| Navigation | navigation3 (`androidx.navigation3`) |
| Image Loading | Coil 3.x |
| Async | Coroutines + Flow |

- Kotlin 2.2.10, AGP 9.2.1, min SDK 24, target SDK 36
- All dependency versions are in `gradle/libs.versions.toml`. Never hardcode versions in `build.gradle.kts`.

## Module Structure

```
app/                   — entry point; wires Koin, Navigation, GlobalErrorHandler
build-logic/
  convention/          — Gradle convention plugins (app.library, app.library.compose,
                         app.feature, app.domain, app.data)
core/
  common/              — utilities, extensions
  config/              — BaseViewModel<S,E,A>, EventsEffect, BackgroundEvent,
                         ResultState, BaseUseCase, GlobalErrorHandler (interface)
  design-system/       — reusable Composable components (Buttons, TextFields, Dialogs)
  navigation/          — @Serializable route objects for all features (AppRoutes.kt)
  network/             — Ktor client (KtorClientFactory), safeRequest(), DefaultGlobalErrorHandler
  shared-data-store/   — DataStore access layer (token, preferences)
  shared-model/        — domain models; ZERO dependencies to other modules
  shared-ui/           — Composable helpers (shimmer, AsyncImage wrappers); not design-system
domain/
  <feature>/           — repo interface + use cases (extend BaseUseCase)
data/
  <feature>/           — ServiceImpl (Ktor) + DTO + mapper + RepositoryImpl + Koin module
feature/
  <feature>/           — Screen + ViewModel (extends BaseViewModel) + State/Event/Action
```

### Dependency Rules (strict — do not break)

- `core:shared-model` → **zero** project dependencies; standalone pure types only
- `core:config` → no project dependencies (provides abstractions, depends on nothing)
- `domain/*` → may depend on: `core:config`, `core:shared-model`, `core:shared-data-store`
- `data/*` → may depend on: `core:network`, `core:shared-data-store`, `core:shared-model`
- `feature/*` → may depend on: its domain module, `core:design-system`, `core:shared-ui`, `core:shared-model`, `core:navigation`, `core:config`, `core:network`
- Feature modules **must NOT** depend directly on `data/*`

## Convention Plugins (build-logic)

Use the correct plugin in each module's `build.gradle.kts`. Plugins auto-include their transitive dependencies — only add extras not covered.

| Plugin alias | Purpose | Auto-includes |
|---|---|---|
| `app.library` | Base Android library | core-ktx, koin-android, coroutines |
| `app.library.compose` | Library + Compose | everything in app.library + Compose BOM, Material3, koin-compose, lifecycle-runtime-compose |
| `app.feature` | Feature UI module | everything in app.library.compose + koin-compose-viewmodel, navigation3, Coil, lifecycle-viewmodel-compose, core:{config,design-system,shared-ui,shared-model,navigation,network} |
| `app.domain` | Domain module | everything in app.library + core:{config,shared-model,shared-data-store} |
| `app.data` | Data/repository module | everything in app.library + kotlin-serialization, ksp, room plugins + Room, DataStore, Ktor, core:{network,shared-data-store,shared-model} |

Minimal `build.gradle.kts` for a new feature module:
```kotlin
plugins { alias(libs.plugins.app.feature) }
android { namespace = "abika.sinau.moviedbnew.feature.home" }
dependencies {
    implementation(project(":domain:movie")) // only module-specific extras
}
```

Also add it to `settings.gradle.kts`:
```kotlin
include(":feature:home")
```

## MVI Pattern

Every screen follows `BaseViewModel<S, E, A>` from `core:config`. Define State, Event, and Action in one file (e.g. `HomeState.kt`):

- **State** — immutable UI snapshot via `StateFlow`, drives recomposition
- **Action** — only path for UI → ViewModel; queued in a `Channel`, processed serially in `handleAction()`
- **Event** — one-shot signals (navigation, toast) via `Channel` so they aren't replayed on recomposition

```kotlin
// HomeState.kt
data class HomeState(val isLoading: Boolean = false, val movies: List<Movie> = emptyList())

sealed class HomeEvent {
    data class NavigateToDetail(val id: Int) : HomeEvent()
}

sealed class HomeAction {
    data object LoadMovies : HomeAction()
    data class MovieClicked(val id: Int) : HomeAction()
}

// HomeViewModel.kt
class HomeViewModel(
    private val getMoviesUseCase: GetMoviesUseCase
) : BaseViewModel<HomeState, HomeEvent, HomeAction>(HomeState()) {

    init { trySendAction(HomeAction.LoadMovies) }

    override suspend fun handleAction(action: HomeAction) = when (action) {
        HomeAction.LoadMovies -> loadMovies()
        is HomeAction.MovieClicked -> sendEvent(HomeEvent.NavigateToDetail(action.id))
    }

    private suspend fun loadMovies() {
        updateState { copy(isLoading = true) }
        when (val result = getMoviesUseCase()) {
            is ResultState.Success -> updateState { copy(isLoading = false, movies = result.data) }
            is ResultState.Error -> { updateState { copy(isLoading = false) }; handlerError(result) }
            else -> Unit
        }
    }
}

// HomeScreen.kt
@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel) { event ->
        when (event) {
            is HomeEvent.NavigateToDetail -> backStack.add(MovieDetailRoute(event.id))
        }
    }
    // ... render state
}
```

**Why events are not in State:** storing navigation in State (e.g. `navigateTo: Route? = null`) requires the UI to null it after consuming — easy to forget, causes double-navigation on back press. `Channel`-based events auto-consume.

**`BackgroundEvent`:** for events that must fire even when the lifecycle is below RESUMED (e.g. splash navigation), implement the `BackgroundEvent` marker interface. `EventsEffect` delivers these immediately; all others wait for `RESUMED`.

**Pitfall — double 401 handling:** if Ktor auth plugin handles 401 (token refresh/force logout), do NOT also handle `ErrorType.Auth` in `DefaultGlobalErrorHandler`. Pick one location.

## Navigation3 Setup

Routes are `@Serializable` objects/data classes in `core:navigation/AppRoutes.kt`. Add new routes there — not in individual feature modules.

```kotlin
// In app module
@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(SplashRoute)
    NavDisplay(backStack = backStack, onBack = { backStack.removeLastOrNull() }) {
        entry<SplashRoute> { SplashScreen(...) }
        entry<HomeRoute> { HomeScreen(...) }
        entry<MovieDetailRoute> { entry -> DetailScreen(movieId = entry.key.movieId) }
    }
}
```

Key differences from old Navigation Compose: `rememberNavBackStack()` replaces `rememberNavController()`, `NavDisplay` replaces `NavHost`, `entry<Route>` replaces `composable<Route>`, navigate = `backStack.add(route)`, back = `backStack.removeLastOrNull()`.

## Data Layer Pattern

```
Service (interface in data/) → ServiceImpl (Ktor) → RepositoryImpl (maps DTO→domain) → Koin module
Repo interface lives in domain/, not data/
```

Use `client.safeRequest { ... }` from `core:network` for all Ktor calls — returns `ResultState<T>` and handles network/timeout/HTTP errors uniformly.

## App Module Wiring (one-time setup)

The `app` module must register:
1. `HttpClient` via `createKtorClient(baseUrl, ...)` from `core:network`
2. `GlobalErrorHandler` via `DefaultGlobalErrorHandler(onAuthError, onShowMessage)` from `core:network`
3. All feature/data Koin modules via `startKoin { modules(...) }`
4. `AppNavigation()` as the Compose content in `MainActivity`

## KSP Version

KSP follows the format `kotlinVersion-2.0.x`. For Kotlin 2.2.10, the correct version is `2.2.10-2.0.2`. If you upgrade Kotlin, find the matching KSP at https://github.com/google/ksp/releases.
