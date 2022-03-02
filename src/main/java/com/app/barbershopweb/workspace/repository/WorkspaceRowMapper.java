package com.app.barbershopweb.workspace.repository;

import com.app.barbershopweb.workspace.Workspace;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkspaceRowMapper implements RowMapper<Workspace> {
    @Override
    public Workspace mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Workspace(
                rs.getLong("workspace_id"),
                rs.getLong("barbershop_id"),
                rs.getLong("user_id"),
                rs.getBoolean("active")
        );
    }
}
