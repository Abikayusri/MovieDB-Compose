package abika.sinau.moviedbnew.core.navigation

import kotlinx.serialization.Serializable

// All navigation destinations across the app live here.
// Add new routes as feature modules are added.

@Serializable
data object SplashRoute

@Serializable
data object HomeRoute

@Serializable
data class MovieDetailRoute(val movieId: Int)
