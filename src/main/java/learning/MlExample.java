package learning;

import ai.djl.training.TrainingResult;
import learning.dataset.DummyDataset;

public class MlExample {
    public static void main(String[] args) {
        DummyDataset trainDataset = new DummyDataset.Builder(100, 10)
                .setSampling(16, false).build();
        DummyDataset testDataset = new DummyDataset.Builder(100,10)
                .setSampling(1 ,false).build();

        CompetencyModel model1 = new CompetencyModel("model", trainDataset.getRowSize(),
                trainDataset.getNumClasses());

        TrainingResult result =  model1.fit(trainDataset, testDataset, 5);
        System.out.println(result.getEvaluations());
        model1.save("models");
    }
}
