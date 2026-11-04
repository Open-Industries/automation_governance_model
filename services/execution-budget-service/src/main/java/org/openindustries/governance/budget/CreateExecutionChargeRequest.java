package org.openindustries.governance.budget;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record CreateExecutionChargeRequest(
        @NotBlank @Pattern(regexp = "SCHEDULED|EVENT|MANUAL|RETRY") String runType,
        @Positive long runs) {
}
