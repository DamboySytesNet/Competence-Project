package statistics;

import generation.GeneratorConsts;
import generation.POIFactory;
import generation.TraceGenerator;
import generation.UserFactory;
import model.Geolocalization;
import model.POI;
import model.Trace;
import model.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class StatisticsTest {

    List<Trace> traces;
    POIFactory poiFactory;
    UserFactory userFactory;
    List<POI> pointsOfInterest;
    List<User> users;
    LocalDateTime startTime;


    @Before
    public void init() throws InterruptedException {
        poiFactory = POIFactory.getInstance();
        userFactory = UserFactory.getInstance();
        pointsOfInterest = new LinkedList<>();
        users = new LinkedList<>();
        startTime = LocalDateTime.now();
        traces = new LinkedList<>();

        for (int i = 0; i < 5; i++) {
            pointsOfInterest.add(poiFactory.generate());
        }

        for (int i = 0; i < 2; i++) {
            users.add(userFactory.generate());
        }

        LocalDateTime currentTime = startTime;
        TraceGenerator traceGenerator = new TraceGenerator(users, pointsOfInterest);

        System.out.println(users);
        System.out.println();

        for (int i = 0; i < 60; i++) {
            traces.addAll(traceGenerator.generateTrace(currentTime));
            Thread.sleep(1000);
            currentTime = currentTime.plusMinutes(GeneratorConsts.TIME_STEP);
        }

    }


    @Test
    public void generateWaitingTimeStatisticsTest() {

        for (Trace trace : traces) {
            System.out.println("User: " + trace.getUser().getUserID()
                    + ", time in POI " + trace.getPoinOfInterest().getId()
                    + ", (min): " + ChronoUnit.SECONDS.between(trace.getEntryTime(), trace.getExitTime()) / 60.0);
        }

    }

    @Test
    public void generateLongestRouteStatisticsTest() {

        Map<User, Double> userLongestRoute = new HashMap<>();

        for (User user : users) {
            double route = 0.0;
            List<UUID> uniqueIdList = new ArrayList<>();
            uniqueIdList.add(traces.get(0).getPoinOfInterest().getId());
            for (int i = 1; i < traces.size(); i++) {
                if (!uniqueIdList.contains(traces.get(i).getPoinOfInterest().getId())
                        && traces.get(i).getUser().getUserID().equals(user.getUserID())) {
                    uniqueIdList.add(traces.get(i).getPoinOfInterest().getId());
                    route += traces.get(i - 1).getPoinOfInterest().getGeolocalization()
                            .getDistance(traces.get(i).getPoinOfInterest().getGeolocalization());
                }

            }
            userLongestRoute.put(user, route);

        }

        System.out.println(userLongestRoute.toString());

    }
}
