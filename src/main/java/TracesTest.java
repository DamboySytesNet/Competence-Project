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

        for (int i=0; i < 1000; i++) {
            pointsOfInterest.add(poiFactory.generate());
        }

        for (int i=0; i < 10000; i++) {
            users.add(userFactory.generate());
        }

        LocalDateTime currentTime = startTime;
        TraceGenerator traceGenerator = new TraceGenerator(users, pointsOfInterest);

        System.out.println(users);

        while (true) {
            List<Trace> traces = traceGenerator.generateTrace(currentTime);
            System.out.println(currentTime);
            System.out.println(traces.size());
            Thread.sleep(GeneratorConsts.TIME_SPEED * 1000);
            currentTime = currentTime.plusMinutes(GeneratorConsts.TIME_SPEED);
        }

    }
}
