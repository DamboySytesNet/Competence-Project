package learning;

import generation.Experiment;
import model.Trace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RouteTest {
    int noUsers = 5;
    int noPois = 10;
    int noTraces = 100;
    int timeStep = 3;
    Experiment experiment;
    List<Route> routes;

    @Before
    public void setUp() {
        noUsers = 5;
        noPois = 10;
        noTraces = 100;
        timeStep = 3;
        experiment = new Experiment(noUsers, noPois, noTraces, timeStep);

        routes = experiment.getTraces().stream()
                .collect(Collectors.groupingBy(Trace::getUser))
                .entrySet().stream()
                .map(entry -> new Route(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }


    @Test
    public void chronologicalOrderTest() {
        boolean result = routes.stream()
                .allMatch(r -> r.getTraces().stream()
                        .sorted(Comparator.comparing(Trace::getEntryTime))
                        .collect(Collectors.toList())
                        .equals(r.getTraces())
        );

        Assert.assertTrue(result);
    }

    @Test
    public void groupingWithLimitTest() {
        int limit = 3;

        routes.forEach(route -> {
            List<List<Trace>> groupedTraces = route.getGroupedTraces(limit);

            Assert.assertEquals(route.getTraces().size() - limit + 1, groupedTraces.size());
            groupedTraces.forEach(lt -> {
                Assert.assertEquals(limit, lt.size());
//                System.out.println(lt);
                lt.forEach(Assert::assertNotNull);
//                lt.forEach(t -> {
//                    System.out.println(t.getEntryTime());
//                    System.out.println(t.getPointOfInterest());
//                             });

            });
        });
    }
}