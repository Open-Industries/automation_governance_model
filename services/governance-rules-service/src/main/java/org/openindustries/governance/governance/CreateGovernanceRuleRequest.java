package org.openindustries.governance.governance;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record CreateGovernanceRuleRequest(
        @NotBlank String name,
        @NotBlank @Pattern(regexp = "LOW|MEDIUM|HIGH|CRITICAL") String riskTier,
        @Positive long maxRunsPerMonth,
        @Positive int maxFailurePct,
        @Min(1) @Max(100) int priority) {
}
