package learning.dataset;


import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.training.dataset.RandomAccessDataset;
import ai.djl.training.dataset.Record;
import ai.djl.translate.TranslateException;
import ai.djl.util.Progress;

import java.io.IOException;
import java.util.List;

public class MyDataset extends RandomAccessDataset {
    private List<Row> rows;

    private MyDataset(BaseBuilder<?> builder, List<Row> rows) {
        super(builder);
        this.rows = rows;
    }

    @Override
    protected Record get(NDManager ndManager, long l) throws IOException {
        Float[] record = rows.get((int)l).getData();
        float[] recordf = new float[record.length];
        for (int i=0; i<record.length; ++i) {
            recordf[i] = record[i];
        }
        NDArray datum = ndManager.create(recordf);
        NDArray label = ndManager.create(rows.get((int)l).getLabel());

        return new Record(new NDList(datum), new NDList(label));
    }

    @Override
    protected long availableSize() {
        return this.rows.size();
    }

    public int getRowSize() {
        return this.rows.size();
    }

    public int getNumClasses() {
        return 2;
    }

    @Override
    public void prepare(Progress progress) throws IOException, TranslateException {
    }

    public static final class Builder extends BaseBuilder<Builder> {


        @Override
        protected Builder self() {
            return this;
        }

        public MyDataset build(List<Row> rows) {
            return new MyDataset(this, rows);
        }
    }
}
