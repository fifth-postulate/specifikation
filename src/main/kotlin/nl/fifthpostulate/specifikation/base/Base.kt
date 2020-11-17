package nl.fifthpostulate.specifikation.base

import nl.fifthpostulate.specifikation.Failure
import nl.fifthpostulate.specifikation.Report
import nl.fifthpostulate.specifikation.Specification
import nl.fifthpostulate.specifikation.Success

data class Predicate<T, V>(private val violation: V, private val predicate: (T) -> Boolean) :
    Specification<T, V> {
    override fun isMetBy(subject: T): Report<V> {
        return if (predicate(subject)) {
            Success()
        } else {
            Failure(violation)
        }
    }
}

fun <T, V> ((T) -> Boolean).toSpecification(violation: V): Specification<T, V> =
    Predicate(violation, this)

data class View<S,V,T,W>(val specification: Specification<T, W>, val violationTransform: (S, T, W) -> V, val memberOf: (S) -> T): Specification<S, V> {
    constructor(specification: Specification<T, W>, violationTransform: (T, W) -> V, memberOf: (S) -> T): this(specification, {_, member, violation -> violationTransform(member, violation)}, memberOf)
    constructor(specification: Specification<T, W>, violationTransform: (W) -> V, memberOf: (S) -> T): this(specification, {_, _, violation -> violationTransform(violation)}, memberOf)
    constructor(specification: Specification<T, W>, violationTransform: () -> V, memberOf: (S) -> T): this(specification, {_, _, _ -> violationTransform()}, memberOf)
    override fun isMetBy(subject: S): Report<V> {
        val member = memberOf(subject)
        return specification
            .isMetBy(member)
            .mapFailure {violation -> violationTransform(subject, member, violation)}
    }
}
