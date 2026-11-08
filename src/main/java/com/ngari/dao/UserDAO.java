
package com.ngari.dao;

import com.ngari.database.DBConnection;
import com.ngari.model.User;
import com.ngari.security.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    public User authenticate(String username, String password)
            throws Exception {

        String sql = """
                SELECT id, username, password_hash
                FROM users
                WHERE username = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            // Supply the username to the query
            statement.setString(1, username);

            // Execute the query and retrieve the result
            try (ResultSet resultSet = statement.executeQuery()) {

                // Check whether a matching username exists
                if (resultSet.next()) {

                    int id = resultSet.getInt("id");
                    String storedUsername =
                            resultSet.getString("username");

                    String storedHash =
                            resultSet.getString("password_hash");

                    // Verify the submitted password
                    boolean passwordMatches =
                            PasswordUtil.verifyPassword(
                                    password,
                                    storedHash
                            );

                    if (passwordMatches) {
                        return new User(id, storedUsername);
                    }
                }
            }
        }

        // No matching user or incorrect password
        return null;
    }
}
