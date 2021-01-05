package statistics;

import generation.GeneratorConsts;
import generation.POIFactory;
import generation.TraceGenerator;
import generation.UserFactory;
import model.POI;
import model.Trace;
import model.User;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.ExperimentRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

public class StatisticsTest {

    static List<Trace> traces;
    static POIFactory poiFactory;
    static UserFactory userFactory;
    static List<POI> pointsOfInterest;
    static List<User> users;
    static LocalDateTime startTime;
    static Statistics stats;


    @BeforeClass
    public static void init() {
        poiFactory = POIFactory.getInstance();
        userFactory = UserFactory.getInstance();
        pointsOfInterest = new LinkedList<>();
        users = new LinkedList<>();
        startTime = LocalDateTime.now();
        traces = new LinkedList<>();
        stats = new Statistics();

        for (int i = 0; i < 3; i++) {
            pointsOfInterest.add(poiFactory.generate());
        }

        for (int i = 0; i < 1; i++) {
            users.add(userFactory.generate());
        }

        LocalDateTime currentTime = startTime;
        TraceGenerator traceGenerator = new TraceGenerator(users, pointsOfInterest, currentTime);

        for (int i = 0; i < 60; i++) {
            traces.addAll(traceGenerator.generateTraces(currentTime, ExperimentRepository.DEFAULT_ID));
            currentTime = currentTime.plusMinutes(GeneratorConsts.TIME_STEP);
        }

        stats.addNewTrace(traces.get(0), null);
        for (int i = 1; i < traces.size(); i++) {
            stats.addNewTrace(traces.get(i), traces.get(i - 1));
        }
    }

    @Test
    public void testLengthOfStayStatistics() {
        System.out.println("\n--------------TRACES");
        for (Trace trace : traces) {
            System.out.println(
                "POI: " + trace.getPointOfInterest().getId() + " " +
                    ChronoUnit.SECONDS.between(
                        trace.getEntryTime(),
                        trace.getExitTime()) / 60.0 + " min"
            );
        }
        System.out.println();

        System.out.println(stats.getLengthOfStayText(users.get(0), pointsOfInterest.get(0)));
        System.out.println();

        Assert.assertTrue(true);
    }

    @Test
    public void testLongestRouteStatistics() {
        System.out.println("\n--------------TRACES");
        for (int i = 1; i < traces.size(); i++) {
            System.out.println(traces.get(i).getPointOfInterest().getGeolocalization()
                .getDistance(traces.get(i - 1).getPointOfInterest().getGeolocalization()));
        }
        System.out.println();

        System.out.println(stats.getLongestRouteText(users.get(0)));
        System.out.println();

        Assert.assertTrue(true);
    }

    @Test
    public void testPopularityStatistics() {
        System.out.println("--------------POIs");
        for (POI poi : pointsOfInterest) {
            System.out.println(poi.getId());
        }

        System.out.println("\n--------------TRACES");
        for (Trace trace : traces) {
            System.out.println(trace.getPointOfInterest().getId());
        }
        System.out.println();

        System.out.println(stats.getMostPopularPOIText(pointsOfInterest.get(0)));
        System.out.println();
        System.out.println(stats.getMostPopularPOIText(pointsOfInterest.get(1)));
        System.out.println();
        System.out.println(stats.getMostPopularPOIText(pointsOfInterest.get(2)));

        Assert.assertTrue(true);
    }
}
