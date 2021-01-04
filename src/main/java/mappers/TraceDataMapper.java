package mappers;

import model.POI;
import model.Trace;
import model.TraceData;
import model.User;
import repository.POIRepository;
import repository.UserRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TraceDataMapper {

    public static Trace mapTraceDataToTrace(TraceData traceData) {
        User user = null;
        POI poi = null;
        try {
            user = UserRepository.getById(traceData.getUserId());
            poi = POIRepository.getById(traceData.getPointOfInterestId());
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return Trace.builder()
                .id(traceData.getId())
                .entryTime(traceData.getEntryTime())
                .exitTime(traceData.getExitTime())
                .pointOfInterest(poi)
                .user(user)
                .experimentId(traceData.getExperimentId())
                .build();
    }

    public static TraceData mapTraceToTraceData(Trace trace){
        return TraceData.builder()
                .id(trace.getId())
                .entryTime(trace.getEntryTime())
                .exitTime(trace.getExitTime())
                .userId(trace.getUser().getUserID())
                .pointOfInterestId(trace.getPointOfInterest().getId())
                .experimentId(trace.getExperimentId())
                .build();
    }

    public static List<TraceData> mapTracesToTracesData(List<Trace> traces){
        List<TraceData> tracesData = new ArrayList<>();
        for(Trace trace : traces){
            tracesData.add(mapTraceToTraceData(trace));
        }
        return tracesData;
    }
}
