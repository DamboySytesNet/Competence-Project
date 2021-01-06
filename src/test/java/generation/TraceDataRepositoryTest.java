package generation;

import connectors.CassandraConnector;
import model.TraceData;
import org.junit.Assert;
import org.junit.Test;
import repository.ExperimentRepository;
import repository.TraceDataRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import java.util.List;
import java.util.UUID;

public class TraceDataRepositoryTest {

    @Test
    public void connectionTest() {
        CassandraConnector connector = new CassandraConnector();
        connector.connect();
        TraceDataRepository traceDataRepository = new TraceDataRepository(connector.getSession());

        TraceData traceData = TraceData.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .pointOfInterestId(UUID.randomUUID())
                .entryTime(LocalDateTime.now())
                .exitTime(LocalDateTime.now())
                .experimentId(ExperimentRepository.DEFAULT_ID)
                .build();

        long totalTraces = traceDataRepository.getTotalNumberOfTraces();

        traceDataRepository.insertTrace(traceData);

        TraceData savedData = traceDataRepository.getTraceById(traceData.getId());

        assertTracesEquals(traceData, savedData);
        Assert.assertNull(traceData.getPreviousTraceId());

        Assert.assertEquals(totalTraces + 1, traceDataRepository.getTotalNumberOfTraces());

        traceDataRepository.deleteTraces(Collections.singletonList(traceData.getId()));

        Assert.assertEquals(totalTraces, traceDataRepository.getTotalNumberOfTraces());

        TraceData secondTraceData = TraceData.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .pointOfInterestId(UUID.randomUUID())
                .entryTime(LocalDateTime.now())
                .exitTime(LocalDateTime.now())
                .pointOfInterestId(traceData.getId())
                .experimentId(ExperimentRepository.DEFAULT_ID)
                .build();
      
        traceDataRepository.insertTrace(secondTraceData);

        TraceData savedSecondData = traceDataRepository.getTraceById(secondTraceData.getId());

        Assert.assertEquals(secondTraceData.getPreviousTraceId(), savedSecondData.getPreviousTraceId());

        TraceData thirdTraceData = TraceData.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .pointOfInterestId(UUID.randomUUID())
                .entryTime(LocalDateTime.now())
                .exitTime(LocalDateTime.now())
                .pointOfInterestId(secondTraceData.getId())
                .experimentId(ExperimentRepository.DEFAULT_ID)
                .build();

        TraceData fourthTraceData = TraceData.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .pointOfInterestId(UUID.randomUUID())
                .entryTime(LocalDateTime.now())
                .exitTime(LocalDateTime.now())
                .pointOfInterestId(thirdTraceData.getId())
                .experimentId(ExperimentRepository.DEFAULT_ID)
                .build();

        traceDataRepository.insertTraces(Arrays.asList(thirdTraceData, fourthTraceData));

        TraceData savedThirdData = traceDataRepository.getTraceById(thirdTraceData.getId());

        assertTracesEquals(thirdTraceData, savedThirdData);

        TraceData savedFourthData = traceDataRepository.getTraceById(fourthTraceData.getId());

        assertTracesEquals(fourthTraceData, savedFourthData);

        traceDataRepository.deleteTraces(Arrays.asList(secondTraceData.getId(), thirdTraceData.getId(), fourthTraceData.getId()));

        connector.close();
    }

    @Test
    public void shouldGetDailyTraces(){
        CassandraConnector connector = new CassandraConnector();
        connector.connect();

        TraceDataRepository traceRepository = new TraceDataRepository(connector.getSession());

        long totalTracesForToday = traceRepository.getTracesForToday().size();

        TraceData traceYesterday = TraceData.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .pointOfInterestId(UUID.randomUUID())
                .entryTime(LocalDateTime.now().minusDays(1))
                .exitTime(LocalDateTime.now().minusDays(1))
                .experimentId(UUID.randomUUID())
                .build();

        TraceData traceToday = TraceData.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .pointOfInterestId(UUID.randomUUID())
                .entryTime(LocalDateTime.now())
                .exitTime(LocalDateTime.now())
                .experimentId(UUID.randomUUID())
                .build();

        TraceData traceTomorrow = TraceData.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .pointOfInterestId(UUID.randomUUID())
                .entryTime(LocalDateTime.now().plusDays(1))
                .exitTime(LocalDateTime.now().plusDays(1))
                .experimentId(UUID.randomUUID())
                .build();

        traceRepository.insertTraces(Arrays.asList(traceYesterday, traceToday, traceTomorrow));

        List<TraceData> savedTracesToday = traceRepository.getTracesForToday();

        Assert.assertEquals(totalTracesForToday + 1, savedTracesToday.size());

        traceRepository.deleteTraces(Arrays.asList(traceYesterday.getId(), traceToday.getId(), traceTomorrow.getId()));

        connector.close();
    }

    void assertTracesEquals(TraceData t1, TraceData t2) {
        Assert.assertEquals(t1.getId(), t2.getId());
        Assert.assertEquals(t1.getUserId(), t2.getUserId());
        Assert.assertEquals(t1.getPointOfInterestId(), t2.getPointOfInterestId());
        Assert.assertEquals(t1.getEntryTime().toString().substring(0, 23), t2.getEntryTime().toString());
        Assert.assertEquals(t1.getExitTime().toString().substring(0, 23), t2.getExitTime().toString());
    }

}
