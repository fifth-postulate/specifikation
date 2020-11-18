package nl.fifthpostulate.specifikation.algebra

import nl.fifthpostulate.specifikation.*
import nl.fifthpostulate.specifikation.assertions.ReportAssert.Companion.assertThat
import org.junit.jupiter.api.*


class AllTest {
    lateinit var subject: Person

    @BeforeEach
    fun `create subject under test`() {
        subject = Person("test")
    }

    @Test
    fun `when all sub specifications succeed then all succeeds`() {
        val specification: Specification<Person, String> = All(Succeed(), Succeed(), Succeed())

        val report = specification.isMetBy(subject)

        assertThat(report).isSuccess()
    }

    @Test
    fun `when one sub specifications fails then all fail`() {
        val specification: Specification<Person, String> = All(Succeed(), Succeed(), Succeed(), Fail("first"))

        val report = specification.isMetBy(subject)

        assertThat(report).isFailure()
    }

    @Test
    fun `when failing violations are collected`() {
        val specification: Specification<Person, String> = All(Succeed(), Fail("first"), Succeed(), Succeed(), Fail("second"))

        val report = specification.isMetBy(subject)

        assertThat(report).isFailureWithViolations("first", "second")
    }
}
