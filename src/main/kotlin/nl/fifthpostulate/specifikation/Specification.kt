package nl.fifthpostulate.specifikation

interface Specification<Subject, Violation> {
    fun isMetBy(subject: Subject): Report<Violation>
}

sealed class Report<Violation> {
    abstract fun andThen(other: () -> Report<Violation>): Report<Violation>

    abstract fun <U> mapFailure(transform: (Violation) -> U): Report<U>
}
class Success<Violation> : Report<Violation>() {
    override fun andThen(other: () -> Report<Violation>): Report<Violation> {
        return other()
    }

    override fun <U> mapFailure(transform: (Violation) -> U): Report<U> {
        return this as Report<U>
    }
}

data class Failure<Violation>(val violations: Collection<Violation>): Report<Violation>() {
    constructor(vararg violations: Violation): this(violations.toList())
    override fun andThen(other: () -> Report<Violation>): Report<Violation> {
        val otherReport = other()
        return when (otherReport) {
            is Success -> this
            is Failure -> {
                val violations = mutableListOf<Violation>()
                violations.addAll(this.violations)
                violations.addAll(otherReport.violations)
                Failure(violations)
            }
        }
    }

    override fun <U> mapFailure(transform: (Violation) -> U): Report<U> {
        val mappedViolations = violations
            .map(transform)
        return Failure(mappedViolations)
    }
}
