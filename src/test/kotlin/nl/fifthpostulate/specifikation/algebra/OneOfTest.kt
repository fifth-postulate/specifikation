package nl.fifthpostulate.specifikation.algebra

import nl.fifthpostulate.specifikation.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*


class OneOfTest {
    lateinit var subject: Person

    @BeforeEach
    fun `create subject under test`() {
        subject = Person("test")
    }

    @Test
    fun `when all sub specifications succeed then one of succeeds`() {
        val specification: Specification<Person> = OneOf(Succeed(), Succeed(), Succeed())

        val result = specification.isMetBy(subject)

        assertThat(result is Success).isTrue()
    }

    @Test
    fun `when one sub specifications fails but other succeeds then one of succeeds`() {
        val specification: Specification<Person> = OneOf(Fail("first"), Fail("second"), Succeed(), Fail("third"))

        val result = specification.isMetBy(subject)

        assertThat(result is Success).isTrue()
    }

    @Test
    fun `when failing violations are collected`() {
        val specification: Specification<Person> = OneOf(Fail("first"), Fail("second"))

        val result = specification.isMetBy(subject)

        assertThat(result is Failure).isTrue()
        assertThat((result as Failure).violations).containsExactly("first", "second")
    }
}
