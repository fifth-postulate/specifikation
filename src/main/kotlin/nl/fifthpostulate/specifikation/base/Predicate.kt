package nl.fifthpostulate.specifikation.base

import nl.fifthpostulate.specifikation.Failure
import nl.fifthpostulate.specifikation.Report
import nl.fifthpostulate.specifikation.Specification
import nl.fifthpostulate.specifikation.Success

data class Predicate<T>(private val violation: String, private val predicate: (T) -> Boolean) :
    Specification<T> {
    override fun isMetBy(subject: T): Report {
        return if (predicate(subject)) {
            Success()
        } else {
            Failure(violation)
        }
    }
}

fun <T> ((T) -> Boolean).toSpecification(violation: String): Specification<T> =
    Predicate(violation, this)
