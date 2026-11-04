package org.openindustries.governance.automation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record CreateAutomationRequest(
        @NotBlank String name,
        @NotBlank @Pattern(regexp = "WORKFLOW|BOT|INTEGRATION|REPORT") String automationType,
        @NotBlank @Pattern(regexp = "LOW|MEDIUM|HIGH") String riskTier) {
}
