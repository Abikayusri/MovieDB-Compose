# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test

# Run a single unit test class
./gradlew test --tests "abika.sinau.moviedbnew.ExampleUnitTest"

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Lint check
./gradlew lint

# Clean build
./gradlew clean assembleDebug
```

## Tech Stack

- **Language**: Kotlin 2.2.10
- **UI**: Jetpack Compose with Material3 (Compose BOM 2026.02.01)
- **Min SDK**: 24, **Target SDK**: 36
- **AGP**: 9.2.1
- **Build system**: Gradle with Kotlin DSL (`build.gradle.kts`), version catalog at `gradle/libs.versions.toml`

## Project Structure

Single-module app (`app/`). Package root: `abika.sinau.moviedbnew`.

```
app/src/main/java/abika/sinau/moviedbnew/
  MainActivity.kt          — entry point, sets up Compose content
  ui/theme/
    Color.kt               — color tokens
    Theme.kt               — MovieDBNewTheme (supports dynamic color on Android 12+)
    Type.kt                — typography scale
```

## Architecture Notes

The project is in its initial scaffold state — no navigation, ViewModel, DI, or network layer has been added yet. When building out MovieDB features, the expected additions are:

- **Navigation**: Compose Navigation (`androidx.navigation:navigation-compose`)
- **ViewModel / state**: `lifecycle-viewmodel-compose`
- **Networking**: Retrofit + OkHttp, or Ktor
- **DI**: Hilt (`hilt-android`)
- **Image loading**: Coil (`io.coil-kt:coil-compose`)
- **Data layer**: Repository pattern with coroutines/Flow

All new dependencies should be declared in `gradle/libs.versions.toml` and referenced via `libs.*` aliases — never hardcode version strings in `build.gradle.kts`.

## Compose Conventions

- Edge-to-edge is enabled via `enableEdgeToEdge()` in `MainActivity`; use `WindowInsets` / `Scaffold` padding to avoid content being clipped by system bars.
- Theme is `MovieDBNewTheme` — wrap all previews with it.
- Prefer `@Preview` annotations on dedicated preview composables rather than on the main composable directly.
