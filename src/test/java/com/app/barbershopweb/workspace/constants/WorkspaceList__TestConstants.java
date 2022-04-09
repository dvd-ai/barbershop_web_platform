package com.app.barbershopweb.workspace.constants;

import com.app.barbershopweb.workspace.Workspace;
import com.app.barbershopweb.workspace.WorkspaceDto;

import java.util.List;

import static com.app.barbershopweb.barbershop.constants.BarbershopMetadata__TestConstants.BARBERSHOP_VALID_BARBERSHOP_ID;
import static com.app.barbershopweb.user.constants.UserMetadata__TestConstants.USERS_VALID_USER_ID;
import static com.app.barbershopweb.workspace.constants.WorkspaceMetadata__TestConstants.WORKSPACE_ACTIVE;
import static com.app.barbershopweb.workspace.constants.WorkspaceMetadata__TestConstants.WORKSPACE_VALID_WORKSPACE_ID;

public final class WorkspaceList__TestConstants {

    public static final List<Workspace> WORKSPACE_VALID_ENTITY_LIST = List.of(
            new Workspace(
                    WORKSPACE_VALID_WORKSPACE_ID, USERS_VALID_USER_ID,
                    BARBERSHOP_VALID_BARBERSHOP_ID, WORKSPACE_ACTIVE
            ),
            new Workspace(
                    WORKSPACE_VALID_WORKSPACE_ID + 1, USERS_VALID_USER_ID + 1,
                    BARBERSHOP_VALID_BARBERSHOP_ID + 1, WORKSPACE_ACTIVE
            ),
            new Workspace(
                    WORKSPACE_VALID_WORKSPACE_ID + 2, USERS_VALID_USER_ID + 2,
                    BARBERSHOP_VALID_BARBERSHOP_ID + 2, WORKSPACE_ACTIVE
            )
    );

    public static final List<WorkspaceDto> WORKSPACE_VALID_DTO_LIST = List.of(
            new WorkspaceDto(
                    WORKSPACE_VALID_ENTITY_LIST.get(0).getWorkspaceId(), WORKSPACE_VALID_ENTITY_LIST.get(0).getUserId(),
                    WORKSPACE_VALID_ENTITY_LIST.get(0).getBarbershopId(), WORKSPACE_ACTIVE
            ),
            new WorkspaceDto(
                    WORKSPACE_VALID_ENTITY_LIST.get(1).getWorkspaceId(), WORKSPACE_VALID_ENTITY_LIST.get(1).getUserId(),
                    WORKSPACE_VALID_ENTITY_LIST.get(1).getBarbershopId(), WORKSPACE_ACTIVE
            ),
            new WorkspaceDto(
                    WORKSPACE_VALID_ENTITY_LIST.get(2).getWorkspaceId(), WORKSPACE_VALID_ENTITY_LIST.get(2).getUserId(),
                    WORKSPACE_VALID_ENTITY_LIST.get(2).getBarbershopId(), WORKSPACE_ACTIVE
            )
    );

}
