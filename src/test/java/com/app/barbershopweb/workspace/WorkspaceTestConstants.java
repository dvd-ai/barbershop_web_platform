package com.app.barbershopweb.workspace;

import java.util.List;

public final class WorkspaceTestConstants {
    public final int WORKSPACE_FIELD_AMOUNT = 4;
    public final static String WORKSPACES_URL = "/workspaces";

    public final long INVALID_WORKSPACE_ID = -100L;

    public final long VALID_WORKSPACE_ID = 1L;
    public final long VALID_BARBERSHOP_ID = 1L;
    public final long VALID_USER_ID = 1L;
    public final boolean ACTIVE = true;

    public final long NOT_EXISTED_WORKSPACE_ID = 100_000L;


    public final Workspace VALID_WORKSPACE_ENTITY = new Workspace(
            VALID_WORKSPACE_ID, VALID_USER_ID,
            VALID_BARBERSHOP_ID, ACTIVE
    );

    public final Workspace VALID_UPDATED_WORKSPACE_ENTITY = new Workspace(
            VALID_WORKSPACE_ID, 2L,
            1L, ACTIVE
    );

    public final Workspace WORKSPACE_ENTITY_NOT_EXISTED_ID = new Workspace(
            NOT_EXISTED_WORKSPACE_ID, VALID_USER_ID,
            VALID_BARBERSHOP_ID, ACTIVE
    );

    public final WorkspaceDto VALID_WORKSPACE_DTO = new WorkspaceDto(
            VALID_WORKSPACE_ID, VALID_USER_ID,
            VALID_BARBERSHOP_ID, ACTIVE
    );

    public final WorkspaceDto INVALID_WORKSPACE_DTO = new WorkspaceDto(
            INVALID_WORKSPACE_ID, -100L,
            0L, ACTIVE
    );

    public final WorkspaceDto WORKSPACE_DTO_NOT_EXISTED_ID = new WorkspaceDto(
            NOT_EXISTED_WORKSPACE_ID, VALID_USER_ID,
            VALID_BARBERSHOP_ID, ACTIVE
    );

    public final WorkspaceDto VALID_UPDATED_WORKSPACE_DTO = new WorkspaceDto(
            VALID_WORKSPACE_ID, 1L,
            2L, ACTIVE
    );


    public final List<Workspace> VALID_WORKSPACE_ENTITY_LIST = List.of(
            new Workspace(
                    VALID_WORKSPACE_ID, VALID_USER_ID,
                    VALID_BARBERSHOP_ID, ACTIVE
            ),
            new Workspace(
                    VALID_WORKSPACE_ID + 1, VALID_USER_ID + 1,
                    VALID_BARBERSHOP_ID + 1, ACTIVE
            ),
            new Workspace(
                    VALID_WORKSPACE_ID + 2, VALID_USER_ID + 2,
                    VALID_BARBERSHOP_ID + 2, ACTIVE
            )
    );

    public final List<WorkspaceDto>VALID_WORKSPACE_DTO_LIST = List.of(
            new WorkspaceDto(
                    VALID_WORKSPACE_ENTITY_LIST.get(0).getWorkspaceId(), VALID_WORKSPACE_ENTITY_LIST.get(0).getUserId(),
                    VALID_WORKSPACE_ENTITY_LIST.get(0).getBarbershopId(), ACTIVE
            ),
            new WorkspaceDto(
                    VALID_WORKSPACE_ENTITY_LIST.get(1).getWorkspaceId(), VALID_WORKSPACE_ENTITY_LIST.get(1).getUserId(),
                    VALID_WORKSPACE_ENTITY_LIST.get(1).getBarbershopId(), ACTIVE
            ),
            new WorkspaceDto(
                    VALID_WORKSPACE_ENTITY_LIST.get(2).getWorkspaceId(), VALID_WORKSPACE_ENTITY_LIST.get(2).getUserId(),
                    VALID_WORKSPACE_ENTITY_LIST.get(2).getBarbershopId(), ACTIVE
            )
    );

    //ERROR MESSAGES:

    //CV means 'constraint violation'
    public final String DTO_CV_WORKSPACE_ID_ERR_MSG = "'workspaceDto.workspaceId' must be greater than or equal to 1";
    public final String DTO_CV_USER_ID_ERR_MSG = "'workspaceDto.userId' must be greater than or equal to 1";
    public final String DTO_CV_BARBERSHOP_ID_ERR_MSG = "'workspaceDto.barbershopId' must be greater than or equal to 1";

    //PV means 'path variable'
    public final String PV_WORKSPACE_ID_ERR_MSG = "'workspaceId' must be greater than or equal to 1";

    //fk, uk violation
    public final String DTO_FK_CV_USER_ID_ERR_MSG = "fk violation: user with id " +
            VALID_WORKSPACE_DTO.userId()  + " not present";

    public final String DTO_FK_CV_BARBERSHOP_ID_ERR_MSG = "fk violation: barbershop with id " +
            VALID_WORKSPACE_DTO.barbershopId()  + " not present";

    public final String DTO_UK_CV_ERR_MSG = "uk violation: workspace with user id " + VALID_WORKSPACE_DTO.userId() +
            " and barbershop id " + VALID_WORKSPACE_DTO.barbershopId() + " already exists.";


}
