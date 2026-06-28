package abika.sinau.moviedbnew.core.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseViewModel<S, E, A>(
    initialState: S
) : ViewModel(), KoinComponent {

    private val _stateFlow = MutableStateFlow(initialState)
    val stateFlow: StateFlow<S> = _stateFlow.asStateFlow()

    private val _eventChannel = Channel<E>(Channel.BUFFERED)
    val eventFlow = _eventChannel.receiveAsFlow()

    // UNLIMITED so trySendAction never drops actions during fast user interactions
    private val _actionChannel = Channel<A>(Channel.UNLIMITED)

    private val globalErrorHandler: GlobalErrorHandler by inject()

    init {
        viewModelScope.launch {
            for (action in _actionChannel) {
                handleAction(action)
            }
        }
    }

    abstract suspend fun handleAction(action: A)

    protected fun updateState(reducer: S.() -> S) {
        _stateFlow.update { it.reducer() }
    }

    protected suspend fun sendEvent(event: E) {
        _eventChannel.send(event)
    }

    fun trySendAction(action: A) {
        _actionChannel.trySend(action)
    }

    protected fun handlerError(error: ResultState.Error) {
        globalErrorHandler.handle(error)
    }

    override fun onCleared() {
        super.onCleared()
        _actionChannel.close()
        _eventChannel.close()
    }
}
