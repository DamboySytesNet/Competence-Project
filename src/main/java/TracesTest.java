import generation.GeneratorConsts;
import generation.POIFactory;
import generation.TraceGenerator;
import generation.UserFactory;
import model.POI;
import model.Trace;
import model.User;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class TracesTest {

    public static void main(String[] args) throws InterruptedException {

        POIFactory poiFactory = POIFactory.getInstance();
        UserFactory userFactory = UserFactory.getInstance();

        List<POI> pointsOfInterest = new LinkedList<>();
        List<User> users = new LinkedList<>();

        LocalDateTime startTime = LocalDateTime.now();

        for (int i=0; i < 100; i++) {
            pointsOfInterest.add(poiFactory.generate());
        }

        for (int i=0; i < 10; i++) {
            users.add(userFactory.generate());
        }

        LocalDateTime currentTime = startTime;
        TraceGenerator traceGenerator = new TraceGenerator(users, pointsOfInterest, currentTime);

//        System.out.println(users);

        for (int i=0; i<20; ++i) {
            currentTime = currentTime.plusMinutes(GeneratorConsts.TIME_STEP);
            List<Trace> traces = traceGenerator.generateTraces(currentTime);
//            System.out.println(currentTime);
//            System.out.println(traces.size());
            System.out.println(traces);
            Thread.sleep(1000);
        }

    }
}
