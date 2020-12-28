package generation;

import org.junit.Assert;
import org.junit.Test;
import repository.POIRepository;
import repository.UserRepository;

import java.sql.SQLException;

public class ExperimentSaverTest {

    @Test
    public void shouldSaveExperimentResultsToDB() {
        int noUsers = 4235;
        int noPois = 6634;
        int noTraces = 3534;
        int timeStep = 3;
        Experiment experiment = new Experiment(noUsers,noPois,noTraces, timeStep);
        try {
            long noUsersBeforeExp = UserRepository.getTotalNumberOfUsers();
            long noPoisBeforeExp = POIRepository.getTotalNumberOfPOI();

            ExperimentSaver.saveExperimentResults(experiment);

            long noUsersAfterExp = UserRepository.getTotalNumberOfUsers();
            long noPoisAfterExp = POIRepository.getTotalNumberOfPOI();

            Assert.assertEquals(noUsers, noUsersAfterExp - noUsersBeforeExp);
            Assert.assertEquals(noPois, noPoisAfterExp - noPoisBeforeExp);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
