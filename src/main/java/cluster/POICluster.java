package cluster;

import cluster.clusterable.POIWrapper;
import model.POI;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import java.util.List;
import java.util.stream.Collectors;


public class POICluster {

    private final int K;
    private final int iterations;

    public POICluster(int k, int iterations) {
        K = k;
        this.iterations = iterations;
    }

    public List<CentroidCluster<POIWrapper>> KMeanPOI(List<POI> poiData ) {
        List<POIWrapper> poiCluster= poiData.stream()
                .map(POIWrapper::new)
                .collect(Collectors.toList());

        KMeansPlusPlusClusterer<POIWrapper> clusterer = new KMeansPlusPlusClusterer<>(K, iterations);
        return clusterer.cluster(poiCluster);
    }
}
