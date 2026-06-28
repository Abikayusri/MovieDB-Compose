package abika.sinau.moviedbnew.core.config

/**
 * Marker interface for events that must be delivered even when the lifecycle is below RESUMED
 * (e.g. splash screen navigation that fires before the UI is visible).
 * Events without this marker are held by EventsEffect until the lifecycle reaches RESUMED.
 */
interface BackgroundEvent
