package generation;

import model.POI;
import model.Trace;
import model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ranking.POIRanking;

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

        HashMap<POI, Integer> visitorsRankingDesc = POIRanking.generateVisitorsCountRanking(traces,true);
        HashMap<POI, Integer> visitorsRankingAsc = POIRanking.generateVisitorsCountRanking(traces,false);
        HashMap<POI, Integer> timeRankingDesc = POIRanking.generateTimeSpentRanking(traces, true);
        HashMap<POI, Integer> timeRankingAsc = POIRanking.generateTimeSpentRanking(traces, false);

        Assert.assertTrue(traces.size() != 0);
        Assert.assertTrue(users.size() != 0);

        Integer[] tableToTest = new Integer[visitorsRankingAsc.values().size()];

        //visitorsASC
        tableToTest =  visitorsRankingAsc.values().toArray(tableToTest);
        for(int i = 0 ; i < tableToTest.length - 1  ; i++){
                Assert.assertTrue(tableToTest[i] <= tableToTest[i+1]);
        }

        //visitorsDESC
        tableToTest =  visitorsRankingDesc.values().toArray(tableToTest);
        for(int i = 0 ; i < tableToTest.length - 1  ; i++){
            Assert.assertTrue(tableToTest[i] >= tableToTest[i+1]);
        }

        //timespentASC
        tableToTest =  timeRankingAsc.values().toArray(tableToTest);
        for(int i = 0 ; i < tableToTest.length - 1 ; i++){
            Assert.assertTrue(tableToTest[i] <= tableToTest[i+1]);
        }

        //timespentDESC
        tableToTest =  timeRankingDesc.values().toArray(tableToTest);
        for(int i = 0 ; i < tableToTest.length - 1 ; i++){
            Assert.assertTrue(tableToTest[i] >= tableToTest[i+1]);
        }
    }
}
