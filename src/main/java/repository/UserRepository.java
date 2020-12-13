package repository;

import model.User;
import model.UserGender;
import model.UserType;
import org.apache.commons.codec.digest.DigestUtils;

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

        int phoneNumberToInt = Integer.parseInt(user.getPhoneNumber());
        String fakePhoneNumber = Integer.toString((phoneNumberToInt + 102030405));
        user.setPhoneNumber(fakePhoneNumber);

        String hashFromRealNumber = DigestUtils.sha256Hex(user.getPhoneNumber());

        PreparedStatement fakePhoneNumberStatement = connection.prepareStatement(
                "INSERT INTO `competence-schema`.`fake_phones` " +
                        "(fake_phone_number, real_phone_number) VALUES (?, ?)");
        fakePhoneNumberStatement.setString(1, fakePhoneNumber);
        fakePhoneNumberStatement.setString(2, hashFromRealNumber);
        boolean isFinished =  fakePhoneNumberStatement.executeUpdate() > 0;

        if (!isFinished) {
            return false;
        }

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO `competence-schema`.`persons` (id, phone_number, profile_name, experiment_id, user_gender, user_age) VALUES (?, ?, ?, ?, ?, ?)");
        statement.setString(1, user.getUserID().toString());
        statement.setString(2, fakePhoneNumber);
        statement.setString(3, user.getUserType().toString());
        statement.setString(4, user.getExperimentId());
        statement.setString(5, user.getUserGender().name());
        statement.setInt(6, user.getUserAge());
        isFinished =  statement.executeUpdate() > 0;

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

        String fakeNumber = getById(id).getPhoneNumber();

        PreparedStatement phoneStatement = connection.prepareStatement("DELETE FROM `competence-schema`.`fake_phones` WHERE fake_phone_number=?");
        phoneStatement.setString(1, fakeNumber);

        boolean isFinished =  phoneStatement.executeUpdate() > 0;

        if (!isFinished) {
            return false;
        }

        PreparedStatement statement = connection.prepareStatement("DELETE FROM `competence-schema`.`persons` WHERE id=?");
        statement.setString(1, id.toString());

        isFinished =  statement.executeUpdate() > 0;

        connection.close();
        return isFinished;
    }
}
