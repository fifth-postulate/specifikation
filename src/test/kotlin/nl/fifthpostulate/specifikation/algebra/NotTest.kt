package nl.fifthpostulate.specifikation.algebra

import nl.fifthpostulate.specifikation.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.*

class NotTest {
    lateinit var subject: Person

    @BeforeEach
    fun `create subject under test`() {
        subject = Person("test")
    }

    @Test
    fun `should invert report for success into failure`() {
        val specification: Specification<Person> = Not(Succeed())

        val result = specification.verify(subject)

        Assertions.assertThat(result is Failure).isTrue()
    }

    @Test
    fun `should invert report for failure into success`() {
        val specification: Specification<Person> = Not(Fail("first"))

        val result = specification.verify(subject)

        Assertions.assertThat(result is Success).isTrue()
    }
}
