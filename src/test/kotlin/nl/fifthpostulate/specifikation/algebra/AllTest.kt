package nl.fifthpostulate.specifikation.algebra

import nl.fifthpostulate.specifikation.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.security.auth.Subject


class AllTest {
    lateinit var subject: Person

    @BeforeEach
    fun `create subject under test`() {
        subject = Person("test")
    }

    @Test
    fun `when all sub specifications succeed then all succeeds`() {
        val succeed = Predicate<Person>("never occurs") { true }
        val specification = All(succeed, succeed, succeed)

        val result = specification.verify(subject)

        assertThat(result is Success).isTrue()
    }

    @Test
    fun `when one sub specifications fails then all fail`() {
        val succeed = Predicate<Person>("never occurs") { true }
        val fail = Predicate<Person>("violated") {false}
        val specification = All(succeed, succeed, succeed, fail)

        val result = specification.verify(subject)

        assertThat(result is Failure).isTrue()
    }

    @Test
    fun `when failing violations are collected`() {
        val succeed = Predicate<Person>("never occurs") { true }
        val fail1 = Predicate<Person>("first") { false }
        val fail2 = Predicate<Person>("second") { false }
        val specification = All(succeed, fail1, succeed, succeed, fail2)

        val result = specification.verify(subject)

        assertThat(result is Failure).isTrue()
        assertThat((result as Failure).violations).containsExactly("first", "second")
    }
}
