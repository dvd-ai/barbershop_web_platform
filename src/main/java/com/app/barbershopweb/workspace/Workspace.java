package com.app.barbershopweb.workspace;

public class Workspace {

    private Long workspaceId;
    private Long userId;
    private Long barbershopId;
    private Boolean active;

    public Workspace() {
    }

    public Workspace(Long workspaceId, Long userId, Long barbershopId, Boolean active) {
        this.workspaceId = workspaceId;
        this.userId = userId;
        this.barbershopId = barbershopId;
        this.active = active;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBarbershopId() {
        return barbershopId;
    }

    public void setBarbershopId(Long barbershopId) {
        this.barbershopId = barbershopId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Workspace workspace = (Workspace) o;

        if (!workspaceId.equals(workspace.workspaceId)) return false;
        if (!userId.equals(workspace.userId)) return false;
        if (!barbershopId.equals(workspace.barbershopId)) return false;
        return active.equals(workspace.active);
    }

    @Override
    public int hashCode() {
        int result = workspaceId.hashCode();
        result = 31 * result + userId.hashCode();
        result = 31 * result + barbershopId.hashCode();
        result = 31 * result + active.hashCode();
        return result;
    }
}
