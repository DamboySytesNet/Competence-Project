package generation;

import connectors.CassandraConnector;
import model.TraceData;
import org.junit.Assert;
import org.junit.Test;
import repository.KeyspaceRepository;
import repository.TraceDataRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

public class TraceDataRepositoryTest {

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
        TraceDataRepository traceDataRepository = new TraceDataRepository(connector.getSession());

        traceDataRepository.createTable();
        TraceData traceData = TraceData.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .pointOfInterestId(UUID.randomUUID())
                .entryTime(LocalDateTime.now())
                .exitTime(LocalDateTime.now())
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
                .build();

        TraceData fourthTraceData = TraceData.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .pointOfInterestId(UUID.randomUUID())
                .entryTime(LocalDateTime.now())
                .exitTime(LocalDateTime.now())
                .pointOfInterestId(thirdTraceData.getId())
                .build();

        traceDataRepository.insertTraces(Arrays.asList(thirdTraceData, fourthTraceData));

        TraceData savedThirdData = traceDataRepository.getTraceById(thirdTraceData.getId());

        assertTracesEquals(thirdTraceData, savedThirdData);

        TraceData savedFourthData = traceDataRepository.getTraceById(fourthTraceData.getId());

        assertTracesEquals(fourthTraceData, savedFourthData);

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
