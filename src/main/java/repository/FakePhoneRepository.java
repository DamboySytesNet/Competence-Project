package repository;

import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static connectors.JavaDatabaseConnector.getConnection;

public class FakePhoneRepository {

    private static String calcFakePhoneNumber(String realNumber) {
        int phoneNumberToInt = Integer.parseInt(realNumber);
        return Integer.toString((phoneNumberToInt + 102030405));
    }

    public static String saveFromRealPhoneNumber(String realPhoneNumber) throws SQLException {
        Connection connection = getConnection();
        String fakePhoneNumber = calcFakePhoneNumber(realPhoneNumber);
        String hashFromRealNumber = DigestUtils.sha256Hex(realPhoneNumber);
        PreparedStatement fakePhoneNumberStatement = connection.prepareStatement(
                "INSERT INTO `competence-schema`.`fake_phones` " +
                        "(fake_phone_number, real_phone_number) VALUES (?, ?)");
        fakePhoneNumberStatement.setString(1, fakePhoneNumber);
        fakePhoneNumberStatement.setString(2, hashFromRealNumber);
        fakePhoneNumberStatement.executeUpdate();
        connection.close();
        return fakePhoneNumber;
    }

    public static boolean deleteFakePhone(String fakeNumber) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement phoneStatement = connection.prepareStatement(
                "DELETE FROM `competence-schema`.`fake_phones` WHERE fake_phone_number=?");
        phoneStatement.setString(1, fakeNumber);

        boolean isFinished = phoneStatement.executeUpdate() > 0;
        connection.close();
        return isFinished;
    }
}
