package com.app.barbershopweb.workspace.constants.error;

import static com.app.barbershopweb.workspace.constants.WorkspaceDto__TestConstants.WORKSPACE_VALID_DTO;

public class WorkspaceErrorMessage_Fk__TestConstants {
    public static final String WORKSPACE_ERR_FK_USER_ID = "fk violation: user with id " +
            WORKSPACE_VALID_DTO.userId() + " not present";

    public static final String WORKSPACE_ERR_FK_BARBERSHOP_ID = "fk violation: barbershop with id " +
            WORKSPACE_VALID_DTO.barbershopId() + " not present";

}
