package Ranking;

import model.POI;
import model.Trace;

import java.time.Duration;

import java.util.*;


import lombok.AllArgsConstructor;

import lombok.Data;




@AllArgsConstructor
@Data

public class POIRanking {

    //visitors count in POI ranking
    public static HashMap<POI, Integer> generateVisitorsCountRanking(List<Trace> traces){

        HashMap<POI,Integer> visitorsCount = new HashMap<>();
        for(Trace trace : traces) {
            visitorsCount.put(trace.getPointOfInterest(), visitorsCount.getOrDefault(trace.getPointOfInterest(), 0) + 1);
        }

        return sortByValue(visitorsCount);
    }

    //time spent in POI ranking
    public static  HashMap<POI, Integer> generateTimeSpentRanking(List<Trace> traces){
        HashMap<POI,Integer> totalTimeSpent = new HashMap<>();
        for(Trace trace : traces) {
            Duration timeSpent = Duration.between(trace.getEntryTime(), trace.getExitTime());
            totalTimeSpent.put(trace.getPointOfInterest(), totalTimeSpent.getOrDefault(trace.getPointOfInterest(), 0) + (int) timeSpent.toMinutes());
        }
        return sortByValue(totalTimeSpent);
    }



    public static HashMap<POI, Integer> sortByValue(HashMap<POI, Integer> toBeSorted)
    {
        List<Map.Entry<POI, Integer> > list = new LinkedList<>(toBeSorted.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<POI, Integer>>() {
            public int compare(Map.Entry<POI, Integer> o1,Map.Entry<POI, Integer> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        HashMap<POI, Integer> temp = new LinkedHashMap<POI, Integer>();
        for (Map.Entry<POI, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}
