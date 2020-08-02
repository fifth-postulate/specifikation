package nl.fifthpostulate.specifikation

interface Specification<T> {
    fun verify(subject: T): Report
}

sealed class Report
class Success : Report()
data class Failure(val violations: Collection<String>): Report()
