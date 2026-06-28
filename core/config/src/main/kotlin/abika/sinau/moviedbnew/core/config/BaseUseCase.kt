package abika.sinau.moviedbnew.core.config

abstract class BaseUseCase<P, R> {
    abstract suspend fun execute(params: P): ResultState<R>
    suspend operator fun invoke(params: P): ResultState<R> = execute(params)
}

abstract class BaseUseCaseNoParam<R> {
    abstract suspend fun execute(): ResultState<R>
    suspend operator fun invoke(): ResultState<R> = execute()
}
