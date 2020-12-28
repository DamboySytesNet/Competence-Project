package generation;

import org.junit.Test;

public class ExperimentSaverTest {

    @Test
    public void shouldSaveExperimentResultsToDB() {
        Experiment experiment = new Experiment(4235,6634,3534, 3);
        ExperimentSaver.saveExperimentResults(experiment);
    }
}
