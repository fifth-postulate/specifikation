package nl.fifthpostulate.specifikation.assertions

import nl.fifthpostulate.specifikation.*
import nl.fifthpostulate.specifikation.assertions.ReportAssert.Companion.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.AssertionError

class ReportAssertTest {
    @Test
    fun `isSuccess should pass with a successful report`() {
        val report: Report<String> = Success()

        assertThat(report).isSuccess()
    }

    @Test
    fun `isSucces should fail with a failure report`() {
        val report: Report<String> = Failure("this is wrong")

        assertThrows<AssertionError>("a failure report should not be a Success") { assertThat(report).isSuccess() }
    }

    @Test
    fun `isFailure should pass with a failure report`() {
        val report: Report<String> = Failure("this is wrong")

        assertThat(report).isFailure()
    }

    @Test
    fun `isFailure should fail with a success report`() {
        val report: Report<String> = Success()

        assertThrows<AssertionError>("a success report should not be a Failure") { assertThat(report).isFailure() }
    }

    @Test
    fun `isFailureWithViolations should pass when failure with correct violations`() {
        val report: Report<String> = Failure("this is wrong", "this is also wrong")

        assertThat(report).isFailureWithViolations("this is wrong", "this is also wrong")
    }

    @Test
    fun `isFailureWithViolations should fail when failure with a missing violation`() {
        val report: Report<String> = Failure("this is wrong")

        assertThrows<AssertionError>("when a violation is missing isFailureWithViolation should fail") {
            assertThat(report).isFailureWithViolations("this is wrong", "this is also wrong")
        }
    }

    @Test
    fun `isFailureWithViolations should fail when failure has violations out of order`() {
        val report: Report<String> = Failure("this is also wrong", "this is wrong")

        assertThrows<AssertionError>("when a violations are out of order isFailureWithViolation should fail") {
            assertThat(report).isFailureWithViolations("this is wrong", "this is also wrong")
        }
    }

    @Test
    fun `isFailureWithViolations should fail when failure has with extra violations`() {
        val report: Report<String> = Failure("this is wrong", "this is also wrong", "this is extra")

        assertThrows<AssertionError>("when there are extra violations isFailureWithViolation should fail") {
            assertThat(report).isFailureWithViolations("this is wrong", "this is also wrong")
        }
    }


    @Test
    fun `isFailureWithViolations should fail when report is Success`() {
        val report: Report<String> = Success()

        assertThrows<AssertionError>("when report is a success isFailureWithViolation should fail") {
            assertThat(report).isFailureWithViolations("this is wrong", "this is also wrong")
        }
    }
}
