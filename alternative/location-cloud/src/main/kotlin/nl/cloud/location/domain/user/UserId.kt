package nl.cloud.location.domain.user

import java.util.*

class UserId(val value: String) {

    companion object {
        fun generate(): UserId = UserId(UUID.randomUUID().toString())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserId
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return value
    }

}

