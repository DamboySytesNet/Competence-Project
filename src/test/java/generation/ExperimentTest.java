package generation;

import model.Trace;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

public class ExperimentTest {

    @Test
    public void generatingTest() {

        // given
        int noUsers = 10;
        int noPois = 10;
        int noTraces = 100;
        int timeStep = 3;
        // when
        LocalDateTime startTime = LocalDateTime.now();
        Experiment experiment = new Experiment(noUsers, noPois, noTraces, timeStep);

        // check
        Assert.assertNotNull(experiment.getId());
        Assert.assertNotNull(experiment.getPois());
        Assert.assertNotNull(experiment.getUsers());
        Assert.assertNotNull(experiment.getTraces());
        Assert.assertNotNull(experiment.getStartTime());
        Assert.assertNotNull(experiment.getCurrentTime());
        Assert.assertNotNull(experiment.getTraceGenerator());

        Assert.assertTrue(experiment.getTraces().size() > 100);
        Assert.assertEquals(10, experiment.getUsers().size());
        Assert.assertEquals(10, experiment.getPois().size());

        for (Trace trace: experiment.getTraces()) {
            Assert.assertTrue(trace.getEntryTime().isAfter(startTime));
            Assert.assertTrue(trace.getExitTime().isAfter(trace.getEntryTime()));
        }
    }
}
