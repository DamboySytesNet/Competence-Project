package cluster;

import cluster.clusterable.POIWrapper;
import model.Geolocalization;
import model.POI;
import model.POIType;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class POICluster {

    private final int K;
    private final int iterations;

    public POICluster(int k, int iterations) {
        K = k;
        this.iterations = iterations;
    }

    public void KMeanPOI(List<POI> poiData ) {
        List<POIWrapper> poiCluster= poiData.stream()
                .map(POIWrapper::new)
                .collect(Collectors.toList());

        KMeansPlusPlusClusterer<POIWrapper> clusterer = new KMeansPlusPlusClusterer<>(K, iterations);
        List<CentroidCluster<POIWrapper>> clusterResults = clusterer.cluster(poiCluster);

        for (int i=0; i<clusterResults.size(); i++) {
            System.out.println("Cluster " + i);
            for (POIWrapper locationWrapper : clusterResults.get(i).getPoints())
                System.out.println(locationWrapper.getPoi().getName());
            System.out.println();
        }
    }
}
