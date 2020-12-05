package learning;

import ai.djl.Model;
import ai.djl.ndarray.types.Shape;
import ai.djl.nn.Activation;
import ai.djl.nn.Blocks;
import ai.djl.nn.SequentialBlock;
import ai.djl.nn.core.Linear;
import ai.djl.training.DefaultTrainingConfig;
import ai.djl.training.EasyTrain;
import ai.djl.training.Trainer;
import ai.djl.training.TrainingResult;
import ai.djl.training.dataset.Dataset;
import ai.djl.training.evaluator.Accuracy;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.loss.Loss;
import ai.djl.translate.TranslateException;

import java.io.IOException;
import java.nio.file.Paths;

public class CompetencyModel {
    private final String name;
    private final int inputSize;
    private final int outputSize;
    private Model model;
    private DefaultTrainingConfig config;

    public CompetencyModel(String name, int inputSize, int outputSize) {
        this.name = name;
        this.inputSize = inputSize;
        this.outputSize = outputSize;

        this.initModel();
    }

    private void initModel() {
        int[] hidden = new int[] {128, 64};
        SequentialBlock sequentialBlock = new SequentialBlock();
        sequentialBlock.add(Blocks.batchFlattenBlock(this.inputSize));

        for (int hiddenSize : hidden) {
            sequentialBlock.add(Linear.builder().setUnits(hiddenSize).build());
            sequentialBlock.add(Activation.reluBlock());
        }

        sequentialBlock.add(Linear.builder().setUnits(this.outputSize).build());

        this.model = Model.newInstance(this.name);
        this.model.setBlock(sequentialBlock);

        this.config = new DefaultTrainingConfig(Loss.softmaxCrossEntropyLoss())
                .addEvaluator(new Accuracy())
                .addTrainingListeners(TrainingListener.Defaults.logging());

    }

    public TrainingResult fit(Dataset trainDataset, Dataset testDataset, int epochs) {
        try (Trainer trainer = this.model.newTrainer(this.config)){
            Shape inputShape = new Shape(this.inputSize);
            trainer.initialize(inputShape);
            EasyTrain.fit(trainer, epochs, trainDataset, testDataset);
            return trainer.getTrainingResult();
        } catch (TranslateException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(String outputDir) {
        try {
            this.model.save(Paths.get(outputDir), this.name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
