package repository;

import model.User;
import model.UserGender;
import model.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        User user = User.builder().userID(UUID.fromString(resultSet.getString("id")))
                .experimentId(resultSet.getString("experiment_id"))
                .phoneNumber(resultSet.getString("phone_number"))
                .userAge(resultSet.getInt("user_age")).userGender(UserGender.valueOf(resultSet.getString("user_gender")))
                .userType(UserType.valueOf(resultSet.getString("profile_name"))).build();

        connection.close();
        return user;
    }

    public List<User> getAll() throws SQLException {
        Connection connection = getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM `competence-schema`.`persons`");
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
            users.add(User.builder().userID(UUID.fromString(resultSet.getString("id")))
                    .experimentId(resultSet.getString("experiment_id"))
                    .phoneNumber(resultSet.getString("phone_number"))
                    .userAge(resultSet.getInt("user_age")).userGender(UserGender.valueOf(resultSet.getString("user_gender")))
                    .userType(UserType.valueOf(resultSet.getString("profile_name"))).build());
        }
        connection.close();
        return users;
    }

    public boolean save(User user) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO `competence-schema`.`persons` (id, phone_number, profile_name, experiment_id, user_gender, user_age) VALUES (?, ?, ?, ?, ?, ?)");
        statement.setString(1, user.getUserID().toString());
        statement.setString(2, user.getPhoneNumber());
        statement.setString(3, user.getUserType().toString());
        statement.setString(4, user.getExperimentId());
        statement.setString(5, user.getUserGender().name());
        statement.setInt(6, user.getUserAge());
        boolean isFinished =  statement.executeUpdate() > 0;
        connection.close();
        return isFinished;
    }

    public boolean updateById(User user) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE `competence-schema`.`persons` SET phone_number=?, profile_name=?, experiment_id=?, user_gender=?, user_age=? WHERE id=?");
        statement.setString(1, user.getPhoneNumber());
        statement.setString(2, user.getUserType().toString());
        statement.setString(3, user.getExperimentId());
        statement.setString(4, user.getUserGender().toString());
        statement.setInt(5, user.getUserAge());
        statement.setString(6, user.getUserID().toString());

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
