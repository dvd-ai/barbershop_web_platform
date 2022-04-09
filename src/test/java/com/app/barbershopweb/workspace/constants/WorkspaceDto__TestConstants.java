package com.app.barbershopweb.workspace.constants;

import com.app.barbershopweb.workspace.WorkspaceDto;

import static com.app.barbershopweb.barbershop.constants.BarbershopMetadata__TestConstants.BARBERSHOP_VALID_BARBERSHOP_ID;
import static com.app.barbershopweb.user.constants.UserMetadata__TestConstants.USERS_VALID_USER_ID;
import static com.app.barbershopweb.workspace.constants.WorkspaceMetadata__TestConstants.*;

public final class WorkspaceDto__TestConstants {
    public static final WorkspaceDto WORKSPACE_VALID_DTO = new WorkspaceDto(
            WORKSPACE_VALID_WORKSPACE_ID, USERS_VALID_USER_ID,
            BARBERSHOP_VALID_BARBERSHOP_ID, WORKSPACE_ACTIVE
    );

    public static final WorkspaceDto WORKSPACE_INVALID_DTO = new WorkspaceDto(
            WORKSPACE_INVALID_WORKSPACE_ID, -100L,
            0L, WORKSPACE_ACTIVE
    );

    public static final WorkspaceDto WORKSPACE_NOT_EXISTED_ID_DTO = new WorkspaceDto(
            WORKSPACE_NOT_EXISTED_WORKSPACE_ID, USERS_VALID_USER_ID,
            BARBERSHOP_VALID_BARBERSHOP_ID, WORKSPACE_ACTIVE
    );

    public static final WorkspaceDto WORKSPACE_VALID_UPDATED_DTO = new WorkspaceDto(
            WORKSPACE_VALID_WORKSPACE_ID, 1L,
            2L, WORKSPACE_ACTIVE
    );
}
