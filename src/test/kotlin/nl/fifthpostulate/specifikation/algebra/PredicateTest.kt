package nl.fifthpostulate.specifikation.algebra

import nl.fifthpostulate.specifikation.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*


class PredicateTest {
    lateinit var subject: Person

    @BeforeEach
    fun `create a person with a name`() {
        subject = Person("Daan van Berkel")
    }

    @Test
    fun `when predicate succeeds specification succeeds`() {
        val specification: Specification<Person> = Predicate<Person>("name is not null") { it.name != null }

        val report = specification.verify(subject)

        assertThat(report is Success).isTrue()
    }

    @Test
    fun `when predicate fails specification fails`() {
        val specification: Specification<Person> = Predicate<Person>("name is not null") { it.name != null }

        val report = specification.verify(Person(null))

        assertThat(report is Failure).isTrue()
    }

    @Test
    fun `specification can be made from a predicate`() {
        val specification: Specification<Person> = ::nameNotNull.toSpecification("name is not null")

        val report = specification.verify(subject)

        assertThat(report is Success).isTrue()
    }
}

data class Person(val name: String?)

fun nameNotNull(subject: Person): Boolean =
    subject.name != null
