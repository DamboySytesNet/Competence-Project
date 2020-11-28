package generation;

import model.POI;
import model.Trace;
import model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class TraceGeneratorTest {
    private List<User> users;
    private List<POI> pois;
    private LocalDateTime time;
    private int timeStep; // min

    private TraceGenerator traceGenerator;

    @Before
    public void setUp() throws Exception {
        this.users = new LinkedList<>();
        this.users.add(new User());
        this.users.add(new User());
        this.users.add(new User());
        this.users.add(new User());
        this.users.add(new User());

        this.pois = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            this.pois.add(POIFactory.getInstance().generate());
        }

        this.time = LocalDateTime.now();
        this.timeStep = 3;
    }

    @Test
    public void generateTracesTest() {
        // given
        this.traceGenerator = new TraceGenerator(users, pois, time);
        LocalDateTime currentTime = this.time;
        // when
        List<Trace> traces = new LinkedList<>();

        do {
            currentTime = currentTime.plusMinutes(this.timeStep);
            traces.addAll(this.traceGenerator.generateTraces(currentTime));
        } while (traces.size() < 10);

        System.out.println(traces);
        // check
        for (Trace trace: traces) {
            Assert.assertNotNull(trace.getUser());
            Assert.assertNotNull(trace.getPointOfInterest());
            Assert.assertNotNull(trace.getEntryTime());
            Assert.assertNotNull(trace.getExitTime());

            Assert.assertTrue(trace.getEntryTime().isAfter(this.time));
            Assert.assertTrue(trace.getExitTime().isAfter(trace.getEntryTime()));
        }
    }
}