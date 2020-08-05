package nl.fifthpostulate.specifikation

interface Specification<T> {
    fun isMetBy(subject: T): Report
}

sealed class Report {
    abstract fun combine(other: Report): Report
}
class Success : Report() {
    override fun combine(other: Report): Report {
        return other
    }
}

data class Failure(val violations: Collection<String>): Report() {
    constructor(vararg violations: String): this(violations.toList())
    override fun combine(other: Report): Report {
        return when (other) {
            is Success -> this
            is Failure -> {
                val violations = mutableListOf<String>()
                violations.addAll(this.violations)
                violations.addAll(other.violations)
                Failure(violations)
            }
        }
    }
}
