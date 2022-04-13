package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private static final RowMapper<User> USER_ROW_MAPPER = new UserRowMapper();

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if(violations.size() > 0) throw new ConstraintViolationException(violations);

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) != 0) {
            deleteUserRoles(user.id());
        } else {
            return null;
        }
        userRoleBatchUpdate(user.getRoles(), user.id());
        return user;
    }

    @Override
    public boolean delete(int id) {
        deleteUserRoles(id);
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    private void deleteUserRoles(int user_id) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user_id);
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT u.*, array_agg(ur.role) as roles FROM users u " +
                "LEFT JOIN user_roles ur ON u.id = ur.user_id WHERE u.id=? GROUP BY u.id", USER_ROW_MAPPER, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT u.*, array_agg(ur.role) as roles FROM users u " +
                "LEFT JOIN user_roles ur ON u.id = ur.user_id WHERE u.email=? GROUP BY u.id", USER_ROW_MAPPER, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
         var i = jdbcTemplate.query("SELECT u.*, array_agg(ur.role) as roles FROM users u " +
                "LEFT JOIN user_roles ur ON u.id = ur.user_id GROUP BY u.id, u.name, u.email ORDER BY u.name, u.email",
                USER_ROW_MAPPER);
        return i;
    }

    private static class UserRowMapper implements RowMapper<User> {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setCaloriesPerDay(rs.getInt("calories_per_day"));
            user.setEnabled(rs.getBoolean("enabled"));
            user.setRegistered(rs.getDate("registered"));
            user.setRoles(Arrays.stream((String[]) rs.getArray("roles").getArray())
                    .filter(Objects::nonNull)
                    .map(Role::valueOf)
                    .collect(Collectors.toList()));
            return user;
        }
    }

    private int[] userRoleBatchUpdate(Set<Role> roles, int user_id) {
        return this.jdbcTemplate.batchUpdate(
                "update user_roles set role = ? where user_id = ?",
                new BatchPreparedStatementSetter() {

                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        ps.setString(1, roles.toArray()[i].toString());
                        ps.setInt(2, user_id);
                    }

                    public int getBatchSize() {
                        return roles.size();
                    }
                }
        );
    }
}
