package org.openindustries.governance.budget;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record OpenExecutionBudgetRequest(
        @NotNull java.util.UUID automationId,
        @PositiveOrZero long monthlyRuns) {
}
