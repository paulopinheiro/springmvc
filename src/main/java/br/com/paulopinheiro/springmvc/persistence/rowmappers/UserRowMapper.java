package br.com.paulopinheiro.springmvc.persistence.rowmappers;

import br.com.paulopinheiro.springmvc.persistence.entities.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();

        user.setId(rs.getLong("id"));
        user.setEmail(rs.getString("email"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEnabled(rs.getBoolean("enabled"));

        return user;
    }
}
