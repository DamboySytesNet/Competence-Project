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

    private POIDynamicRanking ranking;

    private List<Experiment> experimentList;

    @Before
    public void setUp() throws Exception {
        this.ranking = new POIDynamicRanking();

        this.experimentList = new LinkedList<>();

        this.experimentList.add(new Experiment(1412,1240,1412, 3 ));
        this.experimentList.add(new Experiment(4456,7566,5677, 3 ));
        this.experimentList.add(new Experiment(4235,6634,3534, 3 ));
    }

    @Test
    public void rankingTest() {

        Integer[] tableToTest;

        //VISITORS ASC
        for(Experiment experiment : this.experimentList){
            ranking.updateVisitorsCountRanking(experiment.getTraces(), false);

            tableToTest = new Integer[ranking.getVisitorsCountRanking().values().size()];

            tableToTest =  ranking.getVisitorsCountRanking().values().toArray(tableToTest);

            for(int i = 0 ; i < tableToTest.length - 1 ; i++){
                Assert.assertTrue(tableToTest[i] <= tableToTest[i+1]);
            }
        }

        //VISITORS DESC
        for(Experiment experiment : this.experimentList){
            ranking.updateVisitorsCountRanking(experiment.getTraces(), true);

            tableToTest = new Integer[ranking.getVisitorsCountRanking().values().size()];

            tableToTest =  ranking.getVisitorsCountRanking().values().toArray(tableToTest);

            for(int i = 0 ; i < tableToTest.length - 1 ; i++){
                Assert.assertTrue(tableToTest[i] >= tableToTest[i+1]);
            }
        }

        //TIME ASC
        for(Experiment experiment : this.experimentList){
            ranking.updateTimeSpentRanking(experiment.getTraces(), false);

            tableToTest = new Integer[ranking.getTimeSpentRanking().values().size()];

            tableToTest =  ranking.getTimeSpentRanking().values().toArray(tableToTest);

            for(int i = 0 ; i < tableToTest.length - 1 ; i++){
                Assert.assertTrue(tableToTest[i] <= tableToTest[i+1]);
            }
        }

        //TIME DESC
        for(Experiment experiment : this.experimentList){
            ranking.updateTimeSpentRanking(experiment.getTraces(), true);

            tableToTest = new Integer[ranking.getTimeSpentRanking().values().size()];

            tableToTest =  ranking.getTimeSpentRanking().values().toArray(tableToTest);

            for(int i = 0 ; i < tableToTest.length - 1 ; i++){
                Assert.assertTrue(tableToTest[i] >= tableToTest[i+1]);
            }
        }
    }
}
