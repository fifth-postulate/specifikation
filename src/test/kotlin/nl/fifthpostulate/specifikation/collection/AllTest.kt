package nl.fifthpostulate.specifikation.collection

import nl.fifthpostulate.specifikation.*
import nl.fifthpostulate.specifikation.assertions.ReportAssert.Companion.assertThat
import nl.fifthpostulate.specifikation.base.*
import nl.fifthpostulate.specifikation.collection.All as AllForCollection
import org.junit.jupiter.api.*

class AllTest {
    lateinit var subjects: Collection<Person>
    val specification: Specification<Person, String> = ::nameNotNull.toSpecification("name is null")

    @BeforeEach
    fun `create subjects under test`() {
        subjects = listOf(
            Person("Daan van Berkel"),
            Person("Geert Mulders")
        )
    }

    @Test
    fun `when all sub specifications succeed then all succeeds`() {
        val specification = AllForCollection(specification)

        val report = specification.isMetBy(subjects)

        assertThat(report).isSuccess()
    }

    @Test
    fun `when one sub specifications fails then all fail`() {
        val specification = AllForCollection(specification)

        val report = specification.isMetBy(listOf(Person(null)))

        assertThat(report).isFailure()
    }

    @Test
    fun `when failing violations are collected`() {
        val specification = AllForCollection(specification)

        val report = specification.isMetBy(listOf(Person(null), Person("Daan van Berkel"), Person(null)))

        assertThat(report).isFailureWithViolations(Pair(0,"name is null"), Pair(2, "name is null"))
    }
}
