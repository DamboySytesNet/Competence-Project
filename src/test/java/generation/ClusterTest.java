package generation;

import cluster.POICluster;
import cluster.clusterable.POIWrapper;
import model.POI;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class ClusterTest {

    private final List<POI> pois = new ArrayList<>();

    public ClusterTest() {
        for (int i = 0; i < 9; i++) {
            pois.add(POIFactory.getInstance().generate());
        }
    }

    @Test
    public void KMeanPOIForVarious() {
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
            Assert.assertTrue(poisWrapper.stream()
                    .flatMap(t -> t.getPoints().stream())
                    .allMatch(new HashSet<>()::add));
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

