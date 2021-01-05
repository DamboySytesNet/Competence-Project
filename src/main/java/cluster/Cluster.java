package cluster;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Cluster<P, W extends Clusterable> {

    private final int K;
    private final int iterations;

    public Cluster(int k, int iterations) {
        K = k;
        this.iterations = iterations;
    }

    public List<CentroidCluster<W>> KMean(List<P> data, Function<P, W> convert) {
        List<W> poiCluster= data.stream()
                .map(convert)
                .collect(Collectors.toList());

        KMeansPlusPlusClusterer<W> clusterer = new KMeansPlusPlusClusterer<W>(K, iterations);
        return clusterer.cluster(poiCluster);
    }
}
