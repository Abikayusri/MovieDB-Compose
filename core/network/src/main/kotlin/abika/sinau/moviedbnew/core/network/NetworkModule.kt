package abika.sinau.moviedbnew.core.network

import org.koin.dsl.module

/**
 * Provides the base Koin module for networking.
 * The app module must register:
 *   - HttpClient via createKtorClient(baseUrl, ...)
 *   - GlobalErrorHandler via DefaultGlobalErrorHandler(onAuthError, onShowMessage)
 *
 * Example in app/src/main/.../AppModule.kt:
 *   val appModule = module {
 *       single { createKtorClient(baseUrl = BuildConfig.BASE_URL) { ... } }
 *       single<GlobalErrorHandler> {
 *           DefaultGlobalErrorHandler(
 *               onAuthError = { /* navigate to login */ },
 *               onShowMessage = { msg -> /* show toast/snackbar */ }
 *           )
 *       }
 *   }
 */
val networkModule = module {
    // Nothing here by default — app module provides HttpClient and GlobalErrorHandler
}
