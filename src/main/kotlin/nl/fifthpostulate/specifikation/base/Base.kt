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

data class View<S,T>(val specification: Specification<T>, val violationTransform: (String) -> String, val memberOf: (S) -> T): Specification<S> {
    constructor(specification: Specification<T>, prefix: String, memberOf: (S) -> T): this(specification, {violation -> "$prefix$violation" }, memberOf)
    override fun isMetBy(subject: S): Report {
        return specification
            .isMetBy(memberOf(subject))
            .mapFailure(violationTransform)
    }

}
