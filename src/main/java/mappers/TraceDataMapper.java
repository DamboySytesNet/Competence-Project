package mappers;

import model.POI;
import model.Trace;
import model.TraceData;
import model.User;
import repository.POIRepository;
import repository.UserRepository;

import java.sql.SQLException;

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
                .build();
    }
}
