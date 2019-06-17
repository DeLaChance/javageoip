import spock.lang.Specification

class DummySpec extends Specification {

    def "test_that_two_strings_can_be_concatenated"() {
        given:
        def a = "a"
        def b = "b"

        when:
        def c = a + b

        then:
        c == "ab"
    }
}
