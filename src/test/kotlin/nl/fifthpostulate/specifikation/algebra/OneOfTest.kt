package nl.fifthpostulate.specifikation.algebra

import nl.fifthpostulate.specifikation.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.security.auth.Subject


class OneOfTest {
    lateinit var subject: Person

    @BeforeEach
    fun `create subject under test`() {
        subject = Person("test")
    }

    @Test
    fun `when all sub specifications succeed then one of succeeds`() {
        val succeed = Predicate<Person>("never occurs") { true }
        val specification = OneOf(succeed, succeed, succeed)

        val result = specification.verify(subject)

        assertThat(result is Success).isTrue()
    }

    @Test
    fun `when one sub specifications fails but other succeeds then one of succeeds`() {
        val succeed = Predicate<Person>("never occurs") { true }
        val fail = Predicate<Person>("violated") {false}
        val specification = OneOf(fail, fail, succeed, fail)

        val result = specification.verify(subject)

        assertThat(result is Success).isTrue()
    }

    @Test
    fun `when failing violations are collected`() {
        val succeed = Predicate<Person>("never occurs") { true }
        val fail1 = Predicate<Person>("first") { false }
        val fail2 = Predicate<Person>("second") { false }
        val specification = OneOf(fail1, fail2)

        val result = specification.verify(subject)

        assertThat(result is Failure).isTrue()
        assertThat((result as Failure).violations).containsExactly("first", "second")
    }
}
