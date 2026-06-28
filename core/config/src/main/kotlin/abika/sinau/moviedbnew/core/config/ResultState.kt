package abika.sinau.moviedbnew.core.config

sealed class ResultState<out T> {
    data object Loading : ResultState<Nothing>()
    data class Success<T>(val data: T) : ResultState<T>()
    data class Error(
        val code: Int? = null,
        val message: String,
        val cause: Throwable? = null,
        val type: ErrorType = ErrorType.Unknown
    ) : ResultState<Nothing>()
}

enum class ErrorType { Network, Auth, Timeout, Validation, Server, Unknown }
