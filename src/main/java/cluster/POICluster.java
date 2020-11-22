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

    private List<POI> initData() {

        List<POI> pois = new ArrayList<>();
        pois.add(new POI("A1", "desc", new Geolocalization(49.14, 15.11), POIType.outdoor));
        pois.add(new POI("A2", "desc", new Geolocalization(46.14, 18.11), POIType.outdoor));
        pois.add(new POI("B1", "desc", new Geolocalization(29.14, 15.11), POIType.outdoor));
        pois.add(new POI("C1", "desc", new Geolocalization(66.14, 65.11), POIType.outdoor));
        pois.add(new POI("C2", "desc", new Geolocalization(61.14, 75.21), POIType.outdoor));
        pois.add(new POI("B2", "desc", new Geolocalization(22.14, 19.11), POIType.outdoor));
        pois.add(new POI("B3", "desc", new Geolocalization(29.14, 9.21), POIType.outdoor));
        return pois;
    }

    public void KMeanPOI() {
        List<POIWrapper> poiCluster= initData().stream()
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
