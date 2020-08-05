package nl.fifthpostulate.specifikation.algebra

import nl.fifthpostulate.specifikation.*

data class Predicate<T>(val violation: String, val predicate: (T) -> Boolean) : Specification<T> {
    override fun isMetBy(subject: T): Report {
        return if (predicate(subject)) {
            Success()
        } else {
            Failure(listOf(violation))
        }
    }
}

fun <T> ((T) -> Boolean).toSpecification(violation: String): Specification<T> =
    Predicate(violation, this)

class All<T>(vararg val specifications: Specification<T>) : Specification<T> {
    override fun isMetBy(subject: T): Report {
        return specifications.map { it.isMetBy(subject) }
            .reduce { acc, report -> acc.combine(report) }
    }
}

class OneOf<T>(vararg val specifications: Specification<T>) : Specification<T> {
    override fun isMetBy(subject: T): Report {
        val partition = specifications.map { it.isMetBy(subject) }
            .partition { it is Success }

        return if (partition.first.isNotEmpty()) {
            partition.first.first()
        } else {
            partition.second.reduce { acc, report -> acc.combine(report) }
        }
    }
}

class Not<T>(val specification: Specification<T>) : Specification<T> {
    override fun isMetBy(subject: T): Report {
        return when (specification.isMetBy(subject)) {
            is Success -> Failure(listOf("should not adhere to specification"))
            is Failure -> Success()
        }
    }
}

data class Fail<T>(val violation: String) : Specification<T> {
    override fun isMetBy(subject: T): Report {
        return Failure(listOf(violation))
    }
}

class Succeed<T>(): Specification<T> {
    override fun isMetBy(subject: T): Report {
        return Success()
    }
}
