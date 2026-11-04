package org.openindustries.governance.functions.autorisk;

import io.quarkus.funqy.Funq;

/**
 * Serverless function (Knative / OpenShift Serverless).
 * Scores automation risk signals (failures, privilege breadth, change rate) with an auditable rationale.
 * Transparent, auditable rules: NORMAL (< 35), WARNING (35-69), CRITICAL (>= 70);
 * the rationale is returned with the score for audit (ISO/IEC 27002 A.8.15).
 * Stateless by design (12-Factor VI): all state arrives in the request.
 */
public class Function {

    public record Event(String automationId, int failureIndex, int privilegeIndex, int changeIndex) {
    }

    public record Score(String automationId, int score, String decision, String rationale) {
    }

    @Funq
    public Score automationRiskScorer(Event event) {
        int score = 0;
        StringBuilder why = new StringBuilder();
        if (event.failureIndex() >= 70) {
            score += 40;
            why.append("failureIndex >= 70; ");
        }
        if (event.privilegeIndex() >= 60) {
            score += 30;
            why.append("privilegeIndex >= 60; ");
        }
        if (event.changeIndex() >= 50) {
            score += 30;
            why.append("changeIndex >= 50; ");
        }
        String decision = score >= 70 ? "CRITICAL" : score >= 35 ? "WARNING" : "NORMAL";
        return new Score(event.automationId(), score, decision,
                why.length() == 0 ? "no signals" : why.toString().trim());
    }
}
