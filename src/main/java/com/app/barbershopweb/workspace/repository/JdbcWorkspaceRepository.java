package com.app.barbershopweb.workspace.repository;

import com.app.barbershopweb.barbershop.crud.repository.BarbershopRepository;
import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.user.crud.repository.UserRepository;
import com.app.barbershopweb.workspace.Workspace;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcWorkspaceRepository implements WorkspaceRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final UserRepository userRepository;
    private final BarbershopRepository barbershopRepository;

    public JdbcWorkspaceRepository(DataSource dataSource, UserRepository userRepository,
                                   BarbershopRepository barbershopRepository) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.userRepository = userRepository;
        this.barbershopRepository = barbershopRepository;
    }

    @Override
    public Long addWorkspace(Workspace workspace) {
        checkFkConstraints(workspace.getBarbershopId(), workspace.getUserId());
        checkUkConstraints(workspace.getBarbershopId(), workspace.getUserId());

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
    public boolean workspaceExistsByBarbershopIdAndUserId(Long barbershopId, Long userId) {
        String sql =
                "SELECT COUNT(*) " +
                        "FROM workspace " +
                        "WHERE barbershop_id = :barbershopId " +
                        "AND user_id = :userId";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("barbershopId", barbershopId)
                .addValue("userId", userId);

        Integer count = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Integer.class);
        return Objects.requireNonNull(count) > 0;
    }

    @Override
    public boolean workspaceIsActiveByBarbershopIdAndUserId(Long barbershopId, Long userId) {
        String sql =
                "SELECT COUNT(*) " +
                        "FROM workspace " +
                        "WHERE barbershop_id = :barbershopId " +
                        "AND user_id = :userId " +
                        "AND active = true";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("barbershopId", barbershopId)
                .addValue("userId", userId);

        Integer count = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Integer.class);
        return Objects.requireNonNull(count) > 0;
    }

    @Override
    public Optional<Workspace> updateWorkspace(Workspace workspace) {
        checkFkConstraints(workspace.getBarbershopId(), workspace.getUserId());
        checkUkConstraints(workspace.getBarbershopId(), workspace.getUserId());

        String sql =
                "UPDATE workspace " +
                        "SET " +
                        "barbershop_id = :barbershop_id, " +
                        "user_id = :user_id, " +
                        "active = :active " +
                        "WHERE workspace_id = :id;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("barbershop_id", workspace.getBarbershopId())
                .addValue("user_id", workspace.getUserId())
                .addValue("active", workspace.getActive())
                .addValue("id", workspace.getWorkspaceId());


        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
        return findWorkspaceById(workspace.getWorkspaceId());
    }

    @Override
    public List<Workspace> getWorkspaces() {
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

    @Override
    public void deactivateWorkspacesByBarbershopId(Long barbershopId) {
        String sql =
                "UPDATE workspace " +
                        "SET " +
                        "active = false " +
                        "WHERE barbershop_id = :barbershopId;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("barbershopId", barbershopId);

        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    @Override
    public void truncateAndRestartSequence() {
        String sql = "TRUNCATE workspace RESTART IDENTITY;";
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource());
    }

    private void checkFkConstraints(Long barbershopId, Long userId) {
        String fkViolation = "fk violation: ";
        String notPresent = " not present";
        List<String> messages = new ArrayList<>();


        if (!userRepository.userExistsById(userId)) {
            messages.add(fkViolation + "user with id " + userId + notPresent);
        }

        if (!barbershopRepository.barbershopExistsById(barbershopId)) {
            messages.add(fkViolation + "barbershop with id " + barbershopId + notPresent);
        }

        if (!messages.isEmpty()) {
            throw new NotFoundException(messages);
        }
    }

    private void checkUkConstraints(Long barbershopId, Long userId) {
        String sql =
                "SELECT count(*) " +
                        "FROM workspace " +
                        "WHERE barbershop_id = :barbershop_id " +
                        "AND " +
                        "user_id = :user_id;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("barbershop_id", barbershopId)
                .addValue("user_id", userId);

        Integer count = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Integer.class);

        if (Objects.requireNonNull(count) > 0) {
            throw new DbUniqueConstraintsViolationException(List.of(
                    "uk violation: " +
                            "workspace with user id " + userId +
                            " and barbershop id " + barbershopId +
                            " already exists."
            )
            );
        }
    }
}
