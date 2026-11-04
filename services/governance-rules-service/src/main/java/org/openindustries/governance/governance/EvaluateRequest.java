package org.openindustries.governance.governance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record EvaluateRequest(
        @NotBlank @Pattern(regexp = "LOW|MEDIUM|HIGH|CRITICAL") String riskTier,
        @Positive int requiredMaxFailurePct,
        @Positive long requiredMaxRunsPerMonth) {
}
