package org.openindustries.governance.control;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record CreateControlItemRequest(
        @NotBlank String name,
        @NotBlank @Pattern(regexp = "ACCESS|CHANGE|MONITORING|APPROVAL") String controlType,
        @Positive int reviewCycleDays) {
}
