package nl.fifthpostulate.specifikation.base

import nl.fifthpostulate.specifikation.*
import nl.fifthpostulate.specifikation.assertions.ReportAssert.Companion.assertThat
import org.junit.jupiter.api.*

class ViewTest {
    lateinit var subject: Person

    @BeforeEach
    fun `create a person with a name`() {
        subject = Person("Daan van Berkel")
    }

    @Test
    fun `when member specification succeeds view succeeds`() {
        val specification: Specification<Person, String> =
            View(String::isNotEmpty.toSpecification("is empty"), {violation -> "name $violation"}) {  it.name ?: "" }

        val report = specification.isMetBy(subject)

        assertThat(report).isSuccess()
    }

    @Test
    fun `when predicate fails specification fails`() {
        val specification: Specification<Person, String> =
            View(String::isNotEmpty.toSpecification("is empty"), {violation -> "name $violation"}) {  it.name ?: "" }

        val report = specification.isMetBy(Person(""))

        assertThat(report).isFailureWithViolations("name is empty")
    }

    @Test
    fun `when predicate fails, violation transform without arguments can be used`() {
        val transform: () -> String = { "no arguments" }
        val specification: Specification<Person, String> =
            View(String::isNotEmpty.toSpecification("is empty"), transform) { person: Person -> person.name ?: "" }

        val report = specification.isMetBy(Person(""))

        assertThat(report).isFailureWithViolations("no arguments")
    }

    @Test
    fun `when predicate fails, violation transform with violation as argument can be used`() {
        val specification: Specification<Person, String> =
            View(String::isNotEmpty.toSpecification("is empty"), { violation -> "name $violation" }) { it.name ?: "" }

        val report = specification.isMetBy(Person(""))

        assertThat(report).isFailureWithViolations("name is empty")
    }

    @Test
    fun `when predicate fails, violation transform with member and violation as arguments can be used`() {
        val specification: Specification<Person, String> =
            View(String::isNotEmpty.toSpecification("is empty"), {member, violation -> "name (\"$member\") $violation" }) { it.name ?: "" }

        val report = specification.isMetBy(Person(""))

        assertThat(report).isFailureWithViolations("name (\"\") is empty")
    }

    @Test
    fun `when predicate fails, violation transform with subject, member and violation as arguments can be used`() {
        val specification: Specification<Person, String> =
            View(String::isNotEmpty.toSpecification("is empty"), {subject, member, violation -> "[$subject] has name (\"$member\") that $violation" }) { it.name ?: "" }

        val report = specification.isMetBy(Person(""))

        assertThat(report).isFailureWithViolations("[Person(name=)] has name (\"\") that is empty")
    }
}
