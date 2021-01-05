package generation;

import connectors.CassandraConnector;
import org.junit.Assert;
import org.junit.Test;
import repository.POIRepository;
import repository.TraceDataRepository;
import repository.UserRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExperimentSaverTest {

    @Test
    public void shouldSaveExperimentResultsToDB() {
        int noUsers = 423;
        int noPois = 663;
        int noTraces = 846;
        int timeStep = 3;
        Experiment experiment = new Experiment(noUsers,noPois,noTraces, timeStep);
        try {
            CassandraConnector connector = new CassandraConnector();
            connector.connect();

            TraceDataRepository traceDataRepository = new TraceDataRepository(connector.getSession());

            long noUsersBeforeExp = UserRepository.getTotalNumberOfUsers();
            long noPoisBeforeExp = POIRepository.getTotalNumberOfPOI();

            ExperimentSaver.saveExperimentResults(experiment);

            long noUsersAfterExp = UserRepository.getTotalNumberOfUsers();
            long noPoisAfterExp = POIRepository.getTotalNumberOfPOI();

            Assert.assertEquals(noUsers, noUsersAfterExp - noUsersBeforeExp);
            Assert.assertEquals(noPois, noPoisAfterExp - noPoisBeforeExp);

            List<UUID> tracesIds = new ArrayList<>();
            experiment.getTraces().forEach((e) -> tracesIds.add(e.getId()));
            traceDataRepository.deleteTraces(tracesIds);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
