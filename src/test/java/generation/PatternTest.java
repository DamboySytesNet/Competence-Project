package generation;

import model.POI;
import model.Route;
import model.Trace;
import model.User;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class PatternTest {
    private List<User> users;
    private List<POI> pois;
    private LocalDateTime time;
    private int timeStep; // min

    @Before
    public void setUp() {
        this.users = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            this.users.add(UserFactory.getInstance().generate());
        }

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
        TraceGenerator traceGenerator = new TraceGenerator(users, pois, time);
        LocalDateTime currentTime = this.time;
        // when
        List<Trace> traces = new LinkedList<>();

        do {
            currentTime = currentTime.plusMinutes(this.timeStep);
            traces.addAll(traceGenerator.generateTraces(currentTime));
        } while (traces.size() < 100);

        List<Route> userTraces = traces.stream()
                .collect(Collectors.groupingBy(trace -> trace.getUser()))
                .entrySet().stream()
                .map(entry -> new Route(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        userTraces.stream()
                .peek(System.out::println)
                .forEach(r ->
                        r.getTraces().stream()
                                .sorted(Comparator.comparing(Trace::getEntryTime))
                                .map(t -> t.getEntryTime())
                                .forEach(System.out::println)
        );
    }
}