package ranking;

import model.POI;
import model.Trace;

import java.time.Duration;


import java.util.List;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.Map;


import lombok.AllArgsConstructor;
import lombok.Data;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;


@AllArgsConstructor
@Data

public class POIDynamicRanking {
    private HashMap<POI, Integer> visitorsCountRanking;
    private HashMap<POI, Integer> timeSpentRanking;

    public POIDynamicRanking(){
        visitorsCountRanking= new HashMap<>();
        timeSpentRanking=new HashMap<>();
    }
    //visitors count in POI ranking
    //true == descending / false == ascending
    public void updateVisitorsCountRanking(List<Trace> traces, boolean descending){
        for(Trace trace : traces) {
            this.visitorsCountRanking.put(trace.getPointOfInterest(), this.visitorsCountRanking.getOrDefault(trace.getPointOfInterest(), 0) + 1);
        }
        setVisitorsCountRanking(this.visitorsCountRanking
                .entrySet()
                .stream()
                .sorted(descending ? Collections.reverseOrder(Map.Entry.comparingByValue()) : comparingByValue())
                .collect(
                        toMap(Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e2,
                                LinkedHashMap::new)));
    }

    //time spent in POI ranking
    //true == descending / false == ascending
    public void updateTimeSpentRanking(List<Trace> traces, boolean descending){

        for(Trace trace : traces) {
            Duration timeSpent = Duration.between(trace.getEntryTime(), trace.getExitTime());
            this.timeSpentRanking.put(trace.getPointOfInterest(), this.timeSpentRanking.getOrDefault(trace.getPointOfInterest(), 0) + (int) timeSpent.toMinutes());
        }

       setTimeSpentRanking(this.timeSpentRanking
               .entrySet()
               .stream()
               .sorted(descending ? Collections.reverseOrder(Map.Entry.comparingByValue()) : comparingByValue())
               .collect(
                       toMap(Map.Entry::getKey,
                               Map.Entry::getValue,
                               (e1, e2) -> e2,
                               LinkedHashMap::new)));
    }

}
