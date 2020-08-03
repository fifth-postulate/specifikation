package nl.fifthpostulate.specifikation.algebra

import nl.fifthpostulate.specifikation.*

data class Predicate<T>(val violation: String, val predicate: (T) -> Boolean) : Specification<T> {
    override fun verify(subject: T): Report {
        return if (predicate(subject)) {
            Success()
        } else {
            Failure(listOf(violation))
        }
    }
}

fun <T> ((T) -> Boolean).toSpecification(violation: String): Specification<T> =
    Predicate(violation, this)

class All<T>(vararg val specifications: Specification<T>): Specification<T> {
    override fun verify(subject: T): Report {
        return specifications.map {it.verify(subject)}
            .reduce { acc, report -> acc.combine(report) }
    }
}
