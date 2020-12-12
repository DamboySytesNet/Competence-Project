package ranking;

import lombok.AllArgsConstructor;
import lombok.Data;
import model.POI;
import model.Trace;

import java.time.Duration;
import java.util.*;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;


@AllArgsConstructor
@Data

public class POIRanking {

    //visitors count in POI ranking
    //true == descending / false == ascending
    public static HashMap<POI, Integer> generateVisitorsCountRanking(List<Trace> traces, boolean descending){
        HashMap<POI,Integer> visitorsCount = new HashMap<>();
        for(Trace trace : traces) {
            visitorsCount.put(trace.getPointOfInterest(), visitorsCount.getOrDefault(trace.getPointOfInterest(), 0) + 1);
        }
        return sortByValue(visitorsCount, descending);
    }

    //time spent in POI ranking
    //true == descending / false == ascending
    public static  HashMap<POI, Integer> generateTimeSpentRanking(List<Trace> traces, boolean descending){
        HashMap<POI,Integer> totalTimeSpent = new HashMap<>();
        for(Trace trace : traces) {
            Duration timeSpent = Duration.between(trace.getEntryTime(), trace.getExitTime());
            totalTimeSpent.put(trace.getPointOfInterest(), totalTimeSpent.getOrDefault(trace.getPointOfInterest(), 0) + (int) timeSpent.toMinutes());
        }
        return sortByValue(totalTimeSpent, descending);
    }
    //true == descending / false == ascending
    public static HashMap<POI, Integer> sortByValue(HashMap<POI, Integer> toBeSorted, boolean descending ){
        return toBeSorted
                .entrySet()
                .stream()
                .sorted(descending ? Collections.reverseOrder(Map.Entry.comparingByValue()) : comparingByValue())
                .collect(
                        toMap(Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e2,
                                LinkedHashMap::new));
    }


}
