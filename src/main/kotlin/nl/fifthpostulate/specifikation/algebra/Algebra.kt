package nl.fifthpostulate.specifikation.algebra

import nl.fifthpostulate.specifikation.*

data class Predicate<T>(private val violation: String, private val predicate: (T) -> Boolean) : Specification<T> {
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

class All<T>(private vararg val specifications: Specification<T>) : Specification<T> {
    override fun isMetBy(subject: T): Report {
        return specifications
            .map { it.isMetBy(subject) }
            .reduce { acc, report -> acc.combine(report) }
    }
}

class OneOf<T>(private vararg val specifications: Specification<T>) : Specification<T> {
    override fun isMetBy(subject: T): Report {
        val partition = specifications
            .map { it.isMetBy(subject) }
            .partition { it is Success }

        return if (partition.first.isNotEmpty()) {
            partition.first.first()
        } else {
            partition.second.reduce { acc, report -> acc.combine(report) }
        }
    }
}

data class Not<T>(private val specification: Specification<T>) : Specification<T> {
    override fun isMetBy(subject: T): Report {
        return when (specification.isMetBy(subject)) {
            is Success -> Failure(listOf("should not adhere to specification"))
            is Failure -> Success()
        }
    }
}

data class Fail<T>(private val violation: String) : Specification<T> {
    override fun isMetBy(subject: T): Report {
        return Failure(listOf(violation))
    }
}

class Succeed<T>(): Specification<T> {
    override fun isMetBy(subject: T): Report {
        return Success()
    }
}
