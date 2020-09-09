package nl.fifthpostulate.specifikation.collection

import nl.fifthpostulate.specifikation.Report
import nl.fifthpostulate.specifikation.Specification
import nl.fifthpostulate.specifikation.Success

class All<T>(private val specification: Specification<T>) : Specification<Iterable<T>> {
    override fun isMetBy(subjects: Iterable<T>): Report {
        return subjects
            .map { subject -> specification.isMetBy(subject) }
            .mapIndexed { index, report -> report.mapFailure { violation -> "[$index] $violation" } }
            .reduce { acc, report -> acc.combine(report) }
    }
}

class OneOf<T>(private val specification: Specification<T>) : Specification<Iterable<T>> {
    override fun isMetBy(subjects: Iterable<T>): Report {
        val partition = subjects
            .map { subject -> specification.isMetBy(subject) }
            .mapIndexed { index, report -> report.mapFailure { violation -> "[$index] $violation" } }
            .partition { it is Success }

        return if (partition.first.isNotEmpty()) {
            partition.first.first()
        } else {
            partition.second.reduce { acc, report -> acc.combine(report) }
        }
    }
}
