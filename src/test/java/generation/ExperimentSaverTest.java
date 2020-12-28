package generation;

import org.junit.Test;

import java.sql.SQLException;

public class ExperimentSaverTest {

    @Test
    public void shouldSaveExperimentResultsToDB() {
        Experiment experiment = new Experiment(4235,6634,3534, 3);
        try {
            ExperimentSaver.saveExperimentResults(experiment);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
