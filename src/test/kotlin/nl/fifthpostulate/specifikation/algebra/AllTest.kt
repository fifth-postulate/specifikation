package nl.fifthpostulate.specifikation.algebra

import nl.fifthpostulate.specifikation.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*


class AllTest {
    lateinit var subject: Person

    @BeforeEach
    fun `create subject under test`() {
        subject = Person("test")
    }

    @Test
    fun `when all sub specifications succeed then all succeeds`() {
        val specification: Specification<Person> = All(Succeed(), Succeed(), Succeed())

        val result = specification.verify(subject)

        assertThat(result is Success).isTrue()
    }

    @Test
    fun `when one sub specifications fails then all fail`() {
        val specification: Specification<Person> = All(Succeed(), Succeed(), Succeed(), Fail("first"))

        val result = specification.verify(subject)

        assertThat(result is Failure).isTrue()
    }

    @Test
    fun `when failing violations are collected`() {
        val specification: Specification<Person> = All(Succeed(), Fail("first"), Succeed(), Succeed(), Fail("second"))

        val result = specification.verify(subject)

        assertThat(result is Failure).isTrue()
        assertThat((result as Failure).violations).containsExactly("first", "second")
    }
}
