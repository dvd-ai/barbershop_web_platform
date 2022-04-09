package com.app.barbershopweb.workspace.constants;

import com.app.barbershopweb.workspace.Workspace;

import static com.app.barbershopweb.barbershop.constants.BarbershopMetadata__TestConstants.BARBERSHOP_VALID_BARBERSHOP_ID;
import static com.app.barbershopweb.user.constants.UserMetadata__TestConstants.USERS_VALID_USER_ID;
import static com.app.barbershopweb.workspace.constants.WorkspaceMetadata__TestConstants.*;

public final class WorkspaceEntity__TestConstants {

    public static final Workspace WORKSPACE_VALID_ENTITY = new Workspace(
            WORKSPACE_VALID_WORKSPACE_ID, USERS_VALID_USER_ID,
            BARBERSHOP_VALID_BARBERSHOP_ID, WORKSPACE_ACTIVE
    );

    public static final Workspace WORKSPACE_VALID_UPDATED_ENTITY = new Workspace(
            WORKSPACE_VALID_WORKSPACE_ID, 2L,
            1L, WORKSPACE_ACTIVE
    );

    public static final Workspace WORKSPACE_NOT_EXISTED_ID_ENTITY = new Workspace(
            WORKSPACE_NOT_EXISTED_WORKSPACE_ID, USERS_VALID_USER_ID,
            BARBERSHOP_VALID_BARBERSHOP_ID, WORKSPACE_ACTIVE
    );

}
