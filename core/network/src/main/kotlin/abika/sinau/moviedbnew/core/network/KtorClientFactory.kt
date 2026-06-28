package abika.sinau.moviedbnew.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createKtorClient(
    baseUrl: String,
    onRequestConfig: (io.ktor.client.request.HttpRequestBuilder.() -> Unit)? = null
): HttpClient = HttpClient(Android) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    install(Logging) {
        level = LogLevel.HEADERS
    }
    defaultRequest {
        url(baseUrl)
        onRequestConfig?.invoke(this)
    }
}
