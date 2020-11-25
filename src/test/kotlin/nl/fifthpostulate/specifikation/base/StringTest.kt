package nl.fifthpostulate.specifikation.base

import nl.fifthpostulate.specifikation.assertions.ReportAssert.Companion.assertThat
import org.junit.jupiter.api.Test

class StringTest {
    @Test
    fun `isBlank should succeed when the string is blank`() {
        assertThat(isBlank.isMetBy("    ")).isSuccess()
    }

    @Test
    fun `isBlank should fail when the string is not blank`() {
        assertThat(isBlank.isMetBy("not blank string")).isFailureWithViolations("string is not blank")
    }

    @Test
    fun `isNotBlank should succeed when the string is not blank`() {
        assertThat(isNotBlank.isMetBy("not blank string")).isSuccess()
    }

    @Test
    fun `isNotBlank should fail when the string is blank`() {
        assertThat(isNotBlank.isMetBy("     ")).isFailureWithViolations("string is blank")
    }

    @Test
    fun `isEmpty should succeed when the string is empty`() {
        assertThat(isEmpty.isMetBy("")).isSuccess()
    }

    @Test
    fun `isEmpty should fail when the string is not empty`() {
        assertThat(isEmpty.isMetBy("not empty string")).isFailureWithViolations("string is not empty")
    }

    @Test
    fun `isNotEmpty should succeed when the string is not empty`() {
        assertThat(isNotEmpty.isMetBy("not empty string")).isSuccess()
    }

    @Test
    fun `isNotEmpty should fail when the string is empty`() {
        assertThat(isNotEmpty.isMetBy("")).isFailureWithViolations("string is empty")
    }

    @Test
    fun `isNullOrBlank should succeed when the string is null or blank`() {
        assertThat(isNullOrBlank.isMetBy(null)).isSuccess()
        assertThat(isBlank.isMetBy("    ")).isSuccess()
    }

    @Test
    fun `isNullOrBlank should fail when the string is not blank`() {
        assertThat(isNullOrBlank.isMetBy("not blank string")).isFailureWithViolations("string is not null nor blank")
    }

    @Test
    fun `isNullOrEmpty should succeed when the string is null or blank`() {
        assertThat(isNullOrEmpty.isMetBy(null)).isSuccess()
        assertThat(isNullOrEmpty.isMetBy("")).isSuccess()
    }

    @Test
    fun `isNullOrEmpty should fail when the string is not null and not empty`() {
        assertThat(isNullOrEmpty.isMetBy("not empty string")).isFailureWithViolations("string is not null nor empty")
    }
}

