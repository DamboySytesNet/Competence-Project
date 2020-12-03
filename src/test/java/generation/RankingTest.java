package generation;

import Ranking.POIRanking;
import model.POI;
import model.Trace;
import model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class RankingTest {

    private List<User> users;
    private List<POI> pois;
    private List<Trace> traces;
    private LocalDateTime time;
    private int timeStep; // min
    private TraceGenerator traceGenerator;

    @Before
    public void setUp() throws Exception {
        this.users = new LinkedList<>();
        this.pois = new LinkedList<>();
         this.traces = new LinkedList<>();
         //gen Users
        for (int i = 0; i < 10; i++) {
            this.users.add(UserFactory.getInstance().generate());
        }
        //gen Pois
        for (int i = 0; i < 10; i++) {
            this.pois.add(POIFactory.getInstance().generate());
        }

        this.time = LocalDateTime.now();
        this.timeStep = 3;
        this.traceGenerator = new TraceGenerator(users, pois, time);
        LocalDateTime currentTime = this.time;
        //gen Traces
        do {
            currentTime = currentTime.plusMinutes(this.timeStep);
            traces.addAll(this.traceGenerator.generateTraces(currentTime));
        } while (traces.size() < 100);
    }

    @Test
    public void rankingTest() {

        HashMap<POI, Integer> visitorsRanking = POIRanking.generateVisitorsCountRanking(traces);
        HashMap<POI, Integer> timeRanking = POIRanking.generateTimeSpentRanking(traces);

        Assert.assertTrue(traces.size() != 0);
        Assert.assertTrue(users.size() != 0);
        for(POI poi : visitorsRanking.keySet()){
           Assert.assertTrue(visitorsRanking.get(poi) > 0 );
        }
        for(POI poi : timeRanking.keySet()){
            Assert.assertTrue(timeRanking.get(poi) > 0 );
        }
    }
}
