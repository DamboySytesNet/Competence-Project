package generation;

import lombok.Getter;
import model.POI;
import model.Trace;
import model.User;
import repository.ExperimentRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
public class Experiment {
    private UUID id;
    private List<User> users;
    private List<POI> pois;
    private List<Trace> traces;

    private LocalDateTime currentTime;
    private final LocalDateTime startTime;
    private final int timeStep; // min
    private final TraceGenerator traceGenerator;

    public Experiment(int noUsers, int noPois, int noTraces, int timeStep) {
        this(UUID.randomUUID(), noUsers, noPois, noTraces, timeStep);
    }

    public Experiment(UUID expId, int noUsers, int noPois, int noTraces, int timeStep) {
        this.id = expId;
        this.startTime = LocalDateTime.now();
        this.currentTime = startTime;
        this.timeStep = timeStep;

        this.pois = new LinkedList<>();
        for (int i = 0; i < noPois; ++i) {
            pois.add(POIFactory.getInstance().generate(id.toString()));
        }

        this.users = new LinkedList<>();
        for (int i = 0; i < noUsers; ++i) {
            try {
                users.add(UserFactory.getInstance().generate(id.toString()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        this.traces = new LinkedList<>();
        this.traceGenerator = new TraceGenerator(users, pois, currentTime);
        do {
            this.currentTime = currentTime.plusMinutes(this.timeStep);
            traces.addAll(traceGenerator.generateTraces(currentTime, ExperimentRepository.DEFAULT_ID));
        } while (traces.size() < noTraces);
    }
}
