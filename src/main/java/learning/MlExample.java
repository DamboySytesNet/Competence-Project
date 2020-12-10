package learning;

import ai.djl.training.TrainingResult;
import generation.Experiment;
import learning.dataset.Dataset;
import learning.dataset.Row;
import model.Trace;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MlExample {
    public static void main(String[] args) {
        List<Row> rows = new LinkedList<>();

        Experiment experiment = new Experiment(5, 10, 1000, 5);
        List<Route> routes = experiment.getTraces().stream()
                .collect(Collectors.groupingBy(Trace::getUser))
                .entrySet().stream()
                .map(entry -> new Route(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());


        routes.forEach(route -> {
            rows.addAll(
                    route.getGroupedTraces(3).stream()
                            .map(lt -> new Row(route.getUser(), lt, (int)Math.round(Math.random() * 2)))
                            .collect(Collectors.toList()));
        });

        System.out.println("Phase 1");
        Dataset trainDataset = new Dataset.Builder()
                .setSampling(16, true)
                .build(rows);

        CompetencyModel model1 = new CompetencyModel("model", trainDataset.getRowSize(),
                trainDataset.getNumClasses());

        System.out.println("Phase 2");
        TrainingResult result =  model1.fit(
                trainDataset, // should be train ds
                trainDataset, // should be test ds
                5);
        System.out.println(result.getEvaluations());
        model1.save("models");
    }

}
