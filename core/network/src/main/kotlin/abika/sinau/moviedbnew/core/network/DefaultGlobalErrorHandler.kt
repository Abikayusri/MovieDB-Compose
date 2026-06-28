package abika.sinau.moviedbnew.core.network

import abika.sinau.moviedbnew.core.config.ErrorType
import abika.sinau.moviedbnew.core.config.GlobalErrorHandler
import abika.sinau.moviedbnew.core.config.ResultState

class DefaultGlobalErrorHandler(
    private val onAuthError: () -> Unit,
    private val onShowMessage: (String) -> Unit
) : GlobalErrorHandler {
    override fun handle(error: ResultState.Error) {
        when (error.type) {
            ErrorType.Auth -> onAuthError()
            else -> onShowMessage(error.message)
        }
    }
}
