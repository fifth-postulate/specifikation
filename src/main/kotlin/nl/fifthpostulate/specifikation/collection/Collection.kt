package nl.fifthpostulate.specifikation.collection

import nl.fifthpostulate.specifikation.Report
import nl.fifthpostulate.specifikation.Specification
import nl.fifthpostulate.specifikation.Success

class All<T, V>(private val specification: Specification<T, V>) : Specification<Iterable<T>, Pair<Int, V>> {
    override fun isMetBy(subjects: Iterable<T>): Report<Pair<Int, V>> {
        return subjects
            .map { subject -> specification.isMetBy(subject) }
            .mapIndexed { index, report -> report.mapFailure { violation -> Pair(index, violation) } }
            .reduce { acc, report -> acc.andThen { report }}
    }
}

class OneOf<T, V>(private val specification: Specification<T, V>) : Specification<Iterable<T>, Pair<Int, V>> {
    override fun isMetBy(subjects: Iterable<T>): Report<Pair<Int, V>> {
        val partition = subjects
            .map { subject -> specification.isMetBy(subject) }
            .mapIndexed { index, report -> report.mapFailure { violation -> Pair(index, violation) } }
            .partition { it is Success }

        return if (partition.first.isNotEmpty()) {
            partition.first.first()
        } else {
            partition.second.reduce { acc, report -> acc.andThen {report} }
        }
    }
}
