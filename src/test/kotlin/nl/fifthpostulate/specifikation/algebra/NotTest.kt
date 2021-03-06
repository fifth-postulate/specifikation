package nl.fifthpostulate.specifikation.algebra

import nl.fifthpostulate.specifikation.*
import nl.fifthpostulate.specifikation.assertions.ReportAssert.Companion.assertThat
import org.junit.jupiter.api.*

class NotTest {
    lateinit var subject: Person

    @BeforeEach
    fun `create subject under test`() {
        subject = Person("test")
    }

    @Test
    fun `should invert report for success into failure`() {
        val specification: Specification<Person, String> = Not(Succeed())

        val report = specification.isMetBy(subject)

        assertThat(report).isFailure()
    }

    @Test
    fun `should invert report for failure into success`() {
        val specification: Specification<Person, String> = Not(Fail("first"))

        val report = specification.isMetBy(subject)

        assertThat(report).isSuccess()
    }
}
