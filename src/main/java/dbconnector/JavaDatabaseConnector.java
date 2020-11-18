package dbconnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JavaDatabaseConnector {

    private final static String URL = "jdbc:mysql://localhost:3306/competence-schema";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "password";
    private final static String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

    public static ResultSet doOperation(String query) {
        ResultSet resultSet = null;
        try {
            Class.forName(DRIVER_NAME);
            Connection connection = DriverManager.getConnection(
                    URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        assert resultSet != null;
        return resultSet;
    }

}
