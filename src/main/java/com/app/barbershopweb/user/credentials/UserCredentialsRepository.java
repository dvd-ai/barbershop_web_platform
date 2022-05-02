package com.app.barbershopweb.user.credentials;

import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.user.crud.repository.UserRepository;
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

@Repository
public class UserCredentialsRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final UserRepository userRepository;

    public UserCredentialsRepository(DataSource dataSource, UserRepository userRepository) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.userRepository = userRepository;
    }

    public Long addUserCredentials(UserCredentials userCredentials) {
        checkFk(userCredentials.getUserId());
        checkUk(userCredentials.getUsername(), userCredentials.getUserId());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql =
                "INSERT INTO user_credentials(user_id, username, password, enabled) " +
                        "VALUES (" +
                        ":userId," +
                        ":username," +
                        ":password," +
                        ":enabled" +
                        ");";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("userId", userCredentials.getUserId())
                .addValue("username", userCredentials.getUsername())
                .addValue("password", userCredentials.getPassword())
                .addValue("enabled", userCredentials.getEnabled());

        namedParameterJdbcTemplate.update(sql, sqlParameterSource, keyHolder);
        return Long.valueOf((Integer) keyHolder.getKeys().get("user_id"));
    }

    public boolean credentialsExistByUsername(String username) {
        String sql =
                "SELECT COUNT(*) FROM user_credentials " +
                        "WHERE username = :username;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("username", username);

        Integer count = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Integer.class);
        return Objects.requireNonNull(count) > 0;
    }

    public boolean credentialsExistByUserId(Long userId) {
        String sql =
                "SELECT COUNT(*) FROM user_credentials " +
                        "WHERE user_id = :userId;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("userId", userId);

        Integer count = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Integer.class);
        return Objects.requireNonNull(count) > 0;
    }

    private void checkFk(Long id) {
        if (!userRepository.userExistsById(id)) {
            throw new NotFoundException(List.of("fk violation: user with id " + id + " not present"));
        }
    }

    private void checkUk(String username, Long userId) {
        List<String> messages = new ArrayList<>();
        String ukViolation = "uk violation: user credentials with ";
        String exist = " already exist";

        if (credentialsExistByUsername(username)) {
            messages.add(ukViolation + "username " + username + exist);
        }

        if (credentialsExistByUserId(userId)) {
            messages.add(ukViolation + "userId " + userId + exist);
        }

        if (!messages.isEmpty()) {
            throw new DbUniqueConstraintsViolationException(messages);
        }
    }

    public void truncateAndRestartSequence() {
        String sql = "TRUNCATE user_credentials RESTART IDENTITY CASCADE;";
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource());
    }
}
