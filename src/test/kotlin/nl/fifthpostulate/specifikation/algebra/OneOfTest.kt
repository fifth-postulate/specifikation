package nl.fifthpostulate.specifikation.algebra

import nl.fifthpostulate.specifikation.*
import nl.fifthpostulate.specifikation.assertions.ReportAssert.Companion.assertThat
import org.junit.jupiter.api.*


class OneOfTest {
    lateinit var subject: Person

    @BeforeEach
    fun `create subject under test`() {
        subject = Person("test")
    }

    @Test
    fun `when all sub specifications succeed then one of succeeds`() {
        val specification: Specification<Person, String> = OneOf(Succeed(), Succeed(), Succeed())

        val report = specification.isMetBy(subject)

        assertThat(report).isSuccess()
    }

    @Test
    fun `when one sub specifications fails but other succeeds then one of succeeds`() {
        val specification: Specification<Person, String> = OneOf(Fail("first"), Fail("second"), Succeed(), Fail("third"))

        val report = specification.isMetBy(subject)

        assertThat(report).isSuccess()
    }

    @Test
    fun `when failing violations are collected`() {
        val specification: Specification<Person, String> = OneOf(Fail("first"), Fail("second"))

        val report = specification.isMetBy(subject)

        assertThat(report).isFailureWithViolations("first", "second")
    }
}
