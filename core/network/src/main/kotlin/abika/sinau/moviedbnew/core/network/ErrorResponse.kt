package abika.sinau.moviedbnew.core.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    // Generic format
    val message: String? = null,
    val error: String? = null,
    // TMDB API format
    @SerialName("status_message") val statusMessage: String? = null,
    @SerialName("status_code") val statusCode: Int? = null
) {
    fun getErrorMessage(): String = message ?: statusMessage ?: error ?: "Terjadi kesalahan"
}
