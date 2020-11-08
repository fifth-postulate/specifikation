package nl.fifthpostulate.specifikation.collection

import nl.fifthpostulate.specifikation.*
import nl.fifthpostulate.specifikation.base.nameNotNull
import nl.fifthpostulate.specifikation.base.toSpecification
import nl.fifthpostulate.specifikation.collection.All as AllForCollection
import org.assertj.core.api.Assertions.assertThat
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

        val result = specification.isMetBy(subjects)

        assertThat(result is Success).isTrue()
    }

    @Test
    fun `when one sub specifications fails then all fail`() {
        val specification = AllForCollection(specification)

        val result = specification.isMetBy(listOf(Person(null)))

        assertThat(result is Failure).isTrue()
    }

    @Test
    fun `when failing violations are collected`() {
        val specification = AllForCollection(specification)

        val result = specification.isMetBy(listOf(Person(null), Person("Daan van Berkel"), Person(null)))

        assertThat(result is Failure).isTrue()
        assertThat((result as Failure).violations).containsExactly(Pair(0,"name is null"), Pair(2, "name is null"))
    }
}
