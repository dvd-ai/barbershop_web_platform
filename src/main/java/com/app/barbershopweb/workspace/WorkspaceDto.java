package com.app.barbershopweb.workspace;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public record WorkspaceDto(
        @Min(1)
        Long workspaceId,
        @Min(1)
        Long userId,
        @Min(1)
        Long barbershopId,
        @NotNull
        Boolean active
) {
}
