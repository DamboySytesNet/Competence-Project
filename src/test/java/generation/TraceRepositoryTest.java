package generation;

import connectors.CassandraConnector;
import model.TraceData;
import org.junit.Assert;
import org.junit.Test;
import repository.KeyspaceRepository;
import repository.TraceRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TraceRepositoryTest {

    @Test
    public void test() {
        CassandraConnector connector = new CassandraConnector();
        connector.connect();

        String keyspaceName = "competence_project";

        KeyspaceRepository keyspaceRepository = new KeyspaceRepository(connector.getSession());
        keyspaceRepository.createKeyspace(
                keyspaceName,
                "SimpleStrategy",
                1);

        keyspaceRepository.useKeyspace(keyspaceName);
        TraceRepository traceRepository = new TraceRepository(connector.getSession());
        traceRepository.createTable();
        TraceData traceData = TraceData.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .pointOfInterestId(UUID.randomUUID())
                .entryTime(LocalDateTime.now())
                .exitTime(LocalDateTime.now())
                .build();

        long totalTraces = traceRepository.getTotalNumberOfTraces();

        traceRepository.insertTrace(traceData);

        TraceData savedData = traceRepository.getTraceById(traceData.getId());

        assertTracesEquals(traceData, savedData);
        Assert.assertNull(traceData.getPreviousTraceId());

        Assert.assertEquals(totalTraces + 1, traceRepository.getTotalNumberOfTraces());

        traceRepository.deleteTraces(Collections.singletonList(traceData.getId()));

        Assert.assertEquals(totalTraces, traceRepository.getTotalNumberOfTraces());

        TraceData secondTraceData = TraceData.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .pointOfInterestId(UUID.randomUUID())
                .entryTime(LocalDateTime.now())
                .exitTime(LocalDateTime.now())
                .pointOfInterestId(traceData.getId())
                .build();
        traceRepository.insertTrace(secondTraceData);

        TraceData savedSecondData = traceRepository.getTraceById(secondTraceData.getId());

        Assert.assertEquals(secondTraceData.getPreviousTraceId(), savedSecondData.getPreviousTraceId());

        TraceData thirdTraceData = TraceData.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .pointOfInterestId(UUID.randomUUID())
                .entryTime(LocalDateTime.now())
                .exitTime(LocalDateTime.now())
                .pointOfInterestId(secondTraceData.getId())
                .build();

        TraceData fourthTraceData = TraceData.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .pointOfInterestId(UUID.randomUUID())
                .entryTime(LocalDateTime.now())
                .exitTime(LocalDateTime.now())
                .pointOfInterestId(thirdTraceData.getId())
                .build();

        traceRepository.insertTraces(Arrays.asList(thirdTraceData, fourthTraceData));

        TraceData savedThirdData = traceRepository.getTraceById(thirdTraceData.getId());

        assertTracesEquals(thirdTraceData, savedThirdData);

        TraceData savedFourthData = traceRepository.getTraceById(fourthTraceData.getId());

        assertTracesEquals(fourthTraceData, savedFourthData);

        traceRepository.deleteTraces(Arrays.asList(secondTraceData.getId(), thirdTraceData.getId(), fourthTraceData.getId()));

        connector.close();
    }

    @Test
    public void shouldGetDailyTraces(){
        CassandraConnector connector = new CassandraConnector();
        connector.connect();

        String keyspaceName = "competence_project";

        KeyspaceRepository keyspaceRepository = new KeyspaceRepository(connector.getSession());
        keyspaceRepository.createKeyspace(
                keyspaceName,
                "SimpleStrategy",
                1);

        keyspaceRepository.useKeyspace(keyspaceName);
        TraceRepository traceRepository = new TraceRepository(connector.getSession());
        traceRepository.createTable();

        long totalTracesForToday = traceRepository.getTracesForToday().size();

        TraceData traceYesterday = TraceData.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .pointOfInterestId(UUID.randomUUID())
                .entryTime(LocalDateTime.now().minusDays(1))
                .exitTime(LocalDateTime.now().minusDays(1))
                .build();

        TraceData traceToday = TraceData.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .pointOfInterestId(UUID.randomUUID())
                .entryTime(LocalDateTime.now())
                .exitTime(LocalDateTime.now())
                .build();

        TraceData traceTomorrow = TraceData.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .pointOfInterestId(UUID.randomUUID())
                .entryTime(LocalDateTime.now().plusDays(1))
                .exitTime(LocalDateTime.now().plusDays(1))
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
