package repository;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static dbconnector.JavaDatabaseConnector.getConnection;

public class UserRepository {


    public User getById(UUID id) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM `competence-schema`.`persons` WHERE id=?");
        statement.setString(1, id.toString());
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        User user = new User(UUID.fromString(resultSet.getString("id")),
                resultSet.getString("phone_number"), 21,
                UserType.valueOf(resultSet.getString("profile_name")), UserGender.female);

        connection.close();
        return user;
    }

    public List<User> getAll() throws SQLException {
        Connection connection = getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM `competence-schema`.`persons`");
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
            users.add(new User( UUID.fromString(resultSet.getString("id")),
                    resultSet.getString("phone_number"), 21,
                    UserType.valueOf(resultSet.getString("profile_name")), UserGender.female));
        }
        connection.close();
        return users;
    }

    public boolean save(User user) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO `competence-schema`.`persons` (id, phone_number, profile_name, experiment_id) VALUES (?, ?, ?, ?)");
        statement.setString(1, user.getUserID().toString());
        statement.setString(2, user.getPhoneNumber());
        statement.setString(3, user.getUserType().toString());
        statement.setLong(4, 1);

        boolean isFinished =  statement.executeUpdate() > 0;
        connection.close();
        return isFinished;
    }

    public boolean updateById(User user) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE `competence-schema`.`persons` SET phone_number=?, profile_name=?, experiment_id=? WHERE id=?");
        statement.setString(1, user.getPhoneNumber());
        statement.setString(2, user.getUserType().toString());
        statement.setLong(3, 1);
        statement.setString(4, user.getUserID().toString());

        boolean isFinished =  statement.executeUpdate() > 0;
        connection.close();
        return isFinished;
    }

    public boolean delete(UUID id) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM `competence-schema`.`persons` WHERE id=?");
        statement.setString(1, id.toString());

        boolean isFinished =  statement.executeUpdate() > 0;
        connection.close();
        return isFinished;
    }
}
