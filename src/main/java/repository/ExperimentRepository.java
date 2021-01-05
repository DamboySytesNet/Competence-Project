package repository;

import connectors.CassandraConnector;
import generation.Experiment;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static connectors.JavaDatabaseConnector.getConnection;

public class ExperimentRepository {

    public static final UUID DEFAULT_ID = UUID.fromString("ccb7764e-4e85-11eb-ae93-0242ac130002");

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

    public static List<Pair<UUID, Long>> getAllIds() throws SQLException {
        Connection connection = getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery(
                "SELECT * FROM `competence-schema`.`experiments`");
        List<Pair<UUID, Long>> ids = new ArrayList<>();
        CassandraConnector connector = new CassandraConnector();
        connector.connect();
        TraceDataRepository traceDataRepository = new TraceDataRepository(connector.getSession());
        while (resultSet.next()) {
            UUID experimentId = UUID.fromString(resultSet.getString("id"));
            long noTraces = traceDataRepository.getTotalNumberOfTracesByExperimentId(experimentId);
            ids.add(new ImmutablePair<>(
                    experimentId,
                    noTraces));
        }
        connection.close();
        return ids;
    }
}
