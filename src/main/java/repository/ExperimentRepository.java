package repository;

import generation.Experiment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static connectors.JavaDatabaseConnector.getConnection;

public class ExperimentRepository {
    public static boolean save(Experiment experiment) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO `competence-schema`.`experiments` "
                        + "(id, date) VALUES (?, NOW())");
        statement.setString(1, experiment.getId().toString());
        boolean isFinished = statement.executeUpdate() > 0;
        connection.close();
        return isFinished;
    }
}
