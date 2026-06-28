package abika.sinau.moviedbnew.core.config

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.withResumed

/**
 * Collects one-shot events from a [BaseViewModel].
 * Non-[BackgroundEvent] events are held until lifecycle reaches RESUMED to prevent
 * navigation or UI side-effects from firing while the app is in the background.
 */
@Composable
fun <E> EventsEffect(
    viewModel: BaseViewModel<*, E, *>,
    handler: suspend (E) -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val handlerState by rememberUpdatedState(handler)

    LaunchedEffect(viewModel, lifecycle) {
        viewModel.eventFlow.collect { event ->
            if (event is BackgroundEvent) {
                handlerState(event)
            } else {
                lifecycle.withResumed { handlerState(event) }
            }
        }
    }
}
