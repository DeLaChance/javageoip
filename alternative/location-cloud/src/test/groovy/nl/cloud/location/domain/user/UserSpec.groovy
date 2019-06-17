package nl.cloud.location.domain.user

import nl.cloud.location.domain.user.User
import spock.lang.Specification

/**
 * Unit test for {@link nl.cloud.location.domain.user.User}.
 *
 */
class UserSpec extends Specification {

    def instanceToBeTested

    def "test that a user can be initialized"() {
        given:
        def name = "John Snow"

        when:
        instanceToBeTested = new User(name)

        then:
        instanceToBeTested.getId() != null
        instanceToBeTested.getName() == name
        instanceToBeTested.getKeyWords() == []
    }

    def "test that a user can be assigned keywords"() {
        given:
        def name = "John Snow"
        def keyWords = ["Lord Commander of the Night's Watch", "Azor Ahai"]

        instanceToBeTested = new User(name)

        when:
        keyWords.each {keyword ->
            instanceToBeTested.addKeyword(keyword)
        }

        then:
        instanceToBeTested.getId() != null
        instanceToBeTested.getName() == name
        instanceToBeTested.getKeyWords() == keyWords
    }
}
