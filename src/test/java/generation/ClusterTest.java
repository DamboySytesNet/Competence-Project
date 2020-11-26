package generation;

import cluster.POICluster;
import cluster.clusterable.POIWrapper;
import model.Geolocalization;
import model.POI;
import model.POIType;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class ClusterTest {

    List<POI> pois = new ArrayList<>() {{
        add(new POI("A1", "desc", new Geolocalization(49.14, 15.11), POIType.outdoor, "1"));
        add(new POI("A2", "desc", new Geolocalization(46.14, 18.11), POIType.outdoor, "1"));
        add(new POI("B1", "desc", new Geolocalization(29.14, 15.11), POIType.outdoor, "1"));
        add(new POI("C1", "desc", new Geolocalization(66.14, 65.11), POIType.outdoor, "1"));
        add(new POI("C2", "desc", new Geolocalization(61.14, 75.21), POIType.outdoor, "1"));
        add(new POI("B2", "desc", new Geolocalization(22.14, 19.11), POIType.outdoor, "1"));
        add(new POI("B3", "desc", new Geolocalization(29.14, 9.21), POIType.outdoor, "1"));
        add(new POI("C3", "desc", new Geolocalization(55.14, 64.21), POIType.outdoor, "1"));
        add(new POI("A1", "desc", new Geolocalization(44.14, 17.11), POIType.outdoor, "1"));
    }};

    @Test
    public void KMeanPOIForAll() {
        //given:
        int clusters = 3;
        POICluster poiCluster = new POICluster(clusters, 75);

        for (int i = 0; i < 100; i++) {
            //when:
            Collections.shuffle(pois);
            List<POI> reducedPois = pois.subList(0, new Random().nextInt(pois.size() - clusters) + clusters);
            List<CentroidCluster<POIWrapper>> poisWrapper = poiCluster.KMeanPOI(reducedPois);

            //then:
            Assert.assertEquals(clusters, poisWrapper.size());
            Assert.assertEquals(2, poisWrapper.get(0).getCenter().getPoint().length);
            Assert.assertEquals(reducedPois.size(),
                    poisWrapper.stream().mapToInt(cluster -> cluster.getPoints().size()).sum());
            Assert.assertTrue(poisWrapper.stream().allMatch(new HashSet<>()::add));

        }
    }

    @Test(expected = org.apache.commons.math3.exception.NumberIsTooSmallException.class)
    public void KMeanPOI() {
        //given:
        int clusters = 10;
        POICluster poiCluster = new POICluster(clusters, 75);

        //then:
        Assert.assertTrue(pois.size() < clusters);
        poiCluster.KMeanPOI(pois);
    }
}

