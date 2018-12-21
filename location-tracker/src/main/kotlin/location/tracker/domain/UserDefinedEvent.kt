package location.tracker.domain

/**
 * Thrown whenever the user for this application has been defined.
 */
data class UserDefinedEvent(
   val newUser: User
)