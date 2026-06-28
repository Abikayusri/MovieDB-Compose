package abika.sinau.moviedbnew.core.network

import abika.sinau.moviedbnew.core.config.ErrorType
import abika.sinau.moviedbnew.core.config.ResultState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import java.io.IOException

suspend inline fun <reified T> HttpClient.safeRequest(
    block: HttpClient.() -> HttpResponse
): ResultState<T> = try {
    val response = block()
    if (response.status.isSuccess()) {
        ResultState.Success(response.body())
    } else {
        val errorBody = runCatching { response.body<ErrorResponse>() }.getOrNull()
        ResultState.Error(
            code = response.status.value,
            message = errorBody?.getErrorMessage() ?: "Terjadi kesalahan",
            type = mapHttpErrorType(response.status.value)
        )
    }
} catch (e: HttpRequestTimeoutException) {
    ResultState.Error(
        code = null,
        message = "Koneksi timeout",
        cause = e,
        type = ErrorType.Timeout
    )
} catch (e: IOException) {
    ResultState.Error(
        code = null,
        message = "Tidak ada koneksi internet",
        cause = e,
        type = ErrorType.Network
    )
} catch (e: Exception) {
    ResultState.Error(
        code = null,
        message = e.message ?: "Terjadi kesalahan",
        cause = e
    )
}

fun mapHttpErrorType(code: Int): ErrorType = when (code) {
    401, 403 -> ErrorType.Auth
    408, 504 -> ErrorType.Timeout
    in 400..499 -> ErrorType.Validation
    in 500..599 -> ErrorType.Server
    else -> ErrorType.Unknown
}
