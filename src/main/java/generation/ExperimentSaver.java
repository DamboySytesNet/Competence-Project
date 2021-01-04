package generation;

import connectors.CassandraConnector;
import mappers.TraceDataMapper;
import model.Trace;
import model.TraceData;
import repository.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExperimentSaver {

    public static void saveExperimentResults(Experiment experiment) throws SQLException {
        ExperimentRepository.save(experiment);
        saveObjectsByChunkingTo(experiment.getUsers(), new UserRepository());
        saveObjectsByChunkingTo(experiment.getPois(), new POIRepository());
        saveGeneratedTraces(experiment.getTraces());
    }

    // Note:
    // Even though you can have 1000 rows in a single INSERT... VALUES statement,
    // doesn’t mean you should.
    // 25 rows worked best.
    private static void saveObjectsByChunkingTo(List list,
                                                RepositorySaver repositorySaver) {
        List chunk = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            chunk.add(list.get(i));
            if (chunk.size() == 25
                    || i == list.size() - 1) {
                repositorySaver.saveAllGeneric(chunk);
                chunk = new ArrayList<>();
            }
        }
    }

    private static void saveGeneratedTraces(List<Trace> traces){
        List<TraceData> tracesData = TraceDataMapper.mapTracesToTracesData(traces);
        CassandraConnector connector = new CassandraConnector();
        connector.connect();

        TraceDataRepository traceDataRepository = new TraceDataRepository(connector.getSession());

        List<TraceData> chunk = new ArrayList<>();

        chunk.add(tracesData.get(0));

        for (int i = 1; i < tracesData.size(); i++) {
            tracesData.get(i).setPreviousTraceId(tracesData.get(i-1).getId());
            chunk.add(tracesData.get(i));
            if (chunk.size() == 10
                    || i == tracesData.size() - 1) {
                traceDataRepository.insertTraces(chunk);
                chunk = new ArrayList<>();
            }
        }
        connector.close();
    }
}
