package nl.fifthpostulate.specifikation.algebra

import nl.fifthpostulate.specifikation.*

class All<T, V>(private vararg val specifications: Specification<T, V>) : Specification<T, V> {
    override fun isMetBy(subject: T): Report<V> {
        return specifications
            .map { it.isMetBy(subject) }
            .reduce { acc, report -> acc.andThen {report} }
    }
}

class OneOf<T, V>(private vararg val specifications: Specification<T, V>) : Specification<T, V> {
    override fun isMetBy(subject: T): Report<V> {
        val partition = specifications
            .map { it.isMetBy(subject) }
            .partition { it is Success }

        return if (partition.first.isNotEmpty()) {
            partition.first.first()
        } else {
            partition.second.reduce { acc, report -> acc.andThen {report} }
        }
    }
}

data class Not<T, V>(private val specification: Specification<T, V>) : Specification<T, V> {
    override fun isMetBy(subject: T): Report<V> {
        return when (specification.isMetBy(subject)) {
            is Success -> Failure(listOf()) // TODO message kind of failure
            is Failure -> Success()
        }
    }
}

data class Fail<T, V>(private val violation: V) : Specification<T, V> {
    override fun isMetBy(subject: T): Report<V> {
        return Failure(listOf(violation))
    }
}

class Succeed<T, V>(): Specification<T, V> {
    override fun isMetBy(subject: T): Report<V> {
        return Success()
    }
}
