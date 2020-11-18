package nl.fifthpostulate.specifikation.collection

import nl.fifthpostulate.specifikation.*
import nl.fifthpostulate.specifikation.assertions.ReportAssert.Companion.assertThat
import nl.fifthpostulate.specifikation.base.*
import nl.fifthpostulate.specifikation.collection.OneOf as OneOfCollection
import org.junit.jupiter.api.*

class OneOfTest {
    lateinit var subjects: Collection<Person>
    val specification: Specification<Person, String> = ::nameNotNull.toSpecification("name is null")

    @BeforeEach
    fun `create subject under test`() {
        subjects = listOf(
            Person("Daan van Berkel"),
            Person("Geert Mulders")
        )
    }

    @Test
    fun `when all sub specifications succeed then one of succeeds`() {
        val specification = OneOfCollection(specification)

        val report = specification.isMetBy(subjects)

        assertThat(report).isSuccess()
    }

    @Test
    fun `when one sub specifications fails but other succeeds then one of succeeds`() {
        val specification = OneOfCollection(specification)

        val report = specification.isMetBy(listOf(Person(null), Person(null), Person("Daan van Berkel")))

        assertThat(report).isSuccess()
    }

    @Test
    fun `when failing violations are collected`() {
        val specification = OneOfCollection(specification)

        val report = specification.isMetBy(listOf(Person(null), Person(null), Person(null)))

        assertThat(report).isFailureWithViolations(Pair(0, "name is null"), Pair(1, "name is null"), Pair(2, "name is null"))
    }
}
