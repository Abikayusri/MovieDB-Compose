package abika.sinau.moviedbnew.core.config

interface GlobalErrorHandler {
    fun handle(error: ResultState.Error)
}
