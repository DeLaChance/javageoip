package nl.cloud.location.configuration

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("location.cloud")
                .mainClass(Application.javaClass)
                .start()
    }
}