package generation;

import connectors.CassandraConnector;
import model.TraceData;
import org.junit.Assert;
import org.junit.Test;
import repository.KeyspaceRepository;
import repository.TraceRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

public class TraceRepositoryTest {

    @Test
    public void test() {
        CassandraConnector connector = new CassandraConnector();
        connector.connect("127.0.0.1", 9042);

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

        Assert.assertEquals(totalTraces + 1, traceRepository.getTotalNumberOfTraces());

        traceRepository.deleteTraces(Collections.singletonList(traceData.getId()));

        Assert.assertEquals(totalTraces, traceRepository.getTotalNumberOfTraces());
    }

}
