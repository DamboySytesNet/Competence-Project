package learning;

import ai.djl.training.TrainingResult;
import generation.Experiment;
import learning.dataset.MyDataset;
import learning.dataset.Row;
import model.Trace;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MlExample {
    public static void main(String[] args) {
        List<Row> rows = new LinkedList<>();
//        DummyDataset trainDataset = new DummyDataset.Builder(100, 10)
//                .setSampling(16, false).build();
//        DummyDataset testDataset = new DummyDataset.Builder(100,10)
//                .setSampling(1 ,false).build();

        Experiment experiment = new Experiment(5, 10, 1000, 5);
        List<Route> routes = experiment.getTraces().stream()
                .collect(Collectors.groupingBy(Trace::getUser))
                .entrySet().stream()
                .map(entry -> new Route(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());


        for (Route route : routes) {
            rows.addAll(
                    route.getGroupedTraces(3).stream()
                            .map(lt -> new Row(route.getUser(), lt))
                            .collect(Collectors.toList()));
        }
        System.out.println("Phase 1");
        MyDataset myDataset = new MyDataset.Builder()
                .setSampling(16, false)
                .build(rows);

        CompetencyModel model1 = new CompetencyModel("model", myDataset.getRowSize(),
                myDataset.getNumClasses());

        System.out.println("Phase 2");
        TrainingResult result =  model1.fit(
                myDataset, //should be train ds
                myDataset, //should be test ds
                5);
        System.out.println(result.getEvaluations());
        model1.save("models");
    }

}
