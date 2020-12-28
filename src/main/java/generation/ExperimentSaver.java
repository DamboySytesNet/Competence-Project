package generation;

import repository.POIRepository;
import repository.RepositorySaver;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class ExperimentSaver {

    public static void saveExperimentResults(Experiment experiment) {
        saveObjectsByChunkingTo(experiment.getUsers(), new UserRepository());
        saveObjectsByChunkingTo(experiment.getPois(), new POIRepository());
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
}
