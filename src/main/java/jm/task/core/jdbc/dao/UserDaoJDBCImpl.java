package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private static Connection connection = Util.getConnection();

    private static final String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS myDbTest.users (" +
            "id BIGINT NOT NULL AUTO_INCREMENT, " +
            "name VARCHAR(45) NULL, " +
            "lastName VARCHAR(45) NULL, " +
            "age TINYINT NULL, " +
            "UNIQUE INDEX id_UNIQUE (id ASC));";
    private static final String DROP_TABLE_USERS = "DROP TABLE IF EXISTS myDbTest.users;";
    private static final String SAVE_USER = "INSERT INTO myDbTest.users (name, lastName, age) VALUES (?, ?, ?);";
    private static final String REMOVE_USER_BY_ID = "DELETE FROM myDbTest.users WHERE id = ?";
    private static final String GET_ALL_USERS = "SELECT * FROM myDbTest.users;";
    private static final String CLEAN_TABLE_USERS = "DELETE FROM myDbTest.users;";

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_TABLE_USERS);
            System.out.println("Table created or already exists.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(DROP_TABLE_USERS);
            System.out.println("Table dropped.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_USER)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.executeUpdate();
            System.out.printf("User с именем %s добавлен в базу данных \n", name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_USER_BY_ID)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            System.out.printf("User with id = %s was removed from database\n", id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL_USERS);
            while (resultSet.next()) {
                Long id = (long) resultSet.getInt("id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("lastName");
                Byte age = (byte) resultSet.getInt("age");
                User user = new User(name, lastName, age);
                user.setId(id);
                userList.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CLEAN_TABLE_USERS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
