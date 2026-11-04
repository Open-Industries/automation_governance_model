package org.openindustries.governance.functions.govcheck;

import io.quarkus.funqy.Funq;

import java.util.ArrayList;
import java.util.List;

/**
 * Serverless function (Knative / OpenShift Serverless).
 * Compares overdue reviews and evidence coverage against automation targets; one finding per breach.
 * Compares measured values with targets and returns a verdict with one finding per
 * breach — machine-checkable assurance evidence.
 */
public class Function {

    public record Kpi(String automationId, int measuredOverdueReviews, int targetOverdueReviews,
                      double measuredEvidenceCoveragePct, double targetEvidenceCoveragePct) {
    }

    public record Verdict(String automationId, boolean compliant, List<String> findings) {
    }

    @Funq
    public Verdict governanceComplianceEvaluator(Kpi kpi) {
        List<String> findings = new ArrayList<>();
        if (kpi.measuredOverdueReviews() > kpi.targetOverdueReviews()) {
            findings.add("overdueReviews " + kpi.measuredOverdueReviews() + " exceeds target " + kpi.targetOverdueReviews());
        }
        if (kpi.measuredEvidenceCoveragePct() < kpi.targetEvidenceCoveragePct()) {
            findings.add("evidenceCoveragePct " + kpi.measuredEvidenceCoveragePct() + " below target " + kpi.targetEvidenceCoveragePct());
        }
        return new Verdict(kpi.automationId(), findings.isEmpty(), findings);
    }
}
