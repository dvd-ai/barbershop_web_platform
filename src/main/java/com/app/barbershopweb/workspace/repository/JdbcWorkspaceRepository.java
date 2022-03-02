package com.app.barbershopweb.workspace.repository;

import com.app.barbershopweb.workspace.Workspace;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcWorkspaceRepository implements WorkspaceRepository{

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcWorkspaceRepository(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Long addWorkspace(Workspace workspace) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql =
                "INSERT INTO workspace(barbershop_id, user_id, active) " +
                        "VALUES (" +
                        ":barbershop_id," +
                        ":user_id," +
                        ":active" +
                        ");";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("barbershop_id", workspace.getBarbershopId())
                .addValue("user_id", workspace.getUserId())
                .addValue("active", workspace.getActive());

        namedParameterJdbcTemplate.update(sql, sqlParameterSource, keyHolder);
        return Long.valueOf((Integer) keyHolder.getKeys().get("workspace_id"));
    }

    @Override
    public Optional<Workspace> findWorkspaceById(Long id) {
        Optional<Workspace> workspaceOptional;

        String sql =
                "SELECT workspace_id, barbershop_id, user_id, active " +
                        "FROM workspace " +
                        "WHERE workspace_id = :id;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        try {
            workspaceOptional = Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, new WorkspaceRowMapper())
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

        return workspaceOptional;
    }

    @Override
    public Optional<Workspace>updateWorkspace(Workspace workspace) {
        String sql =
                "UPDATE workspace " +
                        "SET " +
                        "barbershop_id = :barbershop_id, " +
                        "user_id = :user_id, " +
                        "active = :active, " +
                        "WHERE workspace_id = :id;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("barbershop_id", workspace.getBarbershopId())
                .addValue("user_id", workspace.getUserId())
                .addValue("active", workspace.getActive())
                .addValue(  "id", workspace.getWorkspaceId());


        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
        return findWorkspaceById(workspace.getWorkspaceId());
    }

    @Override
    public List<Workspace>getWorkspaces() {
        String sql =
                "SELECT " +
                        "workspace_id, " +
                        "barbershop_id, " +
                        "user_id, " +
                        "active " +
                        "FROM workspace;";


        return namedParameterJdbcTemplate.query(sql, new WorkspaceRowMapper());
    }

    @Override
    public void deleteWorkspaceById(Long id) {
        String sql = "DELETE FROM workspace " +
                "WHERE workspace_id = :id;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }
}
