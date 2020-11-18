package nl.fifthpostulate.specifikation.assertions

import nl.fifthpostulate.specifikation.*
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.IterableAssert

class ReportAssert<Violation>(report: Report<Violation>): AbstractAssert<ReportAssert<Violation>, Report<Violation>>(report, ReportAssert::class.java) {
    fun isSuccess(): ReportAssert<Violation>{
        if (actual !is Success) {
            failWithMessage("expected report to be a Success but it was not")
        }
        return this
    }

    fun isFailure(): ReportAssert<Violation>{
        if (actual !is Failure) {
            failWithMessage("expected report to be a Failure but it was not")
        }
        return this
    }

    fun isFailureWithViolations(vararg expectedViolations: Violation): ReportAssert<Violation> {
        if (actual !is Failure) {
            failWithMessage("Success does not have violations")
        }
        IterableAssert((actual as Failure).violations).containsExactly(*expectedViolations)
        return this
    }

    companion object {
        @JvmStatic
        fun <V> assertThat(report: Report<V>): ReportAssert<V> {
            return ReportAssert(report)
        }
    }
}
