package generation;

import cluster.POICluster;
import cluster.clusterable.POIWrapper;
import model.Geolocalization;
import model.POI;
import model.POIType;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ClusterTest {

    @Test
    public void KMeanPOI() {
        //given:
        POICluster poiCluster = new POICluster(3, 75);

        List<POI> pois = new ArrayList<>();
        pois.add(new POI("A1", "desc", new Geolocalization(49.14, 15.11), POIType.outdoor, "1"));
        pois.add(new POI("A2", "desc", new Geolocalization(46.14, 18.11), POIType.outdoor, "1"));
        pois.add(new POI("B1", "desc", new Geolocalization(29.14, 15.11), POIType.outdoor, "1"));
        pois.add(new POI("C1", "desc", new Geolocalization(66.14, 65.11), POIType.outdoor, "1"));
        pois.add(new POI("C2", "desc", new Geolocalization(61.14, 75.21), POIType.outdoor, "1"));
        pois.add(new POI("B2", "desc", new Geolocalization(22.14, 19.11), POIType.outdoor, "1"));
        pois.add(new POI("B3", "desc", new Geolocalization(29.14, 9.21), POIType.outdoor, "1"));
        pois.add(new POI("C3", "desc", new Geolocalization(55.14, 64.21), POIType.outdoor, "1"));
        pois.add(new POI("A1", "desc", new Geolocalization(44.14, 17.11), POIType.outdoor, "1"));


        //when:
        List<CentroidCluster<POIWrapper>> poisWrapper = poiCluster.KMeanPOI(pois);

        //then:
        Assert.assertEquals(3, poisWrapper.size());
        Assert.assertTrue( "w zaleznosci od losowych center moga pojawic sie dwa przypadki",
                poisWrapper.get(0).getPoints().size() == 3 ||
                        poisWrapper.get(0).getPoints().size() == 6);
        Assert.assertEquals(2, poisWrapper.get(0).getCenter().getPoint().length);


    }
}
