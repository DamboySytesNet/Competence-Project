package generation;

import ranking.POIDynamicRanking;
import ranking.POIRanking;
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

public class DynamicRankingTest {

    private List<List<Trace>> traces;

    private POIDynamicRanking ranking;

    public List<Trace> generateTraces(int usersSize, int poisSize, int tracesSize, int timeStep){

        List<User> users = new LinkedList<>();
        List<POI> pois = new LinkedList<>();
        List<Trace> traces = new LinkedList<>();
        LocalDateTime time = LocalDateTime.now();;
        TraceGenerator traceGenerator;
        //gen Users
        for (int i = 0; i < usersSize; i++) {
            users.add(UserFactory.getInstance().generate());
        }
        //gen Pois
        for (int i = 0; i < poisSize; i++) {
            pois.add(POIFactory.getInstance().generate());
        }

        traceGenerator = new TraceGenerator(users, pois, time);
        LocalDateTime currentTime = time;
        //gen Traces
        do {
            currentTime = currentTime.plusMinutes(timeStep);
            traces.addAll(traceGenerator.generateTraces(currentTime));
        } while (traces.size() < tracesSize);

        return traces;
    }



    @Before
    public void setUp() throws Exception {
        this.ranking = new POIDynamicRanking();
        this.traces = new LinkedList<>();
        this.traces.add(generateTraces(134,220,12412, 3 ));
        this.traces.add(generateTraces(14124,124160,14124, 3 ));
        this.traces.add(generateTraces(14124,42460,442340, 3 ));
        this.traces.add(generateTraces(445640,75660,5677, 3 ));
        this.traces.add(generateTraces(423530,6634560,353440, 3 ));





    }

    @Test
    public void rankingTest() {

        Integer[] tableToTest;

        //VISITORS ASC
        for(List<Trace> tracesList : this.traces){
            ranking.updateVisitorsCountRanking(tracesList, false);

            tableToTest = new Integer[ranking.getVisitorsCountRanking().values().size()];

            tableToTest =  ranking.getVisitorsCountRanking().values().toArray(tableToTest);

            for(int i = 0 ; i < tableToTest.length - 1 ; i++){
                Assert.assertTrue(tableToTest[i] <= tableToTest[i+1]);
            }
        }

        //VISITORS DESC
        for(List<Trace> tracesList : this.traces){
            ranking.updateVisitorsCountRanking(tracesList, true);

            tableToTest = new Integer[ranking.getVisitorsCountRanking().values().size()];

            tableToTest =  ranking.getVisitorsCountRanking().values().toArray(tableToTest);

            for(int i = 0 ; i < tableToTest.length - 1 ; i++){
                Assert.assertTrue(tableToTest[i] >= tableToTest[i+1]);
            }
        }

        //TIME ASC
        for(List<Trace> tracesList : this.traces){
            ranking.updateTimeSpentRanking(tracesList, false);

            tableToTest = new Integer[ranking.getTimeSpentRanking().values().size()];

            tableToTest =  ranking.getTimeSpentRanking().values().toArray(tableToTest);

            for(int i = 0 ; i < tableToTest.length - 1 ; i++){
                Assert.assertTrue(tableToTest[i] <= tableToTest[i+1]);
            }
        }

        //TIME DESC
        for(List<Trace> tracesList : this.traces){
            ranking.updateTimeSpentRanking(tracesList, true);

            tableToTest = new Integer[ranking.getTimeSpentRanking().values().size()];

            tableToTest =  ranking.getTimeSpentRanking().values().toArray(tableToTest);

            for(int i = 0 ; i < tableToTest.length - 1 ; i++){
                Assert.assertTrue(tableToTest[i] >= tableToTest[i+1]);
            }
        }
    }
}
