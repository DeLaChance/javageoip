package location.tracker.domain

/**
 * The user of this location-tracker application
 */
class User(
    val id: String,
    val name: String,
    val keyWords: List<String>
) {

    init {
        if( this.id.isEmpty() ) {
            throw IllegalArgumentException("${this.id} is not a valid id")
        }

        if( this.name.isEmpty() ) {
            throw IllegalArgumentException("${this.name} is not a valid name")
        }
    }

    override fun toString(): String {
        return "{ id=${id}, name=${name}, keyWords=[${keyWords}] }"
    }

}
