package learning.dataset;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.training.dataset.RandomAccessDataset;
import ai.djl.training.dataset.Record;
import ai.djl.translate.TranslateException;
import ai.djl.util.Progress;

import java.io.IOException;

public class DummyDataset extends RandomAccessDataset {
    private int rowSize;
    private int numClasses;
    private DummyDataset(BaseBuilder<?> builder, int rowSize, int numClasses) {
        super(builder);
        this.rowSize = rowSize;
        this.numClasses = numClasses;
    }

    @Override
    protected Record get(NDManager ndManager, long l) throws IOException {
        float[] record = this.getRandRow();
        NDArray datum = ndManager.create(record);
        NDArray label = ndManager.create(Math.round(Math.random() * this.numClasses));
        return new Record(new NDList(datum), new NDList(label));
    }

    private float[] getRandRow() {
        float[] row = new float[this.rowSize];
        for (int i = 0; i < this.rowSize; i++) {
            row[i] = (float) Math.random();
        }
        return row;
    }

    public int getRowSize() {
        return this.rowSize;
    }

    public int getNumClasses() {
        return this.numClasses;
    }

    @Override
    protected long availableSize() {
        return 1000;
    }

    @Override
    public void prepare(Progress progress) throws IOException, TranslateException {
    }

    public static final class Builder extends BaseBuilder<Builder> {
        private int rowSize;
        private int numClasses;

        public Builder(int rowSize, int numClasses) {
            this.rowSize = rowSize;
            this.numClasses = numClasses;
        }

        @Override
        protected Builder self() {
            return this;
        }

        public DummyDataset build() {
            return new DummyDataset(this, rowSize, this.numClasses);
        }
    }
}
