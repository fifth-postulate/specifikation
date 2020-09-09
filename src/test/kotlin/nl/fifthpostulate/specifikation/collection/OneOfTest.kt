package nl.fifthpostulate.specifikation.collection

import nl.fifthpostulate.specifikation.*
import nl.fifthpostulate.specifikation.base.nameNotNull
import nl.fifthpostulate.specifikation.base.toSpecification
import nl.fifthpostulate.specifikation.collection.OneOf as OneOfCollection
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OneOfTest {
    lateinit var subjects: Collection<Person>
    val specification: Specification<Person> = ::nameNotNull.toSpecification("name is null")

    @BeforeEach
    fun `create subject under test`() {
        subjects = listOf(
            Person("Daan van Berkel"),
            Person("Geert Mulders")
        )
    }

    @Test
    fun `when all sub specifications succeed then one of succeeds`() {
        val specification: Specification<Iterable<Person>> = OneOfCollection(specification)

        val result = specification.isMetBy(subjects)

        Assertions.assertThat(result is Success).isTrue()
    }

    @Test
    fun `when one sub specifications fails but other succeeds then one of succeeds`() {
        val specification: Specification<Iterable<Person>> = OneOfCollection(specification)

        val result = specification.isMetBy(listOf(Person(null), Person(null), Person("Daan van Berkel")))

        Assertions.assertThat(result is Success).isTrue()
    }

    @Test
    fun `when failing violations are collected`() {
        val specification: Specification<Iterable<Person>> = OneOfCollection(specification)

        val result = specification.isMetBy(listOf(Person(null), Person(null), Person(null)))

        Assertions.assertThat(result is Failure).isTrue()
        Assertions.assertThat((result as Failure).violations).containsExactly("[0] name is null", "[1] name is null", "[2] name is null")
    }

}
