package nl.fifthpostulate.specifikation.base

import nl.fifthpostulate.specifikation.*
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ViewTest {
    lateinit var subject: Person

    @BeforeEach
    fun `create a person with a name`() {
        subject = Person("Daan van Berkel")
    }

    @Test
    fun `when member specification succeeds view succeeds`() {
        val specification: Specification<Person> =
            View(String::isNotEmpty.toSpecification("is empty"), "name ") {  it.name ?: "" }

        val report = specification.isMetBy(subject)

        assertThat(report is Success).isTrue()
    }

    @Test
    fun `when predicate fails specification fails`() {
        val specification: Specification<Person> =
            View(String::isNotEmpty.toSpecification("is empty"), "name ") {  it.name ?: "" }

        val report = specification.isMetBy(Person(""))

        assertThat(report is Failure).isTrue()
        assertThat((report as Failure).violations).containsExactly("name is empty")
    }
}
